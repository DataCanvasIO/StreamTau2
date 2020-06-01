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

import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Tag(name = "Asset APIs")
@RestController
@RequestMapping("/projects/{projectId}/assets")
public class AssetController {
    @Autowired
    private AssetService assetService;

    @Operation(summary = "List all assets of a project.")
    @GetMapping("")
    public List<AssetPea> listAll(
        @Parameter(description = "The id of the project.")
        @PathVariable("projectId") String projectId
    ) throws IOException {
        return assetService.listAll(projectId);
    }

    @Operation(summary = "Create a new asset for a project.")
    @PostMapping("")
    public AssetPea create(
        @Parameter(description = "The id of the project.")
        @PathVariable("projectId") String projectId,
        @RequestBody AssetPea pea) throws IOException {
        return assetService.create(projectId, pea);
    }

    @Operation(summary = "Update an asset of a project.")
    @PutMapping("/{id}")
    public AssetPea update(
        @Parameter(description = "The id of the project.")
        @PathVariable("projectId") String projectId,
        @Parameter(description = "The id of the asset.")
        @PathVariable("id") String id,
        @RequestBody AssetPea pea) throws IOException {
        pea.setId(id);
        return assetService.update(projectId, pea);
    }

    @Operation(summary = "Delete an asset from a project.")
    @DeleteMapping("/{id}")
    void delete(
        @Parameter(description = "The id of the project.")
        @PathVariable("projectId") String projectId,
        @Parameter(description = "The id of the asset.")
        @PathVariable("id") String id) {
        assetService.delete(projectId, id);
    }
}
