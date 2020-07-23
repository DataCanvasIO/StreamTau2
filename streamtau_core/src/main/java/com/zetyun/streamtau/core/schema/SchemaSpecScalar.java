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

package com.zetyun.streamtau.core.schema;

import com.zetyun.streamtau.runtime.schema.RtSchemaNode;
import com.zetyun.streamtau.runtime.schema.RtSchemaTypes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SchemaSpecScalar extends SchemaSpec {
    @Contract(" -> new")
    @Override
    public @NotNull RtSchemaNode createRtNode() {
        Types type = getType();
        switch (type) {
            case INTEGER:
                return new RtSchemaNode(RtSchemaTypes.INT);
            case NUMBER:
                return new RtSchemaNode(RtSchemaTypes.REAL);
            case STRING:
                return new RtSchemaNode(RtSchemaTypes.STR);
            case BOOLEAN:
                return new RtSchemaNode(RtSchemaTypes.BOOL);
            default:
                throw new IllegalArgumentException("Invalid schema type \"" + type + "\".");
        }
    }
}
