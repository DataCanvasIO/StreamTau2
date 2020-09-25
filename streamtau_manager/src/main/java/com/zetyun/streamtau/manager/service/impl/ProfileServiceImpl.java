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
import com.zetyun.streamtau.manager.controller.protocol.ProjectRequest;
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.pea.AssetPeaFactory;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.ProfileService;
import com.zetyun.streamtau.manager.service.dto.AssetTypeInfo;
import com.zetyun.streamtau.manager.service.dto.ElementProfile;
import com.zetyun.streamtau.manager.utils.CustomizedJsonSchemaModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final CustomizedJsonSchemaModule module;
    private final SchemaGenerator generator;

    @Autowired
    private AssetService assetService;

    public ProfileServiceImpl() {
        module = new CustomizedJsonSchemaModule();
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(
            SchemaVersion.DRAFT_2019_09,
            OptionPreset.PLAIN_JSON
        )
            .without(Option.SCHEMA_VERSION_INDICATOR)
            .with(new JacksonModule(
                JacksonOption.IGNORE_TYPE_INFO_TRANSFORM,
                JacksonOption.RESPECT_JSONPROPERTY_ORDER
            ))
            .with(module);
        SchemaGeneratorConfig config = configBuilder.build();
        generator = new SchemaGenerator(config);
    }

    @Override
    public ElementProfile getInProject(Long projectId, @Nonnull String element) {
        ElementProfile profile = new ElementProfile();
        if (element.equals("Project")) {
            JsonNode jsonSchema = generator.generateSchema(ProjectRequest.class);
            profile.setSchema(jsonSchema);
            return profile;
        } else {
            Class<?> peaClass = AssetPeaFactory.INS.classOf(element);
            if (peaClass != null) {
                module.clearRefs();
                module.setProjectId(projectId);
                JsonNode jsonSchema = generator.generateSchema(peaClass);
                profile.setSchema(jsonSchema);
                profile.setRefs(module.getRefs());
                return profile;
            }
        }
        throw new StreamTauException("10003", element);
    }

    @Override
    public List<AssetTypeInfo> listAssetTypesInProject(Long projectId) {
        module.setProjectId(projectId);
        Map<String, Class<?>> peaClassMap = AssetPeaFactory.INS.getPeaClassMap();
        List<AssetTypeInfo> assetTypeInfoList = new ArrayList<>(peaClassMap.size());
        for (String type : peaClassMap.keySet()) {
            AssetTypeInfo model = new AssetTypeInfo();
            model.setType(type);
            model.setCategory(AssetPeaFactory.INS.make(type).getCategory());
            assetTypeInfoList.add(model);
        }
        return assetTypeInfoList;
    }
}
