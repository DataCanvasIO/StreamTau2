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

import com.zetyun.streamtau.manager.instance.server.FlinkMiniClusterInstance;
import com.zetyun.streamtau.manager.pea.server.FlinkMiniCluster;
import com.zetyun.streamtau.manager.pea.server.ServerStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestFlinkMiniClusterInstance {
    private static FlinkMiniClusterInstance flinkMiniClusterInstance;

    @BeforeClass
    public static void setupClass() {
        FlinkMiniCluster flinkMiniCluster = new FlinkMiniCluster();
        flinkMiniCluster.setId("TEST");
        flinkMiniCluster.setName("FLINK_MINI_CLUSTER");
        flinkMiniClusterInstance = new FlinkMiniClusterInstance(flinkMiniCluster);
    }

    @Test
    public void testStartStop() {
        flinkMiniClusterInstance.start();
        assertThat(flinkMiniClusterInstance.status(), is(ServerStatus.ACTIVE));
        flinkMiniClusterInstance.stop();
        assertThat(flinkMiniClusterInstance.status(), is(ServerStatus.INACTIVE));
    }
}
