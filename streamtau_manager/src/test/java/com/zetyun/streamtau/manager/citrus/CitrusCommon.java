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

import com.zetyun.streamtau.manager.citrus.behavior.Assets;
import com.zetyun.streamtau.manager.pea.AssetPea;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.zetyun.streamtau.manager.pea.generic.PeaUtils.replacePeaIds;

public class CitrusCommon {
    public static final String SERVER_ID = "streamtau-manager";

    public static String varRef(String varName) {
        return "${" + varName + "}";
    }

    public static void updateChildrenId(AssetPea pea) {
        replacePeaIds(pea, (String x) -> varRef(Assets.idVarName(x)));
    }

    public static List<AssetPea> getSortedAssetList(Map<String, AssetPea> peaMap) {
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
}
