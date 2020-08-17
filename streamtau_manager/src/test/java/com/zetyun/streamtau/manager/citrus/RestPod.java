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

package com.zetyun.streamtau.manager.citrus;

import com.consol.citrus.dsl.design.TestDesigner;
import com.zetyun.streamtau.core.pod.Pod;
import com.zetyun.streamtau.manager.citrus.behavior.Assets;
import com.zetyun.streamtau.manager.pea.AssetPea;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class RestPod implements Pod<String, String, AssetPea> {
    private final TestDesigner designer;
    private final String projectId;

    @Override
    public AssetPea load(@Nonnull String id) {
        throw new RuntimeException("RestPod can only do save for test.");
    }

    @Override
    public String save(@Nonnull AssetPea pea) {
        if (pea.getId().startsWith(CitrusCommon.COMMON_PREFIX)) {
            return pea.getId();
        }
        designer.applyBehavior(new Assets.Create(projectId, pea.getId(), pea));
        return CitrusCommon.varRef(Assets.idVarName(pea.getId()));
    }
}
