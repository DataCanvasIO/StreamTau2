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

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.client.deployment.executors.LocalExecutor;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.minicluster.JobExecutor;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironmentFactory;
import org.apache.flink.streaming.api.graph.StreamGraph;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class FlinkMiniClusterEnvironment extends StreamExecutionEnvironment {
    private final JobExecutor jobExecutor;
    private final Collection<Path> jarFiles;
    private final Collection<URL> classPaths;

    public FlinkMiniClusterEnvironment(
        JobExecutor jobExecutor,
        int parallelism,
        Collection<Path> jarFiles,
        Collection<URL> classPaths
    ) {
        this.jobExecutor = jobExecutor;
        this.jarFiles = jarFiles;
        this.classPaths = classPaths;
        getConfiguration().set(DeploymentOptions.TARGET, LocalExecutor.NAME);
        getConfiguration().set(DeploymentOptions.ATTACHED, true);
        setParallelism(parallelism);
    }

    public static void setAsContext(
        final JobExecutor jobExecutor,
        int parallelism,
        final Collection<Path> jarFiles,
        final Collection<URL> classPaths) {
        StreamExecutionEnvironmentFactory factory = () -> new FlinkMiniClusterEnvironment(
            jobExecutor,
            parallelism,
            jarFiles,
            classPaths
        );
        initializeContextEnvironment(factory);
    }

    public static void unsetAsContext() {
        resetContextEnvironment();
    }

    @Override
    public JobExecutionResult execute(StreamGraph streamGraph) throws Exception {
        JobGraph jobGraph = streamGraph.getJobGraph();
        if (jarFiles != null) {
            jarFiles.forEach(jobGraph::addJar);
        }
        if (classPaths != null) {
            jobGraph.setClasspaths(new ArrayList<>(classPaths));
        }
        return jobExecutor.executeJobBlocking(jobGraph);
    }
}
