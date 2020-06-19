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

package com.zetyun.streamtau.manager.controller;

import com.zetyun.streamtau.manager.controller.advise.GlobalExceptionHandler;
import com.zetyun.streamtau.manager.controller.advise.ResponseBodyDecorator;
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.service.ProjectService;
import com.zetyun.streamtau.manager.service.dto.ProjectDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Locale;

import static com.zetyun.streamtau.manager.helper.WebMvcTestUtils.errorCode;
import static com.zetyun.streamtau.manager.helper.WebMvcTestUtils.success;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ProjectController.class})
public class TestProjectController {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ProjectService projectService;

    @Test
    public void testListAll() throws Exception {
        ProjectDto dto = new ProjectDto();
        dto.setId("AAA");
        dto.setName("testListAll");
        when(projectService.listAll()).thenReturn(Collections.singletonList(dto));
        mvc.perform(
            get("/projects")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.size()").value(1))
            .andExpect(jsonPath("$.data[0].id").value("AAA"))
            .andExpect(jsonPath("$.data[0].name").value("testListAll"))
            .andExpect(jsonPath("$.data[0].description").value(nullValue()));
        verify(projectService, times(1)).listAll();
    }

    @Test
    public void testCreate() throws Exception {
        when(projectService.create(any(ProjectDto.class))).then(args -> {
            ProjectDto dto = args.getArgument(0);
            dto.setId("AAA");
            return dto;
        });
        mvc.perform(
            post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testCreate\"}")
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.id").value("AAA"))
            .andExpect(jsonPath("$.data.name").value("testCreate"))
            .andExpect(jsonPath("$.data.description").value(nullValue()));
        verify(projectService, times(1)).create(any(ProjectDto.class));
    }

    @Test
    public void testUpdate() throws Exception {
        when(projectService.update(any(ProjectDto.class))).then(returnsFirstArg());
        mvc.perform(
            put("/projects/AAA")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testUpdate\"}")
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.id").value("AAA"))
            .andExpect(jsonPath("$.data.name").value("testUpdate"))
            .andExpect(jsonPath("$.data.description").value(nullValue()));
        verify(projectService, times(1)).update(any(ProjectDto.class));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(
            delete("/projects/AAA")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(success());
        verify(projectService, times(1)).delete("AAA");
    }

    @Test
    public void testDeleteException() throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        doThrow(new StreamTauException("10001", "AAA")).when(projectService).delete("AAA");
        mvc.perform(
            delete("/projects/AAA")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(errorCode("10001"));
        verify(projectService, times(1)).delete("AAA");
    }

    @Test
    public void testUnknownError() throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        doThrow(new RuntimeException("unknown")).when(projectService).delete("AAA");
        mvc.perform(
            delete("/projects/AAA")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(errorCode("100000"));
    }

    // Mock application
    @Configuration
    @EnableAutoConfiguration
    @Import({
        ProjectController.class,
        ResponseBodyDecorator.class,
        GlobalExceptionHandler.class,
    })
    static class Config {
    }
}
