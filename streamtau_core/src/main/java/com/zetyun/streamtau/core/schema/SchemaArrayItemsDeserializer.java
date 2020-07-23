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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SchemaArrayItemsDeserializer extends StdDeserializer<SchemaArrayItems> {
    private static final long serialVersionUID = 5780462898172671343L;

    protected SchemaArrayItemsDeserializer() {
        super(SchemaArrayItems.class);
    }

    @Override
    public SchemaArrayItems deserialize(
        @NotNull JsonParser parser,
        DeserializationContext ctx
    ) throws IOException {
        JsonNode jsonNode = parser.readValueAsTree();
        SchemaArrayItems items = new SchemaArrayItems();
        if (jsonNode instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            items.setType(parser.getCodec().treeToValue(objectNode.get("type"), Types.class));
            return items;
        } else if (jsonNode instanceof ArrayNode) {
            items.setSpecs(parser.getCodec().treeToValue(jsonNode, SchemaSpec[].class));
            return items;
        }
        return (SchemaArrayItems) ctx.handleUnexpectedToken(_valueClass, parser);
    }
}
