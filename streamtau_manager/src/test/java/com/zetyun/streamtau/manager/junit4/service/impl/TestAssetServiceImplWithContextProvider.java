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
import com.zetyun.streamtau.manager.db.mapper.ProjectAssetMapper;
import com.zetyun.streamtau.manager.db.model.Asset;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.ProjectService;
import com.zetyun.streamtau.manager.service.StorageService;
import com.zetyun.streamtau.manager.service.impl.AssetServiceImpl;
import com.zetyun.streamtau.manager.utils.ApplicationContextProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.zetyun.streamtau.manager.helper.ResourceUtils.readJsonCompact;
import static com.zetyun.streamtau.manager.helper.ResourceUtils.readObjectFromCsv;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    AssetServiceImpl.class,
    ApplicationContextProvider.class,
})
public class TestAssetServiceImplWithContextProvider {
    private static CmdLine cmdLinePea;
    private static Asset cmdLineAsset;

    @Autowired
    private AssetService assetService;
    @MockBean
    private AssetMapper assetMapper;
    @MockBean
    private ProjectAssetMapper projectAssetMapper;
    @MockBean
    private ProjectService projectService;
    // Used implicitly in File pea.
    @MockBean
    private StorageService storageService;

    @Test
    public void testSynthesizeJobDefWithFile() throws IOException {
        when(storageService.createFile("jar")).thenReturn("JAVA_JAR_APP.jar");
        List<Asset> assets = readObjectFromCsv("/jobdef/java_jar/jar_app.csv", Asset.class);
        for (Asset asset : assets) {
            when(assetMapper.findByIdInProject(3L, asset.getProjectAssetId())).thenReturn(asset);
        }
        JobDefPod jobDefPod = assetService.synthesizeJobDef(3L, "APP");
        assertThat(jobDefPod.toJobDefinition(), is(readJsonCompact("/jobdef/java_jar/jar_app.json")));
    }
}
