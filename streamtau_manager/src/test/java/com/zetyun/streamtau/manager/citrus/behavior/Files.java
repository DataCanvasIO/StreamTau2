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
import com.zetyun.streamtau.manager.controller.advise.StreamTauResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.zetyun.streamtau.manager.citrus.CitrusCommon.SERVER_ID;
import static com.zetyun.streamtau.manager.citrus.CitrusCommon.varRef;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

public class Files {
    @RequiredArgsConstructor
    public static class Upload extends AbstractTestBehavior {
        private final String projectId;
        private final String assetId;
        private final Resource resource;

        @Override
        public void apply() {
            MultiValueMap<String, Object> payload = new LinkedMultiValueMap<>();
            payload.add("file", resource);
            http().client(SERVER_ID).send()
                .post("/projects/" + varRef(Projects.idVarName(projectId))
                    + "/assets/" + varRef(Assets.idVarName(assetId))
                    + "/file/")
                .accept(APPLICATION_JSON_VALUE)
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .payload(payload);
            http().client(SERVER_ID).receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .jsonPath("$.status", StreamTauResponse.OK)
                .jsonPath("$.message", StreamTauResponse.SUCCESS)
                .jsonPath("$.data.id", varRef(Assets.idVarName(assetId)));
            echo("Upload file for asset " + varRef(Assets.idVarName(assetId)) + " successfully.");
        }
    }
}
