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

package com.zetyun.streamtau.runtime.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.zetyun.streamtau.runtime.schema.RtSchemaNode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class SchemaNodeTypeMismatch extends RuntimeException {
    private static final long serialVersionUID = 546096342035441897L;

    @Getter
    private final RtSchemaNode schemaNode;
    @Getter
    private final JsonNode jsonNode;

    public SchemaNodeTypeMismatch(
        @NotNull RtSchemaNode schemaNode,
        @NotNull JsonNode jsonNode
    ) {
        super(
            "Json node is require to be " + schemaNode.getType().getRequired() + ", "
                + "but is \"" + jsonNode.getNodeType() + "\"."
        );
        this.schemaNode = schemaNode;
        this.jsonNode = jsonNode;
    }
}
