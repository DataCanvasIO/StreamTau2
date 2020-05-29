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
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.PeaType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CmdLine extends AssetPea {
    @JsonProperty("cmd")
    @Getter
    @Setter
    private String cmd;

    @Override
    public String getType() {
        return PeaType.CMD_LINE.name();
    }

    @Override
    public void mapFrom(Asset model) {
        cmd = model.getScript();
    }

    @Override
    public void mapTo(Asset model) {
        model.setScript(cmd);
    }
}
