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

package com.zetyun.streamtau.runtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.annotation.Nonnull;

@Getter
public enum ScriptFormat {
    TEXT_PLAIN("text/plain"),
    APPLICATION_JSON("application/json"),
    APPLICATION_YAML("application/yaml");

    @JsonValue
    private final String value;

    ScriptFormat(String value) {
        this.value = value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    @Nonnull
    public static ScriptFormat fromString(String str) {
        for (ScriptFormat scriptFormat : ScriptFormat.values()) {
            if (str.equals(scriptFormat.getValue())) {
                return scriptFormat;
            }
        }
        throw new IllegalArgumentException("Invalid string value \""
            + str + "\" for enum type \"" + ScriptFormat.class.getSimpleName() + "\".");
    }

    @Nonnull
    public static ScriptFormat fromExtension(@Nonnull String fileName) {
        if (fileName.endsWith(".yml") || fileName.endsWith(".yaml")) {
            return APPLICATION_YAML;
        } else if (fileName.endsWith(".json")) {
            return APPLICATION_JSON;
        } else if (fileName.endsWith(".txt")) {
            return TEXT_PLAIN;
        }
        throw new IllegalArgumentException("Invalid extension of file name \""
            + fileName + "\" for enum type \"" + ScriptFormat.class.getSimpleName() + "\".");
    }

    @Override
    public String toString() {
        return value;
    }
}
