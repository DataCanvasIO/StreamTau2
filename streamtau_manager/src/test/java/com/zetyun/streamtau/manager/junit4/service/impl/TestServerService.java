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

package com.zetyun.streamtau.manager.junit4.service.impl;

import com.zetyun.streamtau.manager.db.mapper.AssetMapper;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.db.model.AssetCategory;
import com.zetyun.streamtau.manager.db.model.ScriptFormat;
import com.zetyun.streamtau.manager.instance.server.ExecutorInstance;
import com.zetyun.streamtau.manager.instance.server.ServerInstance;
import com.zetyun.streamtau.manager.pea.server.Executor;
import com.zetyun.streamtau.manager.pea.server.ServerStatus;
import com.zetyun.streamtau.manager.service.ProjectService;
import com.zetyun.streamtau.manager.service.ServerService;
import com.zetyun.streamtau.manager.service.impl.ServerServiceImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    ServerServiceImpl.class,
})
public class TestServerService {
    private static Asset asset;

    @Autowired
    private ServerService serverService;
    @MockBean
    private AssetMapper assetMapper;
    @MockBean
    private ProjectService projectService;

    @BeforeClass
    public static void setupClass() {
        asset = new Asset();
        asset.setAssetId(3L);
        asset.setAssetName("Exec Server");
        asset.setAssetType("Executor");
        asset.setProjectAssetId("ABC");
        asset.setScriptFormat(ScriptFormat.TEXT_PLAIN);
        asset.setScript("");
    }

    @Before
    public void setup() throws IOException {
        when(assetMapper.findByIdInProject(2L, "ABC")).thenReturn(asset);
        serverService.stop(2L, "ABC");
    }

    @Test
    public void testListAll() throws IOException {
        Executor executor = new Executor();
        executor.setId("ABC");
        executor.setName("Exec Server");
        executor.setStatus(ServerStatus.INACTIVE);
        ServerInstance serverInstance = new ExecutorInstance();
        serverInstance.setServer(executor);
        when(assetMapper.findByCategoryInProject(2L, AssetCategory.SERVER))
            .thenReturn(Collections.singletonList(asset));
        Collection<ServerInstance> serverInstances = serverService.listAll(2L);
        assertThat(serverInstances, hasItem(serverInstance));
        // 1 in stop, 1 in listAll.
        verify(assetMapper, times(1)).findByCategoryInProject(2L, AssetCategory.SERVER);
    }

    @Test
    public void testStart() throws IOException {
        serverService.start(2L, "ABC");
        assertThat(serverService.getStatus(2L, "ABC"), is(ServerStatus.ACTIVE));
    }
}
