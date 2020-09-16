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
import com.zetyun.streamtau.manager.controller.protocol.ProjectRequest;
import com.zetyun.streamtau.manager.controller.protocol.StreamTauResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.zetyun.streamtau.manager.citrus.CitrusCommon.SERVER_ID;
import static com.zetyun.streamtau.manager.citrus.CitrusCommon.varRef;
import static com.zetyun.streamtau.manager.helper.ResourceUtils.JSON_MAPPER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public final class Projects {
    private Projects() {
    }

    public static String idVarName(String id) {
        return "project_id" + "_" + id;
    }

    @RequiredArgsConstructor
    public static class Create extends AbstractTestBehavior {
        private final String id;
        private final ProjectRequest request;

        @Override
        public void apply() {
            http().client(SERVER_ID).send()
                .post("/api/projects/")
                .accept(APPLICATION_JSON_VALUE)
                .payload(request, JSON_MAPPER);
            http().client(SERVER_ID).receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .jsonPath("$.status", StreamTauResponse.OK)
                .jsonPath("$.message", StreamTauResponse.SUCCESS)
                .jsonPath("$.data.name", request.getName())
                .jsonPath("$.data.description", request.getDescription())
                .jsonPath("$.data.type", request.getType())
                .extractFromPayload("$.data.id", idVarName(id));
            echo("Created project " + varRef(idVarName(id)) + " successfully.");
        }
    }

    @RequiredArgsConstructor
    public static class Delete extends AbstractTestBehavior {
        private final String id;

        @Override
        public void apply() {
            http().client(SERVER_ID).send()
                .delete("/api/projects/" + varRef(idVarName(id)))
                .accept(APPLICATION_JSON_VALUE);
            http().client(SERVER_ID).receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .jsonPath("$.status", StreamTauResponse.OK)
                .jsonPath("$.message", StreamTauResponse.SUCCESS);
            echo("Delete project " + varRef(idVarName(id)) + " successfully.");
        }
    }
}
