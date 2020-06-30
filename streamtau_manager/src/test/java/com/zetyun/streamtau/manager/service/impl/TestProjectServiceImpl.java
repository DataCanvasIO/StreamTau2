/*
 * Copyright 2020 Zetyun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zetyun.streamtau.manager.service.impl;

import com.zetyun.streamtau.manager.db.mapper.ProjectMapper;
import com.zetyun.streamtau.manager.db.mapper.UserProjectMapper;
import com.zetyun.streamtau.manager.db.model.Project;
import com.zetyun.streamtau.manager.db.model.UserProject;
import com.zetyun.streamtau.manager.service.ProjectService;
import com.zetyun.streamtau.manager.service.UserService;
import com.zetyun.streamtau.manager.service.dto.ProjectDto;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ProjectServiceImpl.class})
public class TestProjectServiceImpl {
    @Autowired
    private ProjectService projectService;

    @MockBean
    private ProjectMapper projectMapper;
    @MockBean
    private UserProjectMapper userProjectMapper;
    @MockBean
    private UserService userService;

    @BeforeClass
    public static void setupClass() {
    }

    @Before
    public void setup() {
        when(userService.getLoginUser()).thenReturn("user1");
    }

    @Test
    public void testListAll() {
        Project project = new Project();
        project.setProjectId(1L);
        project.setUserProjectId("AAA");
        project.setProjectName("1st");
        project.setProjectDescription("The first project.");
        when(projectMapper.findAllOfUser(anyString())).thenReturn(Collections.singletonList(project));
        List<ProjectDto> dtoList = projectService.listAll();
        assertThat(dtoList.size(), is(1));
        ProjectDto dto = new ProjectDto();
        dto.setId("AAA");
        dto.setName("1st");
        dto.setDescription("The first project.");
        assertThat(dtoList, hasItem(dto));
        verify(projectMapper, times(1)).findAllOfUser("user1");
    }

    @Test
    public void testCreate() {
        when(projectMapper.insert(any(Project.class))).then(args -> {
            Project model = args.getArgument(0);
            model.setProjectId(2L);
            return 1;
        });
        when(userProjectMapper.addToUser(any(UserProject.class))).then(args -> {
            UserProject model = args.getArgument(0);
            model.setUserProjectId("BBB");
            return 1;
        });
        ProjectDto dto = new ProjectDto();
        dto.setName("forCreate");
        dto = projectService.create(dto);
        assertThat(dto.getId(), is("BBB"));
        assertThat(dto.getName(), is("forCreate"));
        assertThat(dto.getDescription(), nullValue());
        verify(projectMapper, times(1)).insert(any(Project.class));
        verify(userProjectMapper, times(1)).addToUser(any(UserProject.class));
        verify(userService, times(1)).getLoginUser();
    }

    @Test
    public void testUpdate() {
        when(projectMapper.updateForUser(anyString(), any(Project.class))).thenReturn(1);
        ProjectDto dto = new ProjectDto();
        dto.setId("AAA");
        dto.setName("forUpdate");
        dto = projectService.update(dto);
        assertThat(dto.getId(), is("AAA"));
        assertThat(dto.getName(), is("forUpdate"));
        assertThat(dto.getDescription(), nullValue());
        verify(projectMapper, times(1)).updateForUser(eq("user1"), any(Project.class));
        verify(userService, times(1)).getLoginUser();
    }

    @Test
    public void testDelete() {
        when(userProjectMapper.deleteFromUser(any(UserProject.class))).thenReturn(1);
        projectService.delete("AAA");
        verify(userProjectMapper, times(1)).deleteFromUser(any(UserProject.class));
    }

    @Test
    public void testMapProjectId() {
        when(userProjectMapper.getByIds(any(UserProject.class))).then(args -> {
            UserProject model = args.getArgument(0);
            model.setProjectId(3L);
            return model;
        });
        assertThat(projectService.mapProjectId("AAA"), is(3L));
    }
}
