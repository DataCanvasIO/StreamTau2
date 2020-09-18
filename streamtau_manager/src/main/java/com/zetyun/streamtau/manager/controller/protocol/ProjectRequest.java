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

package com.zetyun.streamtau.manager.controller.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zetyun.streamtau.manager.db.model.ProjectType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
    @Schema(
        description = "The name of the project.",
        required = true,
        example = "My project"
    )
    @NotBlank
    @Size(max = 255)
    @JsonProperty(value = "name", required = true)
    private String name;
    @Schema(
        description = "The description of the project.",
        example = "blah blah ..."
    )
    @Size(max = 511)
    @JsonProperty("description")
    private String description;
    @Schema(
        description = "The type of the project.",
        example = "CONTAINER"
    )
    @JsonProperty(value = "type", required = true, defaultValue = "CONTAINER")
    private ProjectType type;
}
