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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StreamTauResponse {
    public static final String OK = "0";
    public static final String SUCCESS = "success";

    @JsonProperty("status")
    private final String status;
    @JsonProperty("message")
    private final String message;
    @JsonProperty("data")
    @JsonSerialize(using = ResponseDataSerializer.class, contentUsing = ResponseDataSerializer.class)
    private final Object data;

    public StreamTauResponse(Object data) {
        this.data = data;
        this.status = OK;
        this.message = SUCCESS;
    }

    public StreamTauResponse(String errorCode, String message) {
        this.status = errorCode;
        this.message = message;
        this.data = null;
    }
}
