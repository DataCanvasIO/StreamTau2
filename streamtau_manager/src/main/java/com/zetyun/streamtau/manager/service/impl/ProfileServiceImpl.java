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

package com.zetyun.streamtau.manager.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;
import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.manager.controller.protocol.ElementProfile;
import com.zetyun.streamtau.manager.controller.protocol.ProjectRequest;
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.pea.AssetPeaFactory;
import com.zetyun.streamtau.manager.service.ProfileService;
import com.zetyun.streamtau.manager.utils.CustomizedJsonSchemaModule;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final SchemaGenerator generator;

    private final Map<String, String> profileCache;

    public ProfileServiceImpl() {
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(
            SchemaVersion.DRAFT_2019_09,
            OptionPreset.PLAIN_JSON
        )
            .without(Option.SCHEMA_VERSION_INDICATOR)
            .with(new JacksonModule(
                JacksonOption.IGNORE_TYPE_INFO_TRANSFORM,
                JacksonOption.RESPECT_JSONPROPERTY_ORDER
            ))
            .with(new CustomizedJsonSchemaModule());
        SchemaGeneratorConfig config = configBuilder.build();
        generator = new SchemaGenerator(config);
        profileCache = new LinkedHashMap<>(65);
    }

    @Override
    public String get(String element) {
        return profileCache.computeIfAbsent(element, this::generateProfile);
    }

    private String generateProfile(@Nonnull String element) {
        ElementProfile profile = new ElementProfile();
        if (element.equals("Project")) {
            JsonNode jsonSchema = generator.generateSchema(ProjectRequest.class);
            profile.setSchema(jsonSchema);
        } else {
            Class<?> peaClass = AssetPeaFactory.INS.getPeaClass(element);
            if (peaClass != null) {
                JsonNode jsonSchema = generator.generateSchema(peaClass);
                profile.setSchema(jsonSchema);
            }
        }
        if (profile.getSchema() != null) {
            try {
                return PeaParser.JSON.stringShowAll(profile);
            } catch (IOException ignored) {
            }
        }
        throw new StreamTauException("10003", element);
    }
}
