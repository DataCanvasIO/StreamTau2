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

import com.zetyun.streamtau.manager.instance.server.ServerInstance;
import com.zetyun.streamtau.manager.pea.server.ServerStatus;

import java.io.IOException;
import java.util.Collection;

public interface ServerService {
    ServerInstance getInstance(Long projectId, String projectAssetId) throws IOException;

    Collection<ServerInstance> listAll(Long projectId) throws IOException;

    void start(Long projectId, String projectAssetId) throws IOException;

    void stop(Long projectId, String projectAssetId) throws IOException;

    ServerStatus getStatus(Long projectId, String projectAssetId) throws IOException;
}
