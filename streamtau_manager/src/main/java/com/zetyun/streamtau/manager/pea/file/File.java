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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.ImmutableMap;
import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.db.model.AssetCategory;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.service.StorageService;
import com.zetyun.streamtau.manager.utils.ApplicationContextProvider;
import com.zetyun.streamtau.runtime.ScriptFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@EqualsAndHashCode(callSuper = true)
public abstract class File extends AssetPea {
    private static final Map<String, String> typeMap = ImmutableMap.<String, String>builder()
        .put("txt", "TxtFile")
        .put("jar", "JarFile")
        .build();

    @JsonView({PeaParser.Show.class})
    @JsonProperty("path")
    @Getter
    @Setter
    private String path;

    @Nullable
    public static String typeFromName(@Nonnull String name) {
        int index = name.lastIndexOf('.');
        if (index == -1) {
            return null;
        }
        return typeMap.get(name.substring(index + 1));
    }

    @Override
    public AssetCategory getCategory() {
        return AssetCategory.FILE;
    }

    @JsonIgnore
    public abstract String getExtension();

    @Override
    public void mapFrom(@Nonnull Asset model) {
        path = model.getScript();
    }

    @Override
    public void mapTo(@Nonnull Asset model) {
        model.setScriptFormat(ScriptFormat.TEXT_PLAIN);
        model.setScript(path);
    }

    @Override
    public void transferAnnex() {
        StorageService storageService = ApplicationContextProvider.getStorageService();
        int index = path.lastIndexOf('.');
        String ext = (index == -1) ? "" : path.substring(index + 1);
        try {
            String newPath = storageService.createFile(ext);
            storageService.copyFile(path, newPath);
            setPath(newPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
