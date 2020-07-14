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

import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.instance.server.FlinkMiniClusterInstance;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.app.FlinkJarApp;
import com.zetyun.streamtau.manager.pea.file.JarFile;
import com.zetyun.streamtau.manager.service.StorageService;
import com.zetyun.streamtau.manager.utils.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.client.program.ProgramInvocationException;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Slf4j
public class FlinkJarRunner extends SingleServerRunner {
    public void run(@NotNull JobDefPod pod, FlinkMiniClusterInstance flinkMiniClusterInstance, Runnable onFinish) {
        FlinkJarApp flinkJarApp = (FlinkJarApp) pod.getApp();
        JarFile jarFile = (JarFile) pod.load(flinkJarApp.getJarFile());
        StorageService storageService = ApplicationContextProvider.getStorageService();
        String path = storageService.resolve(jarFile.getPath());
        try {
            PackagedProgram program = PackagedProgram.newBuilder()
                .setJarFile(new File(path))
                .build();
            JobGraph jobGraph = PackagedProgramUtils.createJobGraph(
                program,
                new Configuration(),
                1,
                false
            );
            flinkMiniClusterInstance.submitJobGraph(jobGraph);
        } catch (ProgramInvocationException e) {
            throw new StreamTauException("10301", flinkJarApp.getName());
        }
    }
}
