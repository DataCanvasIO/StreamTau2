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

package com.zetyun.streamtau.manager.pea;

import com.zetyun.streamtau.manager.helper.ResourceUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestJobDefPod {
    @Test
    public void testFromJobDefinition() throws IOException {
        JobDefPod pod = ResourceUtils.readJobDef("/jobdef/cmdline/cmd_ls.json");
        for (Map.Entry<String, AssetPea> entry : pod.getPeaMap().entrySet()) {
            assertThat(entry.getValue().getId(), is(entry.getKey()));
        }
    }
}
