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

package com.zetyun.streamtau.manager.pea.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zetyun.streamtau.manager.pea.AssetPeaId;
import com.zetyun.streamtau.manager.pea.PeaType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CmdLineApp extends App {
    @JsonProperty("cmdLine")
    @Getter
    @Setter
    private AssetPeaId cmdLine;
    @JsonProperty("host")
    @Getter
    @Setter
    private AssetPeaId host;

    @Override
    public String getType() {
        return PeaType.CMD_LINE_APP.name();
    }
}
