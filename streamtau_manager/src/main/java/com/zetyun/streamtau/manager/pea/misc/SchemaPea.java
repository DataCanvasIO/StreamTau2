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

package com.zetyun.streamtau.manager.pea.misc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.core.schema.SchemaSpec;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.runtime.ScriptFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.IOException;
import javax.annotation.Nonnull;

@JsonTypeName("Schema")
@EqualsAndHashCode(callSuper = true)
public class SchemaPea extends AssetPea {
    @Schema(
        description = "Json schema specification.",
        example = "{\"type\": \"object\", \"properties\": {\"name\": {\"type\": \"string\"}}}"
    )
    @JsonProperty("schema")
    @Getter
    private SchemaSpec schema;

    @Override
    public void mapFrom(@Nonnull Asset model) throws IOException {
        schema = PeaParser.get(model.getScriptFormat()).parse(model.getScript(), SchemaSpec.class);
    }

    @Override
    public void mapTo(@Nonnull Asset model) throws IOException {
        model.setScriptFormat(ScriptFormat.APPLICATION_JSON);
        model.setScript(PeaParser.JSON.stringShowAll(schema));
    }
}
