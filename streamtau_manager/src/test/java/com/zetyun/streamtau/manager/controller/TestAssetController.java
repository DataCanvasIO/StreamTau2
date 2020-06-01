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
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.service.AssetService;
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

import static com.zetyun.streamtau.manager.helper.Utils.errorCode;
import static com.zetyun.streamtau.manager.helper.Utils.success;
import static org.hamcrest.CoreMatchers.nullValue;
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

    @Test
    public void testListAll() throws Exception {
        CmdLine pea = new CmdLine();
        pea.setId("AAA");
        pea.setName("testListAll");
        pea.setCmd("ls");
        when(assetService.listAll("ABC")).thenReturn(Collections.singletonList(pea));
        mvc.perform(
            get("/projects/ABC/assets")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.size()").value(1))
            .andExpect(jsonPath("$.data[0].id").value("AAA"))
            .andExpect(jsonPath("$.data[0].name").value("testListAll"))
            .andExpect(jsonPath("$.data[0].description").value(nullValue()))
            .andExpect(jsonPath("$.data[0].type").value("CmdLine"))
            .andExpect(jsonPath("$.data[0].cmd").value("ls"));
        verify(assetService, times(1)).listAll("ABC");
    }

    @Test
    public void testCreate() throws Exception {
        when(assetService.create(eq("ABC"), any(AssetPea.class))).then(args -> {
            AssetPea pea = args.getArgument(1);
            pea.setId("AAA");
            return pea;
        });
        mvc.perform(
            post("/projects/ABC/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testCreate\", \"type\": \"HostPlat\", \"hostname\": \"localhost\"}")
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.id").value("AAA"))
            .andExpect(jsonPath("$.data.name").value("testCreate"))
            .andExpect(jsonPath("$.data.description").value(nullValue()))
            .andExpect(jsonPath("$.data.type").value("HostPlat"))
            .andExpect(jsonPath("$.data.hostname").value("localhost"));
        verify(assetService, times(1)).create(eq("ABC"), any(AssetPea.class));
    }

    @Test
    public void testCreateWrongFormat() throws Exception {
        mvc.perform(
            post("/projects/ABC/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testCreate\", \"format\": \"json\"}")
        )
            .andDo(print())
            .andExpect(errorCode("10201"));
        verifyNoInteractions(assetService);
    }

    @Test
    public void testUpdate() throws Exception {
        when(assetService.update(eq("ABC"), any(AssetPea.class))).then(returnsSecondArg());
        mvc.perform(
            put("/projects/ABC/assets/AAA")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testUpdate\", \"type\": \"HostPlat\", \"hostname\": \"localhost\"}")
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.id").value("AAA"))
            .andExpect(jsonPath("$.data.name").value("testUpdate"))
            .andExpect(jsonPath("$.data.description").value(nullValue()))
            .andExpect(jsonPath("$.data.type").value("HostPlat"))
            .andExpect(jsonPath("$.data.hostname").value("localhost"));
        verify(assetService, times(1)).update(eq("ABC"), any(AssetPea.class));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(
            delete("/projects/ABC/assets/AAA")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(success());
        verify(assetService, times(1)).delete("ABC", "AAA");
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
