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

import com.zetyun.streamtau.manager.db.model.ProjectAsset;
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
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestProjectAssetMapper {
    private static List<ProjectAsset> projectAssets;

    @Autowired
    private ProjectAssetMapper projectAssetMapper;

    @BeforeClass
    public static void setupClass() throws IOException {
        projectAssets = readObjectFromCsv("/db/data/project_asset.csv", ProjectAsset.class);
    }

    @Test
    public void testFindAll() {
        List<ProjectAsset> modelList = projectAssetMapper.findAll();
        assertThat(modelList.size(), is(projectAssets.size()));
        for (ProjectAsset projectAsset : projectAssets) {
            assertThat(modelList, hasItem(projectAsset));
        }
    }

    @Test
    public void testAddToProject() {
        ProjectAsset model = new ProjectAsset(2L, 5L, null);
        assertThat(projectAssetMapper.addToProject(model), is(1));
        assertThat(model.getProjectAssetId(), notNullValue());
    }

    @Test
    public void testDeleteFromProject() {
        ProjectAsset model = new ProjectAsset(2L, null, "f4e3ddd6-c57c-4fcd-befc-ee7be4157bde");
        assertThat(projectAssetMapper.deleteFromProject(model), is(1));
    }

    @Test
    public void testDeleteFromProjectNotOwn() {
        ProjectAsset model = new ProjectAsset(1L, null, "f4e3ddd6-c57c-4fcd-befc-ee7be4157bde");
        assertThat(projectAssetMapper.deleteFromProject(model), is(0));
    }

    @Configuration
    @EnableAutoConfiguration
    static class Config {
    }
}
