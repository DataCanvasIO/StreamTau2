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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonView;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.pea.app.CmdLineApp;
import com.zetyun.streamtau.manager.pea.generic.Pea;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.pea.plat.HostPlat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;

@JsonTypeInfo(property = "type", use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    // Misc
    @JsonSubTypes.Type(value = CmdLine.class, name = "CmdLine"),
    // plat
    @JsonSubTypes.Type(HostPlat.class),
    // app
    @JsonSubTypes.Type(CmdLineApp.class),
})
@ToString
@EqualsAndHashCode
public abstract class AssetPea implements Pea<String, String> {
    @JsonView({PeaParser.ShowId.class})
    @Getter
    @Setter
    private String id;
    @JsonView({PeaParser.Show.class})
    @Getter
    @Setter
    private String name;
    @JsonView({PeaParser.Show.class})
    @Getter
    @Setter
    private String description;

    @JsonView({PeaParser.Show.class})
    public String getType() {
        JsonTypeName name = getClass().getAnnotation(JsonTypeName.class);
        return name.value();
    }

    public abstract void mapFrom(Asset model) throws IOException;

    public abstract void mapTo(Asset model) throws IOException;
}
