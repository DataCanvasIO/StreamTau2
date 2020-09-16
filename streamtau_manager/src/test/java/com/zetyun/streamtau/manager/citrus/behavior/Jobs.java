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

package com.zetyun.streamtau.manager.citrus.behavior;

import com.consol.citrus.dsl.design.AbstractTestBehavior;
import com.consol.citrus.message.MessageType;
import com.zetyun.streamtau.manager.controller.protocol.JobRequest;
import com.zetyun.streamtau.manager.controller.protocol.StreamTauResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.zetyun.streamtau.manager.citrus.CitrusCommon.SERVER_ID;
import static com.zetyun.streamtau.manager.citrus.CitrusCommon.varRef;
import static com.zetyun.streamtau.manager.helper.ResourceUtils.JSON_MAPPER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class Jobs {
    private static String idVarName(String id) {
        return "job_id" + "_" + id;
    }

    @RequiredArgsConstructor
    public static class Create extends AbstractTestBehavior {
        private final String projectId;
        private final String id;
        private final JobRequest request;

        @Override
        public void apply() {
            http().client(SERVER_ID).send()
                .post("/api/projects/" + varRef(Projects.idVarName(projectId)) + "/jobs/")
                .accept(APPLICATION_JSON_VALUE)
                .payload(request, JSON_MAPPER);
            http().client(SERVER_ID).receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .jsonPath("$.status", StreamTauResponse.OK)
                .jsonPath("$.message", StreamTauResponse.SUCCESS)
                .jsonPath("$.data.name", request.getName())
                .jsonPath("$.data.appId", request.getAppId())
                .jsonPath("$.data.jobStatus", request.getJobStatus())
                .extractFromPayload("$.data.id", idVarName(id));
            echo("Created job " + varRef(idVarName(id)) + " successfully.");
        }
    }
}
