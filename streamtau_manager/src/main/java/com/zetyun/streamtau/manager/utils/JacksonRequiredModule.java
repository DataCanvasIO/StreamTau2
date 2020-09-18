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

package com.zetyun.streamtau.manager.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.victools.jsonschema.generator.Module;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;

import javax.annotation.Nonnull;

public class JacksonRequiredModule implements Module {
    @Override
    public void applyToConfigBuilder(@Nonnull SchemaGeneratorConfigBuilder configBuilder) {
        configBuilder.forFields().withRequiredCheck(f -> f.getAnnotation(JsonProperty.class).required());
    }
}
