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

package com.zetyun.streamtau.manager.service.impl;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.manager.db.mapper.AssetMapper;
import com.zetyun.streamtau.manager.db.mapper.ProjectAssetMapper;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.db.model.AssetCategory;
import com.zetyun.streamtau.manager.db.model.ProjectAsset;
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.AssetPeaFactory;
import com.zetyun.streamtau.manager.pea.AssetPod;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.dto.AssetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

@Service
public class AssetServiceImpl implements AssetService {
    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private ProjectAssetMapper projectAssetMapper;

    @Nonnull
    private List<AssetPea> getAssetPeas(@Nonnull List<Asset> models) throws IOException {
        List<AssetPea> peas = new ArrayList<>(models.size());
        for (Asset model : models) {
            peas.add(AssetPod.fromModel(model));
        }
        return peas;
    }

    @Override
    public List<AssetPea> listAll(Long projectId) throws IOException {
        List<Asset> models = assetMapper.findAllOfProject(projectId);
        return getAssetPeas(models);
    }

    @Override
    public List<AssetPea> listByType(Long projectId, String type) throws IOException {
        List<Asset> models = assetMapper.findOfProjectByType(projectId, type);
        return getAssetPeas(models);
    }

    @Override
    public AssetPea findById(Long projectId, String projectAssetId) throws IOException {
        Asset model = assetMapper.findByIdInProject(projectId, projectAssetId);
        return AssetPod.fromModel(model);
    }

    @Override
    public List<AssetPea> findByType(Long projectId, String assetType) throws IOException {
        List<Asset> models = assetMapper.findByTypeInProject(projectId, assetType);
        return getAssetPeas(models);
    }

    @Override
    public List<AssetPea> findByCategory(Long projectId, AssetCategory assetCategory) throws IOException {
        List<Asset> models = assetMapper.findByCategoryInProject(projectId, assetCategory);
        return getAssetPeas(models);
    }

    @Override
    public AssetPea create(Long projectId, AssetPea pea) throws IOException {
        Asset model = AssetPod.toModel(pea);
        assetMapper.insert(model);
        ProjectAsset projectAsset = new ProjectAsset(projectId, model.getAssetId(), null);
        projectAssetMapper.addToProject(projectAsset);
        model.setProjectAssetId(projectAsset.getProjectAssetId());
        return AssetPod.fromModel(model);
    }

    @Override
    public AssetPea update(Long projectId, AssetPea pea) throws IOException {
        Asset model = AssetPod.toModel(pea);
        if (assetMapper.updateInProject(projectId, model) == 0) {
            throw new StreamTauException("10002", pea.getId());
        }
        return AssetPod.fromModel(model);
    }

    @Override
    public void delete(Long projectId, String projectAssetId) {
        ProjectAsset projectAsset = new ProjectAsset(projectId, null, projectAssetId);
        if (projectAssetMapper.deleteFromProject(projectAsset) == 0) {
            throw new StreamTauException("10002", projectAssetId);
        }
    }

    @Override
    public JobDefPod synthesizeJobDef(Long projectId, String projectAssetId) {
        AssetPod assetPod = new AssetPod(assetMapper, projectId);
        JobDefPod jobDefPod = new JobDefPod(projectAssetId);
        assetPod.transfer(projectAssetId, jobDefPod);
        return jobDefPod;
    }

    @Override
    public List<AssetType> types() throws IOException {
        Map<String, Class<?>> classMap = PeaParser.getSubtypeClasses(AssetPea.class);
        List<AssetType> assetTypeList = new ArrayList<>(classMap.size());
        for (Map.Entry<String, Class<?>> entry : classMap.entrySet()) {
            Class<?> clazz = entry.getValue();
            JsonSchema schema = PeaParser.JSON.createJsonSchema(clazz);
            // Remove common properties to reduce size.
            Map<String, JsonSchema> props = schema.asObjectSchema().getProperties();
            props.remove("id");
            props.remove("name");
            props.remove("description");
            props.remove("type");
            props.remove("category");
            schema.asObjectSchema().setProperties(props);
            String type = entry.getKey();
            AssetType model = new AssetType();
            model.setType(type);
            model.setCategory(AssetPeaFactory.INS.make(type).getCategory());
            model.setSchema(schema);
            assetTypeList.add(model);
        }
        return assetTypeList;
    }
}
