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
import com.zetyun.streamtau.manager.db.model.AssetCategory;
import com.zetyun.streamtau.manager.db.model.ProjectAsset;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.pea.misc.Host;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.impl.AssetServiceImpl;
import com.zetyun.streamtau.runtime.ScriptFormat;
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

import static com.zetyun.streamtau.manager.helper.ResourceUtils.readJsonCompact;
import static com.zetyun.streamtau.manager.helper.ResourceUtils.readObjectFromCsv;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AssetServiceImpl.class})
public class TestAssetServiceImpl {
    private static CmdLine cmdLinePea;
    private static Asset cmdLineAsset;

    @Autowired
    private AssetService assetService;
    @MockBean
    private AssetMapper assetMapper;
    @MockBean
    private ProjectAssetMapper projectAssetMapper;

    @BeforeClass
    public static void setupClass() {
        cmdLinePea = new CmdLine();
        cmdLinePea.setId("AAA");
        cmdLinePea.setName("1st");
        cmdLinePea.setDescription("The first asset.");
        cmdLinePea.setCmd("ls");
        cmdLineAsset = new Asset();
        cmdLineAsset.setAssetId(1L);
        cmdLineAsset.setProjectAssetId("AAA");
        cmdLineAsset.setAssetType("CmdLine");
        cmdLineAsset.setAssetName("1st");
        cmdLineAsset.setAssetDescription("The first asset.");
        cmdLineAsset.setScriptFormat(ScriptFormat.TEXT_PLAIN);
        cmdLineAsset.setScript("ls");
    }

    @Test
    public void testListAll() throws IOException {
        when(assetMapper.findInProject(2L)).thenReturn(Collections.singletonList(cmdLineAsset));
        List<AssetPea> peas = assetService.listAll(2L);
        assertThat(peas.size(), is(1));
        assertThat(peas, hasItem(is(cmdLinePea)));
        verify(assetMapper, times(1)).findInProject(2L);
    }

    @Test
    public void testListByType() throws IOException {
        when(assetMapper.findByTypeInProject(2L, "CmdLine"))
            .thenReturn(Collections.singletonList(cmdLineAsset));
        List<AssetPea> peas = assetService.listByType(2L, "CmdLine");
        assertThat(peas.size(), is(1));
        assertThat(peas, hasItem(is(cmdLinePea)));
        verify(assetMapper, times(1)).findByTypeInProject(2L, "CmdLine");
    }

    @Test
    public void testListByTypes() throws IOException {
        when(assetMapper.findByTypesInProject(eq(2L), any(String[].class)))
            .thenReturn(Collections.singletonList(cmdLineAsset));
        List<AssetPea> peas = assetService.listByTypes(2L, new String[]{});
        assertThat(peas.size(), is(1));
        assertThat(peas, hasItem(is(cmdLinePea)));
        verify(assetMapper, times(1)).findByTypesInProject(eq(2L), any(String[].class));
    }

    @Test
    public void testListByCategory() throws IOException {
        when(assetMapper.findByCategoryInProject(2L, AssetCategory.MISCELLANEOUS))
            .thenReturn(Collections.singletonList(cmdLineAsset));
        List<AssetPea> peas = assetService.listByCategory(2L, AssetCategory.MISCELLANEOUS);
        assertThat(peas, hasItem(cmdLinePea));
        verify(assetMapper, times(1))
            .findByCategoryInProject(2L, AssetCategory.MISCELLANEOUS);
    }

    @Test
    public void testGet() throws IOException {
        when(assetMapper.findByIdInProject(2L, "AAA")).thenReturn(cmdLineAsset);
        AssetPea pea = assetService.get(2L, "AAA");
        assertThat(pea, is(cmdLinePea));
        verify(assetMapper, times(1)).findByIdInProject(2L, "AAA");
    }

    @Test
    public void testCreate() throws IOException {
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
        AssetPea pea = new Host();
        pea.setName("forCreate");
        pea = assetService.create(2L, pea);
        assertThat(pea.getId(), is("BBB"));
        assertThat(pea.getName(), is("forCreate"));
        assertThat(pea.getDescription(), nullValue());
        verify(assetMapper, times(1)).insert(any(Asset.class));
        verify(projectAssetMapper, times(1)).addToProject(any(ProjectAsset.class));
    }

    @Test
    public void testUpdate() throws IOException {
        when(assetMapper.updateInProject(eq(2L), any(Asset.class))).thenReturn(1);
        AssetPea pea = new CmdLine();
        pea.setId("AAA");
        pea.setName("forUpdate");
        pea = assetService.update(2L, pea);
        assertThat(pea.getId(), is("AAA"));
        assertThat(pea.getName(), is("forUpdate"));
        assertThat(pea.getDescription(), nullValue());
        verify(assetMapper, times(1)).updateInProject(eq(2L), any(Asset.class));
    }

    @Test
    public void testDelete() {
        when(projectAssetMapper.deleteFromProject(any(ProjectAsset.class))).thenReturn(1);
        assetService.delete(2L, "AAA");
        verify(projectAssetMapper, times(1)).deleteFromProject(any(ProjectAsset.class));
    }

    @Test
    public void testSynthesizeJobDef() throws IOException {
        List<Asset> assets = readObjectFromCsv("/jobdef/cmd_line/cmd_ls.csv", Asset.class);
        for (Asset asset : assets) {
            when(assetMapper.findByIdInProject(2L, asset.getProjectAssetId())).thenReturn(asset);
        }
        JobDefPod jobDefPod = assetService.synthesizeJobDef(2L, "APP");
        assertThat(jobDefPod.toJobDefinition(), is(readJsonCompact("/jobdef/cmd_line/cmd_ls.json")));
    }
}
