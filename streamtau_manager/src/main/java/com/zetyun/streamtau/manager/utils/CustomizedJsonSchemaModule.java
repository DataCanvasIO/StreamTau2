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
import com.fasterxml.jackson.annotation.JsonView;
import com.github.victools.jsonschema.generator.FieldScope;
import com.github.victools.jsonschema.generator.Module;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.zetyun.streamtau.core.pea.PeaId;
import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.manager.boot.ApplicationContextProvider;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.PeaType;
import com.zetyun.streamtau.manager.service.AssetService;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public class CustomizedJsonSchemaModule implements Module {
    @Getter
    @Setter
    private Long projectId;
    @Getter
    @Setter
    private Map<String, String> refs;

    public void clearRefs() {
        refs = new HashMap<>();
    }

    @Override
    public void applyToConfigBuilder(@Nonnull SchemaGeneratorConfigBuilder configBuilder) {
        configBuilder.forFields().withRequiredCheck(f -> {
            JsonProperty jsonProperty = f.getAnnotation(JsonProperty.class);
            return jsonProperty != null && jsonProperty.required();
        });
        configBuilder.forFields().withIgnoreCheck(f -> {
            JsonView jsonView = f.getAnnotation(JsonView.class);
            return jsonView != null
                && Arrays.stream(jsonView.value()).noneMatch(PeaParser.Show.class::equals);
        });
        configBuilder.forFields().withEnumResolver((FieldScope f) -> {
            PeaId peaId = f.getAnnotation(PeaId.class);
            if (peaId != null) {
                PeaType peaType = f.getAnnotation(PeaType.class);
                if (peaType != null) {
                    try {
                        AssetService assetService = ApplicationContextProvider.getAssetService();
                        List<AssetPea> peaList = assetService.listByTypes(projectId, peaType.value());
                        peaList.forEach(p -> refs.put(p.getId(), p.getName()));
                        return peaList.stream().map(AssetPea::getId).collect(Collectors.toList());
                    } catch (IOException ignored) {
                    }
                }
            }
            return null;
        });
    }
}
