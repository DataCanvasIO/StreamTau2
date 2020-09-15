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

package com.zetyun.streamtau.manager.service;

import com.zetyun.streamtau.manager.db.model.AssetCategory;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.service.dto.AssetTypeInfo;

import java.io.IOException;
import java.util.List;

public interface AssetService {
    List<AssetPea> listAll(Long projectId) throws IOException;

    List<AssetPea> listByType(Long projectId, String type) throws IOException;

    AssetPea findById(Long projectId, String projectAssetId) throws IOException;

    List<AssetPea> findByType(Long projectId, String assetType) throws IOException;

    List<AssetPea> findByCategory(Long projectId, AssetCategory assetCategory) throws IOException;

    AssetPea create(Long projectId, AssetPea pea) throws IOException;

    AssetPea update(Long projectId, AssetPea pea) throws IOException;

    void delete(Long projectId, String projectAssetId);

    JobDefPod synthesizeJobDef(Long projectId, String projectAssetId) throws IOException;

    List<AssetTypeInfo> types() throws IOException;
}
