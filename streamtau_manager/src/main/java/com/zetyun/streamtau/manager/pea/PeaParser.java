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

package com.zetyun.streamtau.manager.pea;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.zetyun.streamtau.manager.db.model.ScriptFormat;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PeaParser {
    public static final PeaParser JSON = createJsonPeaParser();
    public static final PeaParser YAML = createYamlPeaParser();
    private final ObjectMapper mapper;

    public static PeaParser get(ScriptFormat format) {
        switch (format) {
            case APPLICATION_JSON:
                return JSON;
            case APPLICATION_YAML:
                return YAML;
            default:
                throw new IllegalArgumentException("Unknown script format \"" + format + "\".");
        }
    }

    private static PeaParser createJsonPeaParser() {
        JsonMapper mapper = new JsonMapper();
        return new PeaParser(mapperWithCommonProperties(mapper));
    }

    private static PeaParser createYamlPeaParser() {
        ObjectMapper mapper;
        YAMLFactory yamlFactory = new YAMLFactory()
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        mapper = new ObjectMapper(yamlFactory);
        return new PeaParser(mapperWithCommonProperties(mapper));
    }

    private static ObjectMapper mapperWithCommonProperties(ObjectMapper mapper) {
        return mapper
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    }

    public void parse(Object pea, String json) throws IOException {
        mapper.readerForUpdating(pea).readValue(json);
    }

    public <T> T parse(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    public String stringAll(Object pea) throws IOException {
        return mapper.writeValueAsString(pea);
    }

    public String stringWithoutHidden(Object pea) throws IOException {
        return mapper.writerWithView(NoHidden.class).writeValueAsString(pea);
    }

    public static class Hidden {
    }

    public static class NoHidden {
    }
}
