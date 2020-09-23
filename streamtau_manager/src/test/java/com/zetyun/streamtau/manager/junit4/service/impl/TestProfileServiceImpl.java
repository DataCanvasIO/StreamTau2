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

import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.service.ProfileService;
import com.zetyun.streamtau.manager.service.impl.ProfileServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    ProfileServiceImpl.class,
})
public class TestProfileServiceImpl {
    @Autowired
    private ProfileService profileService;

    @Test
    public void testGetProject() {
        String profile = profileService.get("Project");
        assertThat(profile, startsWith("{\"schema\":{\"type\":\"object\",\"properties\":{\""));
    }

    @Test
    public void testGetAssetPea() {
        String profile = profileService.get("CmdLine");
        assertThat(profile, startsWith("{\"schema\":{\"type\":\"object\",\"properties\":{\""));
    }

    @Test(expected = StreamTauException.class)
    public void testGetNonExist() {
        String profile = profileService.get("XXX");
    }
}
