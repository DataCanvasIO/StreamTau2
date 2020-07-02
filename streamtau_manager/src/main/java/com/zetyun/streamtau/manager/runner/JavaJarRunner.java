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
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.app.JavaJarApp;
import com.zetyun.streamtau.manager.pea.file.JarFile;
import com.zetyun.streamtau.manager.pea.plat.HostPlat;
import com.zetyun.streamtau.manager.service.ExecuteService;
import com.zetyun.streamtau.manager.service.StorageService;
import com.zetyun.streamtau.manager.utils.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Slf4j
public class JavaJarRunner implements Runner {
    @Override
    public void run(@NotNull Job job, Runnable onFinish) throws IOException {
        JobDefPod pod = JobDefPod.fromJobDefinition(job.getJobDefinition());
        JavaJarApp javaJarApp = (JavaJarApp) pod.getApp();
        JarFile jarFile = (JarFile) pod.load(javaJarApp.getJarFile());
        HostPlat hostPlat = (HostPlat) pod.load(javaJarApp.getHost());
        if (!hostPlat.isLocalhost()) {
            throw new StreamTauException("10102", javaJarApp.getType());
        }
        StorageService storageService = ApplicationContextProvider.getStorageService();
        String path = storageService.resolve(jarFile.getPath());
        ExecuteService executeService = ApplicationContextProvider.getExecuteService();
        executeService.cmdLine(new String[]{"java", "-jar", path}, onFinish);
    }
}
