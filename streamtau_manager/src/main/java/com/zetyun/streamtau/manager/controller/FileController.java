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
import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.file.File;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.ProjectService;
import com.zetyun.streamtau.manager.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/projects/{projectId}/assets/{assetId}")
@JsonView({PeaParser.Public.class})
public class FileController {
    @Autowired
    private AssetService assetService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private StorageService storageService;

    @Operation(summary = "Upload or replace the real file of an file asset of a project.")
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AssetPea upload(
        @Parameter(description = "The id of the project.")
        @PathVariable("projectId") String projectId,
        @Parameter(description = "The id of the asset.")
        @PathVariable("assetId") String assetId,
        @Parameter(description = "The file to upload.")
        @RequestPart("file") MultipartFile file
    ) throws IOException {
        Long pid = projectService.mapProjectId(projectId);
        File pea = (File) assetService.findById(pid, assetId);
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new StreamTauException("10202");
        }
        String fileType = File.typeFromName(fileName);
        if (!pea.getType().equals(fileType)) {
            throw new StreamTauException("10203", pea.getType(), fileType);
        }
        String path = pea.getPath();
        if (path == null || path.isEmpty()) {
            path = storageService.createFile(pea.getExtension());
            pea.setPath(path);
            assetService.update(pid, pea);
        }
        storageService.saveFile(path, file);
        return pea;
    }
}
