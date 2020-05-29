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
import com.zetyun.streamtau.manager.db.model.ScriptFormat;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.PeaType;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.pea.plat.HostPlat;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.ProjectService;
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
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AssetServiceImpl.class})
public class TestAssetServiceImpl {
    @Autowired
    private AssetService assetService;

    @MockBean
    private AssetMapper assetMapper;
    @MockBean
    private ProjectAssetMapper projectAssetMapper;
    @MockBean
    private ProjectService projectService;

    @BeforeClass
    public static void setupClass() {
    }

    @Before
    public void setup() {
        Asset asset = new Asset();
        asset.setAssetId(1L);
        asset.setProjectAssetId("AAA");
        asset.setAssetType(PeaType.CMD_LINE.name());
        asset.setAssetName("1st");
        asset.setAssetDescription("The first asset.");
        asset.setScriptFormat(ScriptFormat.TEXT_PLAIN);
        asset.setScript("ls");
        when(assetMapper.findAllOfProject(anyLong())).thenReturn(Collections.singletonList(asset));
        when(assetMapper.insert(any(Asset.class))).then(args -> {
            Asset model = args.getArgument(0);
            model.setAssetId(2L);
            return 1;
        });
        when(projectAssetMapper.addToProject(any(ProjectAsset.class))).then(args -> {
            ProjectAsset model = args.getArgument(0);
            model.setProjectAssetId("BBB");
            return 1;
        });
        when(projectAssetMapper.deleteFromProject(any(ProjectAsset.class))).thenReturn(1);
        when(assetMapper.updateInProject(anyLong(), any(Asset.class))).thenReturn(1);
        when(projectService.mapProjectId(anyString())).thenReturn(1L);
    }

    @Test
    public void testListAll() throws IOException {
        List<AssetPea> peas = assetService.listAll("ABC");
        assertThat(peas.size(), is(1));
        CmdLine pea = new CmdLine();
        pea.setId("AAA");
        pea.setName("1st");
        pea.setDescription("The first asset.");
        pea.setCmd("ls");
        assertThat(peas, hasItem(is(pea)));
        verify(projectService, times(1)).mapProjectId("ABC");
        verify(assetMapper, times(1)).findAllOfProject(1L);
    }

    @Test
    public void testCreate() throws IOException {
        AssetPea pea = new HostPlat();
        pea.setName("forCreate");
        pea = assetService.create("ABC", pea);
        assertThat(pea.getId(), is("BBB"));
        assertThat(pea.getName(), is("forCreate"));
        assertThat(pea.getDescription(), nullValue());
        verify(projectService, times(1)).mapProjectId("ABC");
        verify(assetMapper, times(1)).insert(any(Asset.class));
        verify(projectAssetMapper, times(1)).addToProject(any(ProjectAsset.class));
    }

    @Test
    public void testUpdate() throws IOException {
        AssetPea pea = new CmdLine();
        pea.setId("AAA");
        pea.setName("forUpdate");
        pea = assetService.update("ABC", pea);
        assertThat(pea.getId(), is("AAA"));
        assertThat(pea.getName(), is("forUpdate"));
        assertThat(pea.getDescription(), nullValue());
        verify(projectService, times(1)).mapProjectId("ABC");
        verify(assetMapper, times(1)).updateInProject(eq(1L), any(Asset.class));
    }

    @Test
    public void testDelete() {
        assetService.delete("ABC", "AAA");
        verify(projectService, times(1)).mapProjectId("ABC");
        verify(projectAssetMapper, times(1)).deleteFromProject(any(ProjectAsset.class));
    }
}
