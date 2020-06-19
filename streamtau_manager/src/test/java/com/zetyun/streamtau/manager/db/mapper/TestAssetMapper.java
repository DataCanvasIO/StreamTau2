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
import com.zetyun.streamtau.manager.db.model.ScriptFormat;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.zetyun.streamtau.manager.helper.ResourceUtils.readObjectFromCsv;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestAssetMapper {
    private static List<Asset> assets;

    @Autowired
    private AssetMapper assetMapper;

    @BeforeClass
    public static void setupClass() throws IOException {
        assets = readObjectFromCsv("/db/data/asset.csv", Asset.class);
    }

    @Test
    public void testFindById() {
        Asset model = assetMapper.findById(2L);
        assertThat(model, is(assets.get(1)));
    }

    @Test
    public void testInsert() {
        Asset model = new Asset();
        model.setAssetName("New Asset");
        model.setAssetType("COMMAND_LINE");
        model.setScriptFormat(ScriptFormat.TEXT_PLAIN);
        assertThat(assetMapper.insert(model), is(1));
        assertThat(model.getAssetId(), notNullValue());
    }

    @Test
    public void testDelete() {
        assertThat(assetMapper.delete(3L), is(1));
        assertThat(assetMapper.findById(3L), nullValue());
    }

    @Test
    public void testFindAllOfProject() {
        List<Asset> models = assetMapper.findAllOfProject(2L);
        assertThat(models.size(), is(4));
        assertThat(models, hasItems(assets.get(0), assets.get(2), assets.get(3), assets.get(5)));
    }

    @Test
    public void testFindByIdInProject() {
        Asset model = assetMapper.findByIdInProject(2L, "73c3ad25-8de8-4c63-91dd-ac9c69a0243d");
        assertThat(model, is(assets.get(3)));
    }

    @Test
    public void testFindByIdInProjectNotExist() {
        Asset model = assetMapper.findByIdInProject(2L, "26fe1daf-0263-4aee-909b-909b497c7fda");
        assertThat(model, nullValue());
    }

    @Test
    public void testUpdateForProject() {
        Asset model = new Asset();
        model.setProjectAssetId("73c3ad25-8de8-4c63-91dd-ac9c69a0243d");
        model.setAssetName("Updated Asset.");
        assertThat(assetMapper.updateInProject(2L, model), is(1));
        model = assetMapper.findById(4L);
        assertThat(model.getAssetName(), is("Updated Asset."));
    }

    @Test
    public void testUpdateForProjectNotOwn() {
        Asset model = new Asset();
        model.setProjectAssetId("26fe1daf-0263-4aee-909b-909b497c7fda");
        model.setAssetName("Updated Asset.");
        assertThat(assetMapper.updateInProject(2L, model), is(0));
    }

    @Configuration
    @EnableAutoConfiguration
    static class Config {
    }
}
