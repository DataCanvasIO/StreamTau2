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

package com.zetyun.streamtau.manager.junit4.controller;

import com.zetyun.streamtau.manager.config.DevWebMvcConfig;
import com.zetyun.streamtau.manager.controller.JobController;
import com.zetyun.streamtau.manager.controller.advise.GlobalExceptionHandler;
import com.zetyun.streamtau.manager.controller.advise.ResponseBodyDecorator;
import com.zetyun.streamtau.manager.db.model.JobStatus;
import com.zetyun.streamtau.manager.service.JobService;
import com.zetyun.streamtau.manager.service.dto.JobDto;
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

import static com.zetyun.streamtau.manager.helper.WebMvcTestUtils.success;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {JobController.class})
public class TestJobController {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private JobService jobService;

    @Test
    public void testCreate() throws Exception {
        when(jobService.create(eq("ABC"), any(JobDto.class))).then(args -> {
            JobDto dto = args.getArgument(1);
            dto.setId(5L);
            if (dto.getName() == null) {
                dto.setName("New Job");
            }
            if (dto.getJobStatus() == null) {
                dto.setJobStatus(JobStatus.READY);
            }
            return dto;
        });
        mvc.perform(
            post("/api/projects/ABC/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"appId\": \"APP\"}")
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.id").value(5))
            .andExpect(jsonPath("$.data.name").value("New Job"))
            .andExpect(jsonPath("$.data.jobStatus").value("READY"));
        verify(jobService, times(1)).create(eq("ABC"), any(JobDto.class));
    }

    // Mock application
    @Configuration
    @EnableAutoConfiguration
    @Import({
        JobController.class,
        ResponseBodyDecorator.class,
        GlobalExceptionHandler.class,
        DevWebMvcConfig.class,
    })
    static class Config {
    }
}
