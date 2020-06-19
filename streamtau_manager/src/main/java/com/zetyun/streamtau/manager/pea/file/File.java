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

package com.zetyun.streamtau.manager.pea.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.ImmutableMap;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.PeaParser;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;

public abstract class File extends AssetPea {
    private static final Map<String, String> typeMap = ImmutableMap.<String, String>builder()
        .put("txt", "TxtFile")
        .put("jar", "JarFile")
        .build();

    @JsonView({PeaParser.Show.class})
    @Getter
    @Setter
    private String uri;

    public static @Nullable String typeFromName(@NotNull String name) {
        int index = name.lastIndexOf('.');
        if (index == -1) {
            return null;
        }
        return typeMap.get(name.substring(index + 1));
    }

    @JsonIgnore
    public abstract String getExtension();

    @Override
    public void mapFrom(@NotNull Asset model) throws IOException {
        uri = model.getScript();
    }

    @Override
    public void mapTo(@NotNull Asset model) throws IOException {
        model.setScript(uri);
    }
}
