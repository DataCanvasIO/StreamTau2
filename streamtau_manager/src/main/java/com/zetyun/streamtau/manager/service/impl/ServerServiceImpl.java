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
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.db.model.AssetCategory;
import com.zetyun.streamtau.manager.instance.server.ServerInstance;
import com.zetyun.streamtau.manager.instance.server.ServerInstanceFactory;
import com.zetyun.streamtau.manager.pea.AssetPod;
import com.zetyun.streamtau.manager.pea.server.Server;
import com.zetyun.streamtau.manager.pea.server.ServerStatus;
import com.zetyun.streamtau.manager.service.ProjectService;
import com.zetyun.streamtau.manager.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PreDestroy;

@Service
@Slf4j
public class ServerServiceImpl implements ServerService {
    private final Map<Long, ServerInstance> serverInstanceMap = new LinkedHashMap<>();

    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private ProjectService projectService;

    private void updateServerInstanceMap(@NotNull Asset model) throws IOException {
        Long serverId = model.getAssetId();
        serverInstanceMap.putIfAbsent(
            serverId,
            ServerInstanceFactory.get().getServerInstance((Server) AssetPod.fromModel(model))
        );
    }

    @Override
    public ServerInstance getInstance(Long projectId, String projectAssetId) throws IOException {
        Asset model = assetMapper.findByIdInProject(projectId, projectAssetId);
        if (model != null) {
            updateServerInstanceMap(model);
            return serverInstanceMap.get(model.getAssetId());
        }
        return null;
    }

    @Override
    public Collection<ServerInstance> listAll(Long projectId) throws IOException {
        List<Asset> models = assetMapper.findByCategoryInProject(projectId, AssetCategory.SERVER);
        for (Asset model : models) {
            updateServerInstanceMap(model);
        }
        return serverInstanceMap.values();
    }

    @Override
    public void start(Long projectId, String projectAssetId) throws IOException {
        ServerInstance serverInstance = getInstance(projectId, projectAssetId);
        if (log.isInfoEnabled()) {
            log.info("Start server \"{}\"...", serverInstance.getServer().getName());
        }
        serverInstance.start();
    }

    @Override
    public void stop(Long projectId, String projectAssetId) throws IOException {
        ServerInstance serverInstance = getInstance(projectId, projectAssetId);
        serverInstance.stop();
        if (log.isInfoEnabled()) {
            log.info("Stopped server \"{}\"...", serverInstance.getServer().getName());
        }
    }

    @Override
    public ServerStatus getStatus(Long projectId, String projectAssetId) throws IOException {
        ServerInstance serverInstance = getInstance(projectId, projectAssetId);
        return serverInstance.getServer().getStatus();
    }

    @PreDestroy
    public void onExit() {
        for (ServerInstance instance : serverInstanceMap.values()) {
            instance.stop();
        }
    }
}
