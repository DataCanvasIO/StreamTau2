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

package com.zetyun.streamtau.manager.pea.plat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.pea.AssetPea;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@JsonTypeName("HostPlat")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HostPlat extends AssetPea {
    @Schema(
        description = "The hostname or IP of the host.",
        example = "localhost"
    )
    @JsonProperty("hostname")
    @Getter
    @Setter
    private String hostname;

    @Override
    public void mapFrom(@NotNull Asset model) {
        hostname = model.getScript();
    }

    @Override
    public void mapTo(@NotNull Asset model) {
        model.setScript(hostname);
    }
}
