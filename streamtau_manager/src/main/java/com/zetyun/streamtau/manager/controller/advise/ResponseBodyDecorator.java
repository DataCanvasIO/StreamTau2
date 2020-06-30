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

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = {"com.zetyun.streamtau.manager.controller"})
public class ResponseBodyDecorator implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter parameter, Class clazz) {
        boolean isSupported = false;
        Class<?> aClass = parameter.getContainingClass();
        if (aClass.getAnnotation(RestController.class) != null) {
            isSupported = true;
        }
        return isSupported;
    }

    @Override
    public Object beforeBodyWrite(
        Object body,
        MethodParameter parameter,
        MediaType mediaType,
        Class clazz,
        ServerHttpRequest serverHttpRequest,
        ServerHttpResponse serverHttpResponse
    ) {
        return new StreamTauResponse(body);
    }
}
