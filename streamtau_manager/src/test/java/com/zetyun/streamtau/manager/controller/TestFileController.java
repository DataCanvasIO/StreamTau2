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
import com.zetyun.streamtau.manager.pea.file.JarFile;
import com.zetyun.streamtau.manager.service.AssetService;
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

import static com.zetyun.streamtau.manager.helper.Utils.success;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    public void testUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jar",
            "application/java-archive",
            (byte[]) null
        );
        MockMultipartFile name = new MockMultipartFile(
            "name",
            null,
            "text/plain",
            "testJar".getBytes()
        );
        MockMultipartFile desc = new MockMultipartFile(
            "description",
            null,
            "text/plain",
            "A jar file.".getBytes()
        );
        when(assetService.create(eq("ABC"), any(AssetPea.class))).then(args -> {
            AssetPea pea = args.getArgument(1);
            pea.setId("AAA");
            return pea;
        });
        mvc.perform(
            multipart("/projects/ABC/files")
                .file(file)
                .file(name)
                .file(desc)
        )
            .andDo(print())
            .andExpect(success())
            .andExpect(jsonPath("$.data.id").value("AAA"))
            .andExpect(jsonPath("$.data.name").value("testJar"))
            .andExpect(jsonPath("$.data.description").value("A jar file."))
            .andExpect(jsonPath("$.data.type").value("JarFile"));
        verify(assetService, times(1)).create(eq("ABC"), any(JarFile.class));
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
