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

package com.zetyun.streamtau.streaming.exception;

import com.zetyun.streamtau.runtime.schema.RtSchemaRoot;
import lombok.Getter;

public class InvalidMappingTarget extends RuntimeException {
    private static final long serialVersionUID = 7222701441123124786L;

    @Getter
    private final String target;
    @Getter
    private final RtSchemaRoot schema;

    public InvalidMappingTarget(String target, RtSchemaRoot schema) {
        super(
            "Mapping target \"" + target + "\" must be a var in the following schema:\n" + schema
        );
        this.target = target;
        this.schema = schema;
    }
}
