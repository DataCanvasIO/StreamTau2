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

import com.zetyun.streamtau.manager.boot.ApplicationContextProvider;
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.ProfileService;
import com.zetyun.streamtau.manager.service.dto.AssetTypeInfo;
import com.zetyun.streamtau.manager.service.dto.ElementProfile;
import com.zetyun.streamtau.manager.service.impl.ProfileServiceImpl;
import org.hamcrest.CustomMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    ProfileServiceImpl.class,
    ApplicationContextProvider.class,
})
public class TestProfileServiceImpl {
    @Autowired
    private ProfileService profileService;
    @MockBean
    private AssetService assetService;

    @Test
    public void testGetProject() {
        ElementProfile profile = profileService.get("Project");
        assertThat(profile.getSchema().get("type").asText(), is("object"));
        assertThat(profile.getSchema().get("properties").get("name").get("type").asText(), is("string"));
    }

    @Test(expected = StreamTauException.class)
    public void testGetNonExist() {
        ElementProfile profile = profileService.get("XXX");
    }

    @Test
    public void testGetInProject() throws IOException {
        CmdLine cmdLine1 = new CmdLine();
        cmdLine1.setId("CmdLine1");
        cmdLine1.setName("The first cmdline");
        CmdLine cmdLine2 = new CmdLine();
        cmdLine2.setId("CmdLine2");
        cmdLine2.setName("The second cmdline");
        when(assetService.listByTypes(2L, new String[]{"CmdLine"}))
            .thenReturn(Arrays.asList(cmdLine1, cmdLine2));
        ElementProfile profile = profileService.getInProject(2L, "CmdLineApp");
        System.out.println(profile);
    }

    @Test(expected = StreamTauException.class)
    public void testGetInProjectNonExist() {
        ElementProfile profile = profileService.getInProject(2L, "XXX");
    }

    @Test
    public void testListAssetTypes() throws IOException {
        List<AssetTypeInfo> types = profileService.listAssetTypes();
        assertThat(types, hasItem(
            new CustomMatcher<AssetTypeInfo>("Match \"CmdLineApp\" asset type.") {
                @Override
                public boolean matches(Object obj) {
                    return obj instanceof AssetTypeInfo && ((AssetTypeInfo) obj).getType().equals("CmdLineApp");
                }
            }
        ));
    }
}
