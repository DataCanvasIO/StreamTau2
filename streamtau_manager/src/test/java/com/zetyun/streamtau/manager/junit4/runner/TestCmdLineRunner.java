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

package com.zetyun.streamtau.manager.junit4.runner;

import com.zetyun.streamtau.manager.db.model.Job;
import com.zetyun.streamtau.manager.instance.server.ExecutorInstance;
import com.zetyun.streamtau.manager.runner.RunnerFactory;
import com.zetyun.streamtau.manager.service.ServerService;
import com.zetyun.streamtau.manager.utils.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.zetyun.streamtau.manager.helper.ResourceUtils.readJsonCompact;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    ApplicationContextProvider.class,
})
@Slf4j
public class TestCmdLineRunner {
    @MockBean
    private ServerService serverService;
    @MockBean
    private ExecutorInstance executorInstance;

    @BeforeClass
    public static void setupClass() {
    }

    @Before
    public void setup() {
    }

    @Test
    public void testRun() throws IOException {
        Job job = new Job();
        job.setAppType("CmdLineApp");
        job.setJobName("testSleep");
        job.setProjectId(2L);
        job.setJobDefinition(readJsonCompact("/jobdef/cmdline/cmd_sleep.json"));
        when(serverService.getInstance(2L, "EXECUTOR")).thenReturn(executorInstance);
        RunnerFactory.get().run(job, null);
        verify(serverService, times(1)).getInstance(2L, "EXECUTOR");
        verify(executorInstance, times(1)).cmdLine(any(String[].class), any(Runnable.class));
    }
}
