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

package com.zetyun.streamtau.core.pod;

import com.zetyun.streamtau.core.pea.Pea;
import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.runtime.ScriptFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.annotation.Nonnull;

@ToString
@RequiredArgsConstructor
public class FilePod<T, P extends Pea<String, T>> implements Pod<String, T, P> {
    @Getter
    private final String baseUrl;
    @Getter
    private final Class<P> clazz;

    @Override
    public P load(@Nonnull String id) throws IOException {
        try (InputStream inputStream = new URL(baseUrl + id).openStream()) {
            P pea = PeaParser.get(ScriptFormat.fromExtension(id)).parse(inputStream, clazz);
            pea.setId(id);
            return pea;
        }
    }

    @Override
    public void save(@Nonnull P pea) throws IOException {
        try (OutputStream os = new URL(baseUrl + pea.getId()).openConnection().getOutputStream()) {
            PeaParser.get(ScriptFormat.fromExtension(pea.getId())).writeAll(os, pea);
        }
    }
}
