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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonView;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.pea.app.CmdLineApp;
import com.zetyun.streamtau.manager.pea.generic.Pea;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.pea.plat.HostPlat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;

@Schema(
    type = "object",
    subTypes = {
        CmdLine.class,
        HostPlat.class,
        CmdLineApp.class,
    }
)
@JsonTypeInfo(property = "type", use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    // Misc
    @JsonSubTypes.Type(value = CmdLine.class, name = "CmdLine"),
    // plat
    @JsonSubTypes.Type(HostPlat.class),
    // app
    @JsonSubTypes.Type(CmdLineApp.class),
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString
@EqualsAndHashCode
public abstract class AssetPea implements Pea<String, String> {
    @Schema(
        description = "The id of the asset in the project.",
        example = "EFF8318B-91BE-4325-9F1D-4EC192D43B82"
    )
    @JsonView({PeaParser.ShowIdType.class})
    @Getter
    @Setter
    private String id;
    @Schema(description = "The name of the asset.", example = "Some name", required = true)
    @JsonView({PeaParser.Show.class, PeaParser.ShowIdType.class})
    @Getter
    @Setter
    private String name;
    @Schema(description = "The description of the asset.", example = "blah blah...")
    @JsonView({PeaParser.Show.class, PeaParser.ShowIdType.class})
    @Getter
    @Setter
    private String description;

    @Schema(description = "The type of the asset, determines the other fields.")
    @JsonView({PeaParser.ShowIdType.class})
    public String getType() {
        JsonTypeName name = getClass().getAnnotation(JsonTypeName.class);
        return name.value();
    }

    public abstract void mapFrom(Asset model) throws IOException;

    public abstract void mapTo(Asset model) throws IOException;
}
