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
import com.zetyun.streamtau.manager.controller.mapper.JobRequestMapper;
import com.zetyun.streamtau.manager.controller.protocol.JobRequest;
import com.zetyun.streamtau.manager.service.JobService;
import com.zetyun.streamtau.manager.service.dto.JobDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Job APIs")
@RestController
@RequestMapping("/projects/{projectId}/jobs")
@JsonView({PeaParser.Public.class})
public class JobController {
    @Autowired
    private JobService jobService;

    @Operation(summary = "Create a job.")
    @PostMapping("")
    public JobDto create(
        @Parameter(description = "The id of the project.")
        @PathVariable("projectId") String projectId,
        @RequestBody JobRequest jobRequest) throws IOException {
        JobDto dto = JobRequestMapper.MAPPER.toDto(jobRequest);
        return jobService.create(projectId, dto);
    }
}
