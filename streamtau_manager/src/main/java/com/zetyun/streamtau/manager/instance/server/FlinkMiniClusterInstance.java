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

import com.zetyun.streamtau.manager.pea.server.FlinkMiniCluster;
import com.zetyun.streamtau.manager.pea.server.Server;
import com.zetyun.streamtau.manager.pea.server.ServerStatus;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.client.program.MiniClusterClient;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.ProgramInvocationException;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class FlinkMiniClusterInstance extends ServerInstance implements FlinkClusterInstance {
    private MiniCluster miniCluster = null;

    public FlinkMiniClusterInstance(Server server) {
        super(server);
    }

    @Override
    public void start() {
        if (status() == ServerStatus.ACTIVE) {
            return;
        }
        if (miniCluster == null) {
            Configuration configuration = new Configuration();
            Integer port = ((FlinkMiniCluster) getServer()).getRestPort();
            if (port == null) {
                port = 8081;
            }
            configuration.setInteger(RestOptions.PORT, port);
            MiniClusterConfiguration conf = new MiniClusterConfiguration.Builder()
                .setConfiguration(configuration)
                .setNumSlotsPerTaskManager(1024)
                .setNumTaskManagers(1)
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
        // The configuration seems to be no use.
        MiniClusterClient client = new MiniClusterClient(new Configuration(), miniCluster);
        client.shutDownCluster();
        checkAndSetStatus();
        if (log.isInfoEnabled()) {
            log.info("Flink mini cluster \"{}\" stopped.", getServer().getName());
        }
    }

    @Override
    public ServerStatus checkStatus() {
        return miniCluster != null && miniCluster.isRunning() ? ServerStatus.ACTIVE : ServerStatus.INACTIVE;
    }

    public void runPackagedProgram(@Nonnull String path) throws ProgramInvocationException {
        start();
        PackagedProgram program = PackagedProgram.newBuilder()
            .setJarFile(new File(path))
            .build();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(program.getUserCodeClassLoader());
            try {
                FlinkMiniClusterEnvironment.setAsContext(
                    miniCluster,
                    1,
                    Collections.singleton(path),
                    program.getClasspaths()
                );
                program.invokeInteractiveModeForExecution();
            } finally {
                FlinkMiniClusterEnvironment.unsetAsContext();
            }
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    @Override
    public FlinkMiniClusterEnvironment getExecutionEnv(
        int parallelism,
        @Nullable Collection<String> jarFiles,
        @Nullable Collection<URL> classPaths
    ) {
        start();
        return new FlinkMiniClusterEnvironment(miniCluster, parallelism, jarFiles, classPaths);
    }
}
