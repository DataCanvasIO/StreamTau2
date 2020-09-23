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
import com.zetyun.streamtau.manager.controller.ProfileController;
import com.zetyun.streamtau.manager.controller.advise.GlobalExceptionHandler;
import com.zetyun.streamtau.manager.controller.advise.ResponseBodyDecorator;
import com.zetyun.streamtau.manager.service.ProfileService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ProfileController.class})
public class TestProfileController {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ProfileService profileService;

    @Test
    public void testGetProject() throws Exception {
        when(profileService.get(anyString())).thenReturn("{schema:{type:object}}");
        mvc.perform(
            get("/api/profile/Project")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.schema.type").value("object"));

    }

    // Mock application
    @Configuration
    @EnableAutoConfiguration
    @Import({
        ProfileController.class,
        ResponseBodyDecorator.class,
        GlobalExceptionHandler.class,
        DevWebMvcConfig.class,
    })
    static class Config {
    }
}
