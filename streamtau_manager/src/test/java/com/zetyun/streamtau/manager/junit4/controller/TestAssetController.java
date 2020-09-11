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

import com.zetyun.streamtau.manager.controller.AssetController;
import com.zetyun.streamtau.manager.controller.advise.GlobalExceptionHandler;
import com.zetyun.streamtau.manager.controller.advise.ResponseBodyDecorator;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.ProjectService;
import org.junit.Before;
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

import static com.zetyun.streamtau.manager.helper.WebMvcTestUtils.errorCode;
import static com.zetyun.streamtau.manager.helper.WebMvcTestUtils.success;
import static org.mockito.AdditionalAnswers.returnsSecondArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {AssetController.class})
public class TestAssetController {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AssetService assetService;
    @MockBean
    private ProjectService projectService;

    @Before
    public void setup() {
        when(projectService.mapProjectId("ABC")).thenReturn(2L);
    }

    @Test
    public void testListAll() throws Exception {
        CmdLine pea = new CmdLine();
        pea.setId("AAA");
        pea.setName("testListAll");
        pea.setCmd("ls");
        when(assetService.listAll(2L)).thenReturn(Collections.singletonList(pea));
        mvc.perform(
            get("/api/projects/ABC/assets")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.size()").value(1))
            .andExpect(jsonPath("$.data[0].id").value("AAA"))
            .andExpect(jsonPath("$.data[0].name").value("testListAll"))
            .andExpect(jsonPath("$.data[0].type").value("CmdLine"))
            .andExpect(jsonPath("$.data[0].cmd").value("ls"));
        verify(projectService, times(1)).mapProjectId("ABC");
        verify(assetService, times(1)).listAll(2L);
    }

    @Test
    public void testListByType() throws Exception {
        CmdLine pea = new CmdLine();
        pea.setId("AAA");
        pea.setName("testListAll");
        pea.setCmd("ls");
        when(assetService.listByType(2L, "CmdLine"))
            .thenReturn(Collections.singletonList(pea));
        mvc.perform(
            get("/api/projects/ABC/assets?type=CmdLine")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.size()").value(1))
            .andExpect(jsonPath("$.data[0].id").value("AAA"))
            .andExpect(jsonPath("$.data[0].name").value("testListAll"))
            .andExpect(jsonPath("$.data[0].type").value("CmdLine"))
            .andExpect(jsonPath("$.data[0].cmd").value("ls"));
        verify(projectService, times(1)).mapProjectId("ABC");
        verify(assetService, times(1)).listByType(2L, "CmdLine");
    }

    @Test
    public void testCreate() throws Exception {
        when(assetService.create(eq(2L), any(AssetPea.class))).then(args -> {
            AssetPea pea = args.getArgument(1);
            pea.setId("AAA");
            return pea;
        });
        mvc.perform(
            post("/api/projects/ABC/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testCreate\", \"type\": \"Host\", \"hostname\": \"localhost\"}")
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.id").value("AAA"))
            .andExpect(jsonPath("$.data.name").value("testCreate"))
            .andExpect(jsonPath("$.data.type").value("Host"))
            .andExpect(jsonPath("$.data.hostname").value("localhost"));
        verify(projectService, times(1)).mapProjectId("ABC");
        verify(assetService, times(1)).create(eq(2L), any(AssetPea.class));
    }

    @Test
    public void testCreateWrongFormat() throws Exception {
        mvc.perform(
            post("/api/projects/ABC/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testCreate\", \"format\": \"json\"}")
        )
            .andDo(print())
            .andExpect(errorCode("10201"));
        verifyNoInteractions(assetService);
    }

    @Test
    public void testUpdate() throws Exception {
        when(assetService.update(eq(2L), any(AssetPea.class))).then(returnsSecondArg());
        mvc.perform(
            put("/api/projects/ABC/assets/AAA")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testUpdate\", \"type\": \"Host\", \"hostname\": \"localhost\"}")
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.id").value("AAA"))
            .andExpect(jsonPath("$.data.name").value("testUpdate"))
            .andExpect(jsonPath("$.data.type").value("Host"))
            .andExpect(jsonPath("$.data.hostname").value("localhost"));
        verify(projectService, times(1)).mapProjectId("ABC");
        verify(assetService, times(1)).update(eq(2L), any(AssetPea.class));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(
            delete("/api/projects/ABC/assets/AAA")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(success());
        verify(projectService, times(1)).mapProjectId("ABC");
        verify(assetService, times(1)).delete(2L, "AAA");
    }

    @Test
    public void testTypes() throws Exception {
        mvc.perform(
            get("/api/projects/ABC/assets/types")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect((success()));
    }

    // Mock application
    @Configuration
    @EnableAutoConfiguration
    @Import({
        AssetController.class,
        ResponseBodyDecorator.class,
        GlobalExceptionHandler.class,
    })
    static class Config {
    }
}
