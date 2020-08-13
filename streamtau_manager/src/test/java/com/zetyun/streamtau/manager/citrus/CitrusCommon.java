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
import com.zetyun.streamtau.manager.citrus.behavior.Assets;
import com.zetyun.streamtau.manager.pea.AssetPea;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import static com.zetyun.streamtau.core.pea.PeaUtils.replacePeaIds;

public final class CitrusCommon {
    public static final String COMMON_PREFIX = "COMMON_";
    public static final String SERVER_ID = "streamtau-manager";

    private CitrusCommon() {
    }

    @Nonnull
    public static String varRef(String varName) {
        return "${" + varName + "}";
    }

    public static void updateChildrenId(AssetPea pea) {
        replacePeaIds(pea, (String x) -> {
            if (x.startsWith(COMMON_PREFIX)) {
                return x;
            }
            return varRef(Assets.idVarName(x));
        });
    }

    @Nonnull
    public static List<AssetPea> getSortedAssetList(
        @Nonnull Map<String, AssetPea> peaMap
    ) {
        List<AssetPea> peaList = new LinkedList<>(peaMap.values());
        peaList.sort((o1, o2) -> {
            if (o1.reference(o2.getId())) {
                return 1;
            } else if (o2.reference(o1.getId())) {
                return -1;
            }
            return 0;
        });
        return peaList;
    }

    public static void createPeasInList(
        TestDesigner designer,
        @Nonnull List<AssetPea> peaList,
        String projectId
    ) {
        for (AssetPea pea : peaList) {
            if (pea.getId().startsWith(COMMON_PREFIX)) {
                continue;
            }
            updateChildrenId(pea);
            designer.applyBehavior(new Assets.Create(projectId, pea.getId(), pea));
        }
    }

    public static void deletePeasInList(
        TestDesigner designer,
        @Nonnull List<AssetPea> peaList,
        String projectId
    ) {
        for (AssetPea pea : peaList) {
            if (pea.getId().startsWith(COMMON_PREFIX)) {
                continue;
            }
            designer.applyBehavior(new Assets.Delete(projectId, pea.getId()));
        }
    }
}
