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

package com.zetyun.streamtau.manager.junit4.db.mapper;

import com.zetyun.streamtau.manager.db.mapper.ProjectMapper;
import com.zetyun.streamtau.manager.db.model.Project;
import com.zetyun.streamtau.manager.db.model.ProjectType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.zetyun.streamtau.manager.helper.ResourceUtils.readObjectFromCsv;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestProjectMapper {
    private static List<Project> projects;

    @Autowired
    private ProjectMapper projectMapper;

    @BeforeClass
    public static void setupClass() throws IOException {
        projects = readObjectFromCsv("/db/data/project.csv", Project.class);
    }

    @Test
    public void testFindAll() {
        List<Project> modelList = projectMapper.findAll();
        assertThat(modelList.size(), is(projects.size()));
        for (Project project : projects) {
            assertThat(modelList, hasItem(project));
        }
    }

    @Test
    public void testFindById() {
        Project model = projectMapper.findById(2L);
        assertThat(model, is(projects.get(1)));
    }

    @Test
    public void testFindByIdNotExists() {
        Project model = projectMapper.findById(10000L);
        assertThat(model, nullValue());
    }

    @Test
    public void testInsert() {
        Project model = new Project();
        model.setProjectName("Project4");
        model.setProjectType(ProjectType.CONTAINER);
        assertThat(projectMapper.insert(model), is(1));
        assertThat(model.getProjectId(), notNullValue());
        List<Project> modelList = projectMapper.findAll();
        assertThat(modelList.size(), is(projects.size() + 1));
        assertThat(modelList, hasItem(model));
    }

    @Test
    public void testUpdate() {
        Project model = new Project();
        model.setProjectId(1L);
        model.setProjectDescription("Updated desc.");
        assertThat(projectMapper.update(model), is(1));
        model = projectMapper.findById(1L);
        assertThat(model.getProjectName(), is("COMMON"));
        assertThat(model.getProjectDescription(), is("Updated desc."));
    }

    @Test
    public void testUpdateNotExists() {
        Project model = new Project();
        model.setProjectId(10000L);
        model.setProjectName("foobar");
        assertThat(projectMapper.update(model), is(0));
    }

    @Test
    public void testDelete() {
        assertThat(projectMapper.delete(3L), is(1));
        assertThat(projectMapper.findById(3L), nullValue());
    }

    @Test
    public void testDeleteNotExists() {
        assertThat(projectMapper.delete(10000L), is(0));
    }

    @Test
    public void testFindAllOfUser() {
        List<Project> modelList = projectMapper.findAllOfUser("user1");
        assertThat(modelList.size(), is(1));
        assertThat(modelList, hasItems(projects.get(1)));
    }

    @Test
    public void testFindByIdOfUser() {
        Project model = projectMapper.findByIdOfUser("user1", "14b96595-f7f1-4800-a98a-c3d44d9c7e03");
        assertThat(model, is(projects.get(1)));
    }

    @Test
    public void testFindByIdOfUserNotExists() {
        Project model = projectMapper.findByIdOfUser("user1", "ed648217-5f79-4a92-80dd-5592118b0ef8");
        assertThat(model, nullValue());
    }

    @Test
    public void testUpdateForUser() {
        Project model = new Project();
        model.setUserProjectId("14b96595-f7f1-4800-a98a-c3d44d9c7e03");
        model.setProjectDescription("Updated desc.");
        assertThat(projectMapper.updateForUser("user1", model), is(1));
        model = projectMapper.findById(2L);
        assertThat(model.getProjectName(), is("Project2"));
        assertThat(model.getProjectDescription(), is("Updated desc."));
    }

    @Test
    public void testUpdateForUserNotOwn() {
        Project model = new Project();
        model.setUserProjectId("14b96595-f7f1-4800-a98a-c3d44d9c7e03");
        model.setProjectDescription("Updated desc.");
        assertThat(projectMapper.updateForUser("user10000", model), is(0));
    }

    @Configuration
    @EnableAutoConfiguration
    @MapperScans({@MapperScan("com.zetyun.streamtau.manager.db.mapper")})
    static class Config {
    }
}
