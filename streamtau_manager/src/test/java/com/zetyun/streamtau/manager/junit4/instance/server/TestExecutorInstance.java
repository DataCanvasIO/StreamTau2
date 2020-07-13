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

package com.zetyun.streamtau.manager.junit4.instance.server;

import com.zetyun.streamtau.manager.instance.server.ExecutorInstance;
import com.zetyun.streamtau.manager.pea.server.Executor;
import com.zetyun.streamtau.manager.pea.server.ServerStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestExecutorInstance {
    private static ExecutorInstance executorInstance;

    @BeforeClass
    public static void setupClass() {
        Executor executor = new Executor();
        executor.setId("TEST");
        executor.setName("EXECUTOR");
        executorInstance = new ExecutorInstance(executor);
    }

    @Test
    public void testStartStop() {
        executorInstance.start();
        assertThat(executorInstance.status(), is(ServerStatus.ACTIVE));
        executorInstance.stop();
        assertThat(executorInstance.status(), is(ServerStatus.INACTIVE));
    }
}
