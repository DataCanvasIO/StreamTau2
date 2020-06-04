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

import com.zetyun.streamtau.manager.db.model.JobStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class JobRequest {
    @Schema(description = "The name of the job.", example = "New Job")
    private String name;
    @Schema(description = "The id of the app.", required = true, example = "8eeac63d-05c0-4f98-b38b-851db722f640")
    private String appId;
    @Schema(description = "The initial status of the job.", example = "READY")
    private JobStatus jobStatus;
}
