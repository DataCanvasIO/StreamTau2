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

package com.zetyun.streamtau.manager.db.mapper;

import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.db.model.AssetCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AssetMapper {
    Asset findById(@Param("assetId") Long assetId);

    int insert(@Param("model") Asset model);

    int delete(@Param("assetId") Long assetId);

    List<Asset> findAllOfProject(@Param("projectId") Long projectId);

    List<Asset> findOfProjectByType(
        @Param("projectId") Long projectId,
        @Param("assetType") String assetType
    );

    Asset findByIdInProject(@Param("projectId") Long projectId, @Param("projectAssetId") String projectAssetId);

    List<Asset> findByTypeInProject(@Param("projectId") Long projectId, @Param("assetType") String assetType);

    List<Asset> findByCategoryInProject(
        @Param("projectId") Long projectId,
        @Param("assetCategory") AssetCategory assetCategory
    );

    int updateInProject(@Param("projectId") Long projectId, @Param("model") Asset model);
}
