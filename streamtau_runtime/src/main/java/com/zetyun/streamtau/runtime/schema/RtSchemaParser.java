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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.zetyun.streamtau.runtime.ScriptFormat;
import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.runtime.exception.MissingRequiredItem;
import com.zetyun.streamtau.runtime.exception.MissingRequiredKey;
import com.zetyun.streamtau.runtime.exception.SchemaNodeTypeMismatch;
import com.zetyun.streamtau.runtime.exception.UnsupportedFormat;
import com.zetyun.streamtau.runtime.exception.UnsupportedSchemaType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RtSchemaParser implements Serializable {
    private static final ObjectMapper JSON = createJsonMapper();
    private static final ObjectMapper YAML = createYamlMapper();

    private static final long serialVersionUID = 8125735181754377412L;

    private final ObjectMapper mapper;
    private final RtSchema schema;
    private final int maxIndex;

    @Contract(pure = true)
    public RtSchemaParser(@NotNull ScriptFormat format, RtSchema schema) {
        switch (format) {
            case APPLICATION_JSON:
                mapper = JSON;
                break;
            case APPLICATION_YAML:
                mapper = YAML;
                break;
            default:
                throw new UnsupportedFormat(format);
        }
        this.schema = schema;
        this.maxIndex = schema.createIndex(0);
    }

    private static @NotNull ObjectMapper createJsonMapper() {
        JsonMapper mapper = new JsonMapper();
        return mapperWithCommonProperties(mapper);
    }

    @Contract(" -> new")
    private static @NotNull ObjectMapper createYamlMapper() {
        ObjectMapper mapper;
        YAMLFactory yamlFactory = new YAMLFactory()
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        mapper = new ObjectMapper(yamlFactory);
        return mapperWithCommonProperties(mapper);
    }

    private static ObjectMapper mapperWithCommonProperties(@NotNull ObjectMapper mapper) {
        return mapper
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull RtSchemaParser createJsonEventParser(RtSchema schema) {
        return new RtSchemaParser(ScriptFormat.APPLICATION_JSON, schema);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull RtSchemaParser createYamlEventParser(RtSchema schema) {
        return new RtSchemaParser(ScriptFormat.APPLICATION_YAML, schema);
    }

    public RtEvent parse(String text) throws IOException {
        JsonNode jsonNode = mapper.readTree(text);
        return jsonNodeToEvent(jsonNode);
    }

    public RtEvent parse(InputStream is) throws IOException {
        JsonNode jsonNode = mapper.readTree(new InputStreamReader(is));
        return jsonNodeToEvent(jsonNode);
    }

    public String stringify(RtEvent event) throws JsonProcessingException {
        return stringify(event, false);
    }

    public String stringify(RtEvent event, boolean pretty) throws JsonProcessingException {
        ObjectMapper mapper = this.mapper;
        if (pretty) {
            mapper = this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        JsonNode jsonNode = toJsonNodeAccordingSchema(event, schema);
        return mapper.writeValueAsString(jsonNode);
    }

    private @NotNull RtEvent jsonNodeToEvent(JsonNode jsonNode) {
        RtEvent event = new RtEvent(maxIndex);
        parseAccordingSchema(event, jsonNode, schema);
        return event;

    }

    private void parseAccordingSchema(RtEvent event, JsonNode jsonNode, @NotNull RtSchema schema) {
        switch (schema.getType()) {
            case INT:
                event.set(schema.getIndex(), jsonNode.asLong());
                return;
            case REAL:
                event.set(schema.getIndex(), jsonNode.asDouble());
                return;
            case STR:
                event.set(schema.getIndex(), jsonNode.asText());
                return;
            case BOOL:
                event.set(schema.getIndex(), jsonNode.asBoolean());
                return;
            case INT_ARRAY:
                Long[] intArray = new Long[jsonNode.size()];
                for (int i = 0; i < jsonNode.size(); i++) {
                    intArray[i] = jsonNode.get(i).asLong();
                }
                event.set(schema.getIndex(), intArray);
                return;
            case REAL_ARRAY:
                Double[] realArray = new Double[jsonNode.size()];
                for (int i = 0; i < jsonNode.size(); i++) {
                    realArray[i] = jsonNode.get(i).asDouble();
                }
                event.set(schema.getIndex(), realArray);
                return;
            case STR_ARRAY:
                String[] strArray = new String[jsonNode.size()];
                for (int i = 0; i < jsonNode.size(); i++) {
                    strArray[i] = jsonNode.get(i).asText();
                }
                event.set(schema.getIndex(), strArray);
                return;
            case BOOL_ARRAY:
                Boolean[] boolArray = new Boolean[jsonNode.size()];
                for (int i = 0; i < jsonNode.size(); i++) {
                    boolArray[i] = jsonNode.get(i).asBoolean();
                }
                event.set(schema.getIndex(), boolArray);
                return;
            case LIST:
                if (jsonNode.isArray()) {
                    event.set(schema.getIndex(), jsonNodeValue(jsonNode));
                    return;
                }
                break;
            case MAP:
                if (jsonNode.isObject()) {
                    event.set(schema.getIndex(), jsonNodeValue(jsonNode));
                    return;
                }
                break;
            case TUPLE:
                RtSchemaTuple schemaTuple = (RtSchemaTuple) schema;
                for (int i = 0; i < schemaTuple.getChildren().length; i++) {
                    JsonNode item = jsonNode.get(i);
                    if (item != null) {
                        parseAccordingSchema(event, jsonNode.get(i), schemaTuple.getChildren()[i]);
                    } else {
                        throw new MissingRequiredItem(jsonNode, i);
                    }
                }
                return;
            case DICT:
                RtSchemaDict schemaObject = (RtSchemaDict) schema;
                for (Map.Entry<String, RtSchema> entry : schemaObject.getChildren().entrySet()) {
                    String key = entry.getKey();
                    JsonNode child = jsonNode.get(key);
                    if (child != null) {
                        parseAccordingSchema(event, jsonNode.get(key), entry.getValue());
                    } else {
                        throw new MissingRequiredKey(jsonNode, key);
                    }
                }
                return;
            default:
                throw new UnsupportedSchemaType(schema);
        }
        throw new SchemaNodeTypeMismatch(schema, jsonNode);
    }

    private @Nullable Object jsonNodeValue(@NotNull JsonNode jsonNode) {
        switch (jsonNode.getNodeType()) {
            case NUMBER:
                if (jsonNode.isInt()) {
                    return jsonNode.asLong();
                }
                return jsonNode.asDouble();
            case STRING:
                return jsonNode.asText();
            case BOOLEAN:
                return jsonNode.asBoolean();
            case ARRAY:
                List<Object> list = new LinkedList<>();
                for (int i = 0; i < jsonNode.size(); i++) {
                    list.add(jsonNodeValue(jsonNode.get(i)));
                }
                return list;
            case OBJECT:
                Map<String, Object> map = new HashMap<>(jsonNode.size());
                Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
                while (it.hasNext()) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    map.put(entry.getKey(), jsonNodeValue(entry.getValue()));
                }
                return map;
            case NULL:
                return null;
            default:
                break;
        }
        throw new IllegalArgumentException(
            "Unsupported json node type \"" + jsonNode.getNodeType() + "\"."
        );
    }

    private JsonNode toJsonNodeAccordingSchema(RtEvent event, @NotNull RtSchema schema) {
        JsonNode jsonNode;
        switch (schema.getType()) {
            case INT:
                jsonNode = LongNode.valueOf((Long) event.get(schema.getIndex()));
                break;
            case REAL:
                jsonNode = DoubleNode.valueOf((Double) event.get(schema.getIndex()));
                break;
            case STR:
                jsonNode = TextNode.valueOf((String) event.get(schema.getIndex()));
                break;
            case BOOL:
                jsonNode = BooleanNode.valueOf((Boolean) event.get(schema.getIndex()));
                break;
            case INT_ARRAY:
            case REAL_ARRAY:
            case STR_ARRAY:
            case BOOL_ARRAY:
            case LIST:
            case MAP:
                jsonNode = mapper.valueToTree(event.get(schema.getIndex()));
                break;
            case TUPLE:
                jsonNode = mapper.createArrayNode();
                RtSchemaTuple schemaTuple = (RtSchemaTuple) schema;
                for (int i = 0; i < schemaTuple.getChildren().length; i++) {
                    ((ArrayNode) jsonNode).add(toJsonNodeAccordingSchema(event, schemaTuple.getChildren()[i]));
                }
                break;
            case DICT:
                ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance);
                RtSchemaDict schemaObject = (RtSchemaDict) schema;
                for (Map.Entry<String, RtSchema> entry : schemaObject.getChildren().entrySet()) {
                    objectNode.set(entry.getKey(), toJsonNodeAccordingSchema(event, entry.getValue()));
                }
                jsonNode = objectNode;
                break;
            default:
                throw new UnsupportedSchemaType(schema);
        }
        return jsonNode;
    }
}
