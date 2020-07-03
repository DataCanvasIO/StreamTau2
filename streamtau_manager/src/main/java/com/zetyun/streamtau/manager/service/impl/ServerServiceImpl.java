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
import com.zetyun.streamtau.manager.service.ProjectService;
import com.zetyun.streamtau.manager.service.ServerService;
import com.zetyun.streamtau.manager.service.dto.ServerDto;
import com.zetyun.streamtau.manager.service.dto.ServerStatus;
import com.zetyun.streamtau.manager.service.mapper.ServerDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ServerServiceImpl implements ServerService {
    private final Map<Long, ServerDto> embeddedServers = new LinkedHashMap<>();

    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private ProjectService projectService;

    @Override
    public List<ServerDto> listAll(String userProjectId) throws IOException {
        Long projectId = projectService.mapProjectId(userProjectId);
        List<Asset> models = assetMapper.findByCategoryInProject(projectId, AssetCategory.SERVER);
        List<ServerDto> servers = new LinkedList<>();
        for (Asset model : models) {
            ServerDto server = ServerDtoMapper.MAPPER.toDto(model);
            Long serverId = model.getAssetId();
            if (embeddedServers.get(serverId) == null) {
                server.setStatus(ServerStatus.INACTIVE);
                embeddedServers.put(serverId, server);
            }
            servers.add(server);
        }
        return servers;
    }

    @Override
    public void start(String userProjectId, String projectAssetId) {
        // TODO
    }

    @Override
    public void stop(String userProjectId, String projectAssetId) {
        // TODO
    }
}
