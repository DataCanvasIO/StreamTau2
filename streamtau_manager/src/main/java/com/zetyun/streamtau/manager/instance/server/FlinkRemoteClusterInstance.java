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

import com.zetyun.streamtau.manager.pea.server.FlinkRemoteCluster;
import com.zetyun.streamtau.manager.pea.server.Server;
import com.zetyun.streamtau.manager.pea.server.ServerStatus;
import com.zetyun.streamtau.manager.properties.StreamingProperties;
import com.zetyun.streamtau.manager.utils.ApplicationContextProvider;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.ProgramInvocationException;
import org.apache.flink.client.program.StreamContextEnvironment;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.configuration.PipelineOptions;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.core.execution.DefaultExecutorServiceLoader;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class FlinkRemoteClusterInstance extends ServerInstance implements FlinkClusterInstance {
    public FlinkRemoteClusterInstance(Server server) {
        super(server);
        FlinkRemoteCluster cluster = (FlinkRemoteCluster) getServer();
        if (cluster.getPort() == null) {
            cluster.setPort(8081);
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public ServerStatus checkStatus() {
        // TODO: query status of cluster
        return ServerStatus.ACTIVE;
    }

    public void runPackagedProgram(@Nonnull String path) throws ProgramInvocationException {
        FlinkRemoteCluster cluster = (FlinkRemoteCluster) getServer();
        Configuration config = new Configuration();
        config.set(DeploymentOptions.TARGET, "remote");
        config.set(JobManagerOptions.ADDRESS, cluster.getHost());
        config.set(JobManagerOptions.PORT, cluster.getPort());
        config.set(RestOptions.PORT, cluster.getPort());
        try {
            config.set(
                PipelineOptions.JARS,
                Collections.singletonList(new File(path).toURI().toURL().toString())
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        PackagedProgram program = PackagedProgram.newBuilder()
            .setJarFile(new File(path))
            .build();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(program.getUserCodeClassLoader());
            try {
                StreamContextEnvironment.setAsContext(
                    new DefaultExecutorServiceLoader(),
                    config,
                    program.getUserCodeClassLoader(),
                    false,
                    false
                );
                program.invokeInteractiveModeForExecution();
            } finally {
                StreamContextEnvironment.unsetAsContext();
            }
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    @Override
    public StreamExecutionEnvironment getExecutionEnv(
        int parallelism,
        @Nullable Collection<String> jarFiles,
        @Nullable Collection<URL> classPaths
    ) {
        FlinkRemoteCluster cluster = (FlinkRemoteCluster) getServer();
        String host = cluster.getHost();
        Integer port = cluster.getPort();
        return StreamExecutionEnvironment.createRemoteEnvironment(
            host,
            port,
            parallelism,
            addRuntimeJars(jarFiles)
        );
    }

    @Nullable
    private String[] addRuntimeJars(@Nullable Collection<String> jarFiles) {
        StreamingProperties properties = ApplicationContextProvider.getStreamingProperties();
        String libPath = properties.getStreamingRuntimeLibPath();
        File dir = new File(libPath);
        String[] files = dir.list((d, name) -> name.endsWith(".jar"));
        int size = 0;
        size += (files != null ? files.length : 0);
        size += (jarFiles != null) ? jarFiles.size() : 0;
        if (size == 0) {
            return null;
        }
        String[] jars = new String[size];
        int i = 0;
        if (files != null) {
            for (; i < files.length; i++) {
                jars[i] = Paths.get(libPath, files[i]).toString();
                if (log.isInfoEnabled()) {
                    log.info("Added flink run time jar file \"{}\".", jars[i]);
                }
            }
        }
        if (jarFiles != null) {
            for (String f : jarFiles) {
                jars[i++] = f;
                if (log.isInfoEnabled()) {
                    log.info("Added flink run time jar file \"{}\".", f);
                }
            }
        }
        return jars;
    }
}
