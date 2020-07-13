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

package com.zetyun.streamtau.manager.instance.server;

import com.zetyun.streamtau.manager.pea.server.ServerStatus;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.CoreOptions;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class FlinkMiniClusterInstance extends ServerInstance {
    private MiniCluster miniCluster = null;

    @Override
    public void start() {
        if (miniCluster == null) {
            Configuration configuration = new Configuration();
            configuration.setInteger(RestOptions.PORT, 0);
            configuration.setString(CoreOptions.CLASSLOADER_RESOLVE_ORDER, "parent-first");
            MiniClusterConfiguration conf = new MiniClusterConfiguration.Builder()
                .setConfiguration(configuration)
                .setNumSlotsPerTaskManager(5000)
                .build();
            miniCluster = new MiniCluster(conf);
        }
        try {
            miniCluster.start();
            checkAndSetStatus();
            if (log.isInfoEnabled()) {
                log.info("Flink mini cluster \"{}\" started.", getServer().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (miniCluster == null) {
            return;
        }
        try {
            miniCluster.close();
            checkAndSetStatus();
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.info("Flink mini cluster \"{}\" stopped.", getServer().getName());
            }
            e.printStackTrace();
        }
    }

    @Override
    public ServerStatus checkStatus() {
        return miniCluster != null && miniCluster.isRunning() ? ServerStatus.ACTIVE : ServerStatus.INACTIVE;
    }
}
