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

import com.zetyun.streamtau.manager.controller.mapper.ProjectRequestMapper;
import com.zetyun.streamtau.manager.controller.protocol.ProjectRequest;
import com.zetyun.streamtau.manager.service.ProjectService;
import com.zetyun.streamtau.manager.service.dto.ProjectDto;
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

import java.util.List;

@Tag(name = "Project APIs")
@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Operation(summary = "List all projects.")
    @GetMapping("")
    public List<ProjectDto> listAll() {
        return projectService.listAll();
    }

    @Operation(summary = "Create a new project.")
    @PostMapping("")
    public ProjectDto create(@RequestBody ProjectRequest request) {
        return projectService.create(ProjectRequestMapper.MAPPER.toDto(request));
    }

    @Operation(summary = "Update a project.")
    @PutMapping("/{id}")
    public ProjectDto update(
        @Parameter(description = "The id of the project.")
        @PathVariable("id") String id,
        @RequestBody ProjectRequest request) {
        ProjectDto dto = ProjectRequestMapper.MAPPER.toDto(request);
        dto.setId(id);
        return projectService.update(dto);
    }

    @Operation(summary = "Delete a project.")
    @DeleteMapping("/{id}")
    public void delete(
        @Parameter(description = "The id of the project.")
        @PathVariable("id") String id) {
        projectService.delete(id);
    }
}
