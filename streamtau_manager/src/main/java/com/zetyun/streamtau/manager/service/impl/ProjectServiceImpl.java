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

package com.zetyun.streamtau.manager.service.impl;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.manager.db.mapper.ProjectMapper;
import com.zetyun.streamtau.manager.db.mapper.UserProjectMapper;
import com.zetyun.streamtau.manager.db.model.Project;
import com.zetyun.streamtau.manager.db.model.UserProject;
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.service.ProjectService;
import com.zetyun.streamtau.manager.service.UserService;
import com.zetyun.streamtau.manager.service.dto.ProjectDto;
import com.zetyun.streamtau.manager.service.mapper.ProjectDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UserProjectMapper userProjectMapper;
    @Autowired
    private UserService userService;

    @Override
    public List<ProjectDto> listAll() {
        List<Project> models = projectMapper.findAllOfUser(userService.getLoginUser());
        return models.stream()
            .map(ProjectDtoMapper.MAPPER::fromModel)
            .collect(Collectors.toList());
    }

    @Override
    public ProjectDto create(ProjectDto dto) {
        Project model = ProjectDtoMapper.MAPPER.toModel(dto);
        projectMapper.insert(model);
        String userId = userService.getLoginUser();
        UserProject userProject = new UserProject(userId, model.getProjectId(), null);
        userProjectMapper.addToUser(userProject);
        model.setUserProjectId(userProject.getUserProjectId());
        return ProjectDtoMapper.MAPPER.fromModel(model);
    }

    @Override
    public ProjectDto update(ProjectDto dto) {
        Project model = ProjectDtoMapper.MAPPER.toModel(dto);
        if (projectMapper.updateForUser(userService.getLoginUser(), model) == 0) {
            throw new StreamTauException("10001", dto.getId());
        }
        return ProjectDtoMapper.MAPPER.fromModel(model);
    }

    @Override
    public void delete(String userProjectId) {
        UserProject userProject = new UserProject(userService.getLoginUser(), null, userProjectId);
        if (userProjectMapper.deleteFromUser(userProject) == 0) {
            throw new StreamTauException("10001", userProjectId);
        }
    }

    @Override
    public Long mapProjectId(String userProjectId) {
        String userId = userService.getLoginUser();
        UserProject userProject = userProjectMapper.getByIds(new UserProject(userId, null, userProjectId));
        if (userProject == null) {
            throw new StreamTauException("10001", userProjectId);
        }
        return userProject.getProjectId();
    }

    @Override
    public JsonSchema schema() throws IOException {
        JsonSchema schema = PeaParser.JSON.createJsonSchema(ProjectDto.class);
        return schema;
    }
}
