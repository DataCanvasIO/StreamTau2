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

import com.fasterxml.jackson.annotation.JsonView;
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.AssetPeaFactory;
import com.zetyun.streamtau.manager.pea.PeaParser;
import com.zetyun.streamtau.manager.pea.file.File;
import com.zetyun.streamtau.manager.service.AssetService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "File APIs")
@RestController
@RequestMapping("projects/{projectId}/files")
@JsonView({PeaParser.Public.class})
public class FileController {
    @Autowired
    private AssetService assetService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AssetPea upload(
        @Parameter(description = "The id of the project.")
        @PathVariable("projectId") String projectId,
        @Parameter(
            description = "The name of the file asset.",
            required = true,
            example = "Jar file for app 1."
        )
        @RequestPart("name") String name,
        @Parameter(
            description = "The description of the file.",
            example = "This file is the main class for java app."
        )
        @RequestPart("description") String description,
        @Parameter(
            description = "The file to upload.",
            required = true
        )
        @RequestPart("file") MultipartFile file
    ) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new StreamTauException("10202");
        }
        String fileType = File.typeFromName(fileName);
        File pea = (File) AssetPeaFactory.INS.make(fileType);
        pea.setName(name);
        pea.setDescription(description);
        return assetService.create(projectId, pea);
    }
}
