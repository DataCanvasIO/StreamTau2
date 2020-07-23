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

package com.zetyun.streamtau.manager.pea.server;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.runtime.ScriptFormat;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@JsonTypeName("Executor")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Executor extends Server {
    @Override
    public void mapFrom(@NotNull Asset model) throws IOException {
    }

    @Override
    public void mapTo(@NotNull Asset model) throws IOException {
        model.setScriptFormat(ScriptFormat.TEXT_PLAIN);
        model.setScript("");
    }
}
