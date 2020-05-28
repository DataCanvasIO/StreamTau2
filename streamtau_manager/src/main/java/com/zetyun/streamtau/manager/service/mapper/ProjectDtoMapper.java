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

package com.zetyun.streamtau.manager.service.mapper;

import com.zetyun.streamtau.manager.db.model.Project;
import com.zetyun.streamtau.manager.service.dto.ProjectDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectDtoMapper {
    ProjectDtoMapper MAPPER = Mappers.getMapper(ProjectDtoMapper.class);

    @Mappings({
        @Mapping(source = "userProjectId", target = "id"),
        @Mapping(source = "projectName", target = "name"),
        @Mapping(source = "projectDescription", target = "description"),
        @Mapping(source = "projectType", target = "type"),
    })
    ProjectDto fromModel(Project model);

    @Mappings({
        @Mapping(source = "id", target = "userProjectId"),
        @Mapping(source = "name", target = "projectName"),
        @Mapping(source = "description", target = "projectDescription"),
        @Mapping(source = "type", target = "projectType"),
    })
    Project toModel(ProjectDto dto);
}
