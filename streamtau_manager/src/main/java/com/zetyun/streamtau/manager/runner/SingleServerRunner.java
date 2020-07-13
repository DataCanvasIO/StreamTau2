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
import com.zetyun.streamtau.manager.instance.server.ServerInstance;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.app.SingleServerApp;
import com.zetyun.streamtau.manager.pea.server.Server;
import com.zetyun.streamtau.manager.service.ServerService;
import com.zetyun.streamtau.manager.utils.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class SingleServerRunner implements Runner {
    @Override
    public void run(Job job, Runnable onFinish) throws IOException {
        JobDefPod pod = JobDefPod.fromJobDefinition(job.getJobDefinition());
        SingleServerApp app = (SingleServerApp) pod.getApp();
        Server server = (Server) pod.load(app.getServer());
        ServerService serverService = ApplicationContextProvider.getServerService();
        ServerInstance serverInstance = serverService.getInstance(
            job.getProjectId(),
            server.getId()
        );
        try {
            Method method = this.getClass().getDeclaredMethod(
                "run",
                JobDefPod.class,
                serverInstance.getClass(),
                Runnable.class
            );
            method.setAccessible(true);
            if (log.isInfoEnabled()) {
                log.info("Job \"{}\" starts to run on server \"{}\".",
                    job.getJobName(), serverInstance.getServer().getName());
            }
            method.invoke(this, pod, serverInstance, (Runnable) () -> {
                if (onFinish != null) {
                    onFinish.run();
                }
                if (log.isInfoEnabled()) {
                    log.info("Job \"{}\" finished.", job.getJobName());
                }
            });
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new StreamTauException("10102", app.getType(), server.getType());
        }
    }
}
