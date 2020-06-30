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

package com.zetyun.streamtau.manager.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.zetyun.streamtau.manager.pea.JobDefPod;

import java.io.IOException;
import java.util.List;

public class ResourceUtils {
    public static final ObjectMapper JSON_MAPPER = new JsonMapper();

    public static JobDefPod readJobDef(String classPath) throws IOException {
        return JobDefPod.fromJobDefinition(ResourceUtils.class.getResourceAsStream(classPath));
    }

    public static String readJsonCompact(String classPath) throws IOException {
        JsonNode tree = JSON_MAPPER.readTree(ResourceUtils.class.getResourceAsStream(classPath));
        return JSON_MAPPER.writeValueAsString(tree);
    }

    public static <T> List<T> readObjectFromCsv(String classPath, Class<T> clazz) throws IOException {
        MappingIterator<T> objIterator = new CsvMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .readerFor(clazz)
            .with(CsvSchema.emptySchema().withHeader())
            .readValues(ResourceUtils.class.getResourceAsStream(classPath));
        return objIterator.readAll();
    }
}
