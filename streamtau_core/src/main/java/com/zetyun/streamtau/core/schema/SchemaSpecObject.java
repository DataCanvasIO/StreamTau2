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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.zetyun.streamtau.runtime.schema.RtSchema;
import com.zetyun.streamtau.runtime.schema.RtSchemaDict;
import com.zetyun.streamtau.runtime.schema.RtSchemaTypes;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@JsonTypeName("object")
@JsonPropertyOrder({"type", "properties"})
public final class SchemaSpecObject extends SchemaSpec {
    @JsonProperty("properties")
    @Getter
    private Map<String, SchemaSpec> properties;

    @JsonProperty("additionalProperties")
    @Getter
    private Boolean additionalProperties;

    @Override
    public @NotNull RtSchema createRtSchema() {
        if (additionalProperties == null || additionalProperties) {
            return new RtSchema(RtSchemaTypes.MAP);
        }
        Map<String, RtSchema> children = new HashMap<>(properties.size());
        for (Map.Entry<String, SchemaSpec> entry : properties.entrySet()) {
            children.put(entry.getKey(), entry.getValue().createRtSchema());
        }
        RtSchemaDict dict = new RtSchemaDict();
        dict.setChildren(children);
        return dict;
    }
}
