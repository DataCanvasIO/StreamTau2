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

import com.zetyun.streamtau.manager.instance.server.ExecutorInstance;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.app.JavaJarApp;
import com.zetyun.streamtau.manager.pea.file.JarFile;
import com.zetyun.streamtau.manager.service.StorageService;
import com.zetyun.streamtau.manager.utils.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JavaJarRunner extends SingleServerRunner {
    public void run(JobDefPod pod, ExecutorInstance executorInstance, Runnable onFinish) throws IOException {
        JavaJarApp javaJarApp = (JavaJarApp) pod.getApp();
        JarFile jarFile = (JarFile) pod.load(javaJarApp.getJarFile());
        StorageService storageService = ApplicationContextProvider.getStorageService();
        String path = storageService.resolve(jarFile.getPath());
        executorInstance.cmdLine(new String[]{"java", "-jar", path}, onFinish);
    }
}
