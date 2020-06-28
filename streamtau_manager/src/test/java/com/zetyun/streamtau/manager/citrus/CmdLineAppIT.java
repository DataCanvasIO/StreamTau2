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

package com.zetyun.streamtau.manager.citrus;

import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.design.TestDesigner;
import com.consol.citrus.dsl.junit.JUnit4CitrusTest;
import com.zetyun.streamtau.manager.citrus.behavior.Projects;
import com.zetyun.streamtau.manager.controller.protocol.ProjectRequest;
import org.junit.Test;

public class CmdLineAppIT extends JUnit4CitrusTest {
    private static final String SERVER_ID = "streamtau-manager";

    @Test
    @CitrusTest
    public void testRun(@CitrusResource TestDesigner designer) {
        designer.applyBehavior(new Projects.Create(
            new ProjectRequest("test", "for citrus", "CONTAINER")
        ));
    }
}
