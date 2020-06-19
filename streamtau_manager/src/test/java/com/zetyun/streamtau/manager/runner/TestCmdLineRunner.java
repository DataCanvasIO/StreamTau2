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

package com.zetyun.streamtau.manager.runner;

import com.zetyun.streamtau.manager.db.model.Job;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static com.zetyun.streamtau.manager.helper.ResourceUtils.readJsonCompact;

public class TestCmdLineRunner {
    @BeforeClass
    public static void setupClass() {
    }

    @Test
    public void testRun() throws IOException {
        Job job = new Job();
        job.setAppType("CmdLineApp");
        job.setJobName("testSleep");
        job.setJobDefinition(readJsonCompact("/jobdef/cmdline/cmd_sleep.json"));
        RunnerFactory.run(job, null);
    }
}
