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

package com.zetyun.streamtau.runtime.schema;

import lombok.Getter;

import java.util.List;
import java.util.Map;

public enum RtSchemaTypes {
    // single indexed vars
    INT(Long.class, "an integer"),
    REAL(Double.class, "a number"),
    STR(String.class, "a string"),
    BOOL(Boolean.class, "a boolean value"),
    INT_ARRAY(Long[].class, "an array of integers"),
    REAL_ARRAY(Double[].class, "an array of numbers"),
    STR_ARRAY(String[].class, "an array of strings"),
    BOOL_ARRAY(Boolean[].class, "an array of boolean values"),
    LIST(List.class, "an array"),
    MAP(Map.class, "an object"),
    // index into multiple vars
    TUPLE(null, null),
    DICT(null, null);

    @Getter
    private final Class<?> javaClass;
    @Getter
    private final String required;

    RtSchemaTypes(Class<?> javaClass, String required) {
        this.javaClass = javaClass;
        this.required = required;
    }
}
