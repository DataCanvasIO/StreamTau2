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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ServerDto {
    @Schema(description = "The id of the server.", example = "CF870D60-9978-43B0-B0AF-E1DCB11BF538")
    private String id;
    @Schema(description = "The name of the server.", example = "Localhost Flink")
    private String name;
    @Schema(description = "The type of the server.", example = "FlinkMiniCluster")
    private String type;
    @Schema(description = "The description of the server.", example = "Run flink app on localhost.")
    private String description;
    @Schema(description = "The status of the server.", example = "Active")
    private ServerStatus status;
}
