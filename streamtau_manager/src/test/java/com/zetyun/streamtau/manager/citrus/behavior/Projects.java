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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.zetyun.streamtau.manager.controller.protocol.ProjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class Projects {
    private static final String SERVER_ID = "streamtau-manager";

    private static final ObjectMapper mapper = new JsonMapper();

    @RequiredArgsConstructor
    public static class Create extends AbstractTestBehavior {
        private final ProjectRequest request;

        @Override
        public void apply() {
            http().client(SERVER_ID).send()
                .post("/projects/")
                .accept(APPLICATION_JSON_VALUE)
                .payload(request, mapper);
            http().client(SERVER_ID).receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .jsonPath("$.status", "0")
                .jsonPath("$.data.name", request.getName())
                .jsonPath("$.data.description", request.getDescription())
                .jsonPath("$.data.type", request.getType())
                .extractFromPayload("$.data.id", "projectId");
            echo("Created project ${projectId} successfully.");
        }
    }
}
