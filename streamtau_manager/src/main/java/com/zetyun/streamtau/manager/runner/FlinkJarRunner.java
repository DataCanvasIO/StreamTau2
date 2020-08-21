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
import com.zetyun.streamtau.manager.instance.server.FlinkClusterInstance;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.app.FlinkJarApp;
import com.zetyun.streamtau.manager.pea.file.JarFile;
import com.zetyun.streamtau.manager.service.StorageService;
import com.zetyun.streamtau.manager.utils.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.client.program.ProgramInvocationException;

import javax.annotation.Nonnull;

@Slf4j
public class FlinkJarRunner extends SingleServerRunner {
    public void run(
        @Nonnull JobDefPod pod,
        @Nonnull FlinkClusterInstance flinkClusterInstance,
        Runnable onFinish
    ) {
        FlinkJarApp flinkJarApp = (FlinkJarApp) pod.getApp();
        JarFile jarFile = (JarFile) pod.load(flinkJarApp.getJarFile());
        StorageService storageService = ApplicationContextProvider.getStorageService();
        String path = storageService.resolve(jarFile.getPath());
        try {
            flinkClusterInstance.runPackagedProgram(path);
        } catch (ProgramInvocationException e) {
            throw new StreamTauException("10301", flinkJarApp.getName());
        }
    }
}
