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
import com.zetyun.streamtau.manager.pea.file.JarFile;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static com.zetyun.streamtau.manager.helper.Utils.success;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {FileController.class})
public class TestFileController {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AssetService assetService;
    @MockBean
    private StorageService storageService;

    @Test
    public void testUpload() throws Exception {
        when(assetService.findById(eq("ABC"), eq("AAA"))).then(args -> {
            JarFile pea = new JarFile();
            pea.setId("AAA");
            pea.setName("testJar");
            pea.setDescription("A jar file.");
            return pea;
        });
        when(storageService.createFile(anyString())).thenReturn("file://abc");
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jar",
            "application/java-archive",
            (byte[]) null
        );
        mvc.perform(
            multipart("/projects/ABC/assets/AAA/upload")
                .file(file)
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.id").value("AAA"))
            .andExpect(jsonPath("$.data.name").value("testJar"))
            .andExpect(jsonPath("$.data.description").value("A jar file."))
            .andExpect(jsonPath("$.data.type").value("JarFile"));
        verify(assetService, times(1)).findById(eq("ABC"), eq("AAA"));
        verify(assetService, times(1)).update(eq("ABC"), any(JarFile.class));
        verify(storageService, times(1)).createFile("jar");
        verify(storageService, times(1)).saveFile(eq("file://abc"), any(MultipartFile.class));
    }

    // Mock application
    @Configuration
    @EnableAutoConfiguration
    @Import({
        FileController.class,
        ResponseBodyDecorator.class,
        GlobalExceptionHandler.class,
    })
    static class Config {
    }
}
