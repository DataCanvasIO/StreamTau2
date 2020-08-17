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
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

@ToString
public class MapPod<I, T, P extends Pea<I, T, P>> implements Pod<I, T, P> {
    protected final Map<I, P> peaMap;

    public MapPod() {
        this(new HashMap<>());
    }

    public MapPod(Map<I, P> peaMap) {
        this.peaMap = peaMap;
    }

    @Override
    public P load(@Nonnull I id) {
        return peaMap.get(id);
    }

    @Override
    public I save(@Nonnull P pea) {
        peaMap.put(pea.getId(), pea);
        return pea.getId();
    }
}
