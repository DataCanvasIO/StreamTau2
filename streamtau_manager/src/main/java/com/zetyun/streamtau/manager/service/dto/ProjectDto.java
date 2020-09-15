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

package com.zetyun.streamtau.manager.service.dto;

import com.zetyun.streamtau.manager.db.model.ProjectType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProjectDto {
    @Schema(description = "The id of the project.", example = "f08c2459-abc4-4d1e-b24a-2077abc76123")
    private String id;
    @Schema(description = "The name of the project.", example = "My project")
    private String name;
    @Schema(description = "The description of the project.", example = "blah blah...")
    private String description;
    @Schema(description = "The type of the project.", example = "CONTAINER")
    private ProjectType type;
}
