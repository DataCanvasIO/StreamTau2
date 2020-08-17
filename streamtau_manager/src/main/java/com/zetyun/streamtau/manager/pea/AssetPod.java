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

import com.zetyun.streamtau.core.pod.Pod;
import com.zetyun.streamtau.manager.db.mapper.AssetMapper;
import com.zetyun.streamtau.manager.db.model.Asset;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class AssetPod implements Pod<String, String, AssetPea> {
    private final AssetMapper assetMapper;
    private final Long projectId;

    @Nonnull
    public static AssetPea fromModel(@Nonnull Asset model) throws IOException {
        AssetPea pea = AssetPeaFactory.INS.make(model.getAssetType());
        pea.setId(model.getProjectAssetId());
        pea.setName(model.getAssetName());
        pea.setDescription(model.getAssetDescription());
        pea.mapFrom(model);
        return pea;
    }

    @Nonnull
    public static Asset toModel(@Nonnull AssetPea pea) throws IOException {
        Asset model = new Asset();
        model.setAssetType(pea.getType());
        model.setAssetName(pea.getName());
        model.setAssetDescription(pea.getDescription());
        model.setAssetCategory(pea.getCategory());
        model.setProjectAssetId(pea.getId());
        pea.mapTo(model);
        return model;
    }

    @Override
    public AssetPea load(@Nonnull String id) throws IOException {
        Asset model = assetMapper.findByIdInProject(projectId, id);
        return fromModel(model);
    }

    @Override
    public void save(@Nonnull AssetPea pea) throws IOException {
        Asset model = toModel(pea);
        assetMapper.insert(model);
    }
}
