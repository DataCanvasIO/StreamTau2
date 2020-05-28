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

package com.zetyun.streamtau.manager.controller.advise;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiResponse {
    private final String status;
    private final String message;
    private final Object data;

    public ApiResponse(Object data) {
        this.data = data;
        this.status = "0";
        this.message = "success";
    }

    public ApiResponse(String errorCode, String message) {
        this.status = errorCode;
        this.message = message;
        this.data = null;
    }
}
