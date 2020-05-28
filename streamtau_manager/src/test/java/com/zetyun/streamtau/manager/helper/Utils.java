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

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Utils {
    public static <T> List<T> readObjectFromCsv(String classPath, Class<T> clazz) throws IOException {
        MappingIterator<T> objIterator = new CsvMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .readerFor(clazz)
            .with(CsvSchema.emptySchema().withHeader())
            .readValues(Utils.class.getResourceAsStream(classPath));
        return objIterator.readAll();
    }

    /**
     * Define 'success' for WebMvcTest.
     *
     * @return what 'success' means.
     */
    public static ResultMatcher success() {
        return ResultMatcher.matchAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.status").value("0"),
            jsonPath("$.message").value("success")
        );
    }

    /**
     * Define 'errorCode' for WebMvcTest.
     *
     * @return what 'errorCode' means.
     */
    public static ResultMatcher errorCode(String code) {
        return ResultMatcher.matchAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.status").value(code),
            jsonPath("$.message").value(notNullValue(String.class)),
            jsonPath("$.data").value(nullValue())
        );
    }
}
