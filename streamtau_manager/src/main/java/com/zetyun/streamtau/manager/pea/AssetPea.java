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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonView;
import com.zetyun.streamtau.core.pea.Pea;
import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.db.model.AssetCategory;
import com.zetyun.streamtau.manager.pea.app.CmdLineApp;
import com.zetyun.streamtau.manager.pea.app.FlinkJarApp;
import com.zetyun.streamtau.manager.pea.app.FlinkPipelineApp;
import com.zetyun.streamtau.manager.pea.app.JavaJarApp;
import com.zetyun.streamtau.manager.pea.file.JarFile;
import com.zetyun.streamtau.manager.pea.file.TxtFile;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.pea.misc.Host;
import com.zetyun.streamtau.manager.pea.misc.Pipeline;
import com.zetyun.streamtau.manager.pea.misc.SchemaPea;
import com.zetyun.streamtau.manager.pea.server.Executor;
import com.zetyun.streamtau.manager.pea.server.FlinkMiniCluster;
import com.zetyun.streamtau.manager.pea.server.FlinkRemoteCluster;
import com.zetyun.streamtau.runtime.ScriptFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import javax.annotation.Nonnull;

@Schema(
    type = "object",
    subTypes = {
        CmdLineApp.class,
        JavaJarApp.class,
        FlinkJarApp.class,
        CmdLine.class,
        Host.class,
        Pipeline.class,
        SchemaPea.class,
        JarFile.class,
        TxtFile.class,
        Executor.class,
        FlinkMiniCluster.class,
    }
)
@JsonPropertyOrder(value = {"type", "id", "name", "category", "description"}, alphabetic = true)
@JsonTypeInfo(property = "type", use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    // App
    @JsonSubTypes.Type(CmdLineApp.class),
    @JsonSubTypes.Type(JavaJarApp.class),
    @JsonSubTypes.Type(FlinkJarApp.class),
    @JsonSubTypes.Type(FlinkPipelineApp.class),
    // Misc
    @JsonSubTypes.Type(CmdLine.class),
    @JsonSubTypes.Type(Host.class),
    @JsonSubTypes.Type(Pipeline.class),
    @JsonSubTypes.Type(SchemaPea.class),
    // File
    @JsonSubTypes.Type(JarFile.class),
    @JsonSubTypes.Type(TxtFile.class),
    // Server
    @JsonSubTypes.Type(Executor.class),
    @JsonSubTypes.Type(FlinkMiniCluster.class),
    @JsonSubTypes.Type(FlinkRemoteCluster.class),
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@EqualsAndHashCode
public abstract class AssetPea implements Pea<String, String, AssetPea> {
    @Schema(
        description = "The id of the asset in the project.",
        example = "EFF8318B-91BE-4325-9F1D-4EC192D43B82"
    )
    @JsonView({PeaParser.Public.class})
    @Getter
    @Setter
    @JsonProperty("id")
    private String id;
    @Schema(
        description = "The name of the asset.",
        example = "Some name",
        required = true
    )
    @JsonView({PeaParser.Show.class, PeaParser.Public.class})
    @Getter
    @Setter
    @JsonProperty(value = "name", required = true)
    private String name;
    @Schema(
        description = "The description of the asset.",
        example = "blah blah..."
    )
    @JsonView({PeaParser.Show.class, PeaParser.Public.class})
    @Getter
    @Setter
    @JsonProperty("description")
    private String description;

    @Schema(
        description = "The type of the asset, determines the other fields."
    )
    // Jackson serialize the type id before the other properties.
    // This must be ignored or there will be two `type` properties.
    @JsonIgnore
    @Override
    public String getType() {
        JsonTypeName name = getClass().getAnnotation(JsonTypeName.class);
        return name.value();
    }

    @JsonProperty("category")
    public AssetCategory getCategory() {
        return AssetCategory.MISCELLANEOUS;
    }

    public void mapFrom(@Nonnull Asset model) throws IOException {
        PeaParser parser = PeaParser.get(model.getScriptFormat());
        parser.parse(this, model.getScript());
    }

    public void mapTo(@Nonnull Asset model) throws IOException {
        model.setScriptFormat(ScriptFormat.APPLICATION_JSON);
        PeaParser parser = PeaParser.get(model.getScriptFormat());
        model.setScript(parser.stringHideSome(this));
    }

    @Override
    public String toString() {
        try {
            return PeaParser.YAML.stringShowAll(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
