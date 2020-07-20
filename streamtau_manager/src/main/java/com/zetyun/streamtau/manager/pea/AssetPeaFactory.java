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

import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.manager.pea.generic.PeaFactory;

import java.util.Map;

public class AssetPeaFactory extends PeaFactory<String, String, AssetPea> {
    public static final AssetPeaFactory INS = new AssetPeaFactory();

    private AssetPeaFactory() {
        for (Map.Entry<String, Class<?>> entry : PeaParser.JSON.getSubtypeClasses(AssetPea.class).entrySet()) {
            register(entry.getKey(), () -> {
                try {
                    return (AssetPea) entry.getValue().newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    // Cannot get here.
                    return null;
                }
            });
        }
    }
}
