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

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zetyun.streamtau.manager.config.DevWebMvcConfig;
import com.zetyun.streamtau.manager.controller.ProfileController;
import com.zetyun.streamtau.manager.controller.advise.GlobalExceptionHandler;
import com.zetyun.streamtau.manager.controller.advise.ResponseBodyDecorator;
import com.zetyun.streamtau.manager.db.model.AssetCategory;
import com.zetyun.streamtau.manager.service.ProfileService;
import com.zetyun.streamtau.manager.service.ProjectService;
import com.zetyun.streamtau.manager.service.dto.AssetTypeInfo;
import com.zetyun.streamtau.manager.service.dto.ElementProfile;
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

import static com.zetyun.streamtau.manager.helper.WebMvcTestUtils.success;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    @MockBean
    private ProjectService projectService;

    @Before
    public void setup() {
        when(projectService.mapId("PRJ")).thenReturn(2L);
    }

    @Test
    public void testGetProfile() throws Exception {
        ElementProfile profile = new ElementProfile();
        ObjectNode jsonNode = new ObjectNode(JsonNodeFactory.instance);
        jsonNode.put("type", "object");
        profile.setSchema(jsonNode);
        when(profileService.getInProject(2L, "Project")).thenReturn(profile);
        mvc.perform(
            get("/api/projects/PRJ/profile/Project")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.schema.type").value("object"));
    }

    @Test
    public void testAssetTypes() throws Exception {
        AssetTypeInfo assetTypeInfo = new AssetTypeInfo();
        assetTypeInfo.setType("TypeA");
        assetTypeInfo.setCategory(AssetCategory.FILE);
        when(profileService.listAssetTypesInProject(2L)).thenReturn(Collections.singletonList(assetTypeInfo));
        mvc.perform(
            get("/api/projects/PRJ/assetTypes")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect((success()))
            .andExpect(jsonPath("$.data[0].type").value("TypeA"))
            .andExpect(jsonPath("$.data[0].category").value("FILE"));
        verify(profileService, times(1)).listAssetTypesInProject(2L);
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
