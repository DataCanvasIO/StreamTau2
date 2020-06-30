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

import com.zetyun.streamtau.manager.db.mapper.AssetMapper;
import com.zetyun.streamtau.manager.db.mapper.ProjectAssetMapper;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.db.model.ProjectAsset;
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.AssetPod;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {
    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private ProjectAssetMapper projectAssetMapper;
    @Autowired
    private ProjectService projectService;

    @Override
    public List<AssetPea> listAll(String userProjectId) throws IOException {
        Long projectId = projectService.mapProjectId(userProjectId);
        List<Asset> models = assetMapper.findAllOfProject(projectId);
        List<AssetPea> peas = new ArrayList<>(models.size());
        for (Asset model : models) {
            peas.add(AssetPod.fromModel(model));
        }
        return peas;
    }

    @Override
    public AssetPea findById(String userProjectId, String projectAssetId) throws IOException {
        Long projectId = projectService.mapProjectId(userProjectId);
        Asset model = assetMapper.findByIdInProject(projectId, projectAssetId);
        return AssetPod.fromModel(model);
    }

    @Override
    public AssetPea create(String userProjectId, AssetPea pea) throws IOException {
        Asset model = AssetPod.toModel(pea);
        assetMapper.insert(model);
        Long projectId = projectService.mapProjectId(userProjectId);
        ProjectAsset projectAsset = new ProjectAsset(projectId, model.getAssetId(), null);
        projectAssetMapper.addToProject(projectAsset);
        model.setProjectAssetId(projectAsset.getProjectAssetId());
        return AssetPod.fromModel(model);
    }

    @Override
    public AssetPea update(String userProjectId, AssetPea pea) throws IOException {
        Asset model = AssetPod.toModel(pea);
        Long projectId = projectService.mapProjectId(userProjectId);
        if (assetMapper.updateInProject(projectId, model) == 0) {
            throw new StreamTauException("10002", pea.getId());
        }
        return AssetPod.fromModel(model);
    }

    @Override
    public void delete(String userProjectId, String projectAssetId) {
        Long projectId = projectService.mapProjectId(userProjectId);
        ProjectAsset projectAsset = new ProjectAsset(projectId, null, projectAssetId);
        if (projectAssetMapper.deleteFromProject(projectAsset) == 0) {
            throw new StreamTauException("10002", projectAssetId);
        }
    }

    @Override
    public JobDefPod synthesizeJobDef(Long projectId, String projectAssetId) throws IOException {
        AssetPod assetPod = new AssetPod(assetMapper, projectId);
        JobDefPod jobDefPod = new JobDefPod(projectAssetId);
        assetPod.transfer(projectAssetId, jobDefPod);
        return jobDefPod;
    }
}
