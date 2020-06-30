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

import com.google.common.base.Charsets;
import com.zetyun.streamtau.manager.boot.WebAppInitializer;
import com.zetyun.streamtau.manager.db.model.Job;
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.app.CmdLineApp;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.pea.plat.HostPlat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Slf4j
public class CmdLineRunner implements Runner {
    @Override
    public void run(@NotNull Job job, Runnable onFinish) throws IOException {
        JobDefPod pod = JobDefPod.fromJobDefinition(job.getJobDefinition());
        CmdLineApp cmdLineApp = (CmdLineApp) pod.getApp();
        CmdLine cmdLine = (CmdLine) pod.load(cmdLineApp.getCmdLine());
        HostPlat hostPlat = (HostPlat) pod.load(cmdLineApp.getHost());
        if (!hostPlat.isLocalhost()) {
            throw new StreamTauException("10102", cmdLineApp.getType());
        }
        runCmdOnLocalhost(cmdLine.getCmd(), job.getJobName(), onFinish);
    }

    protected void runCmdOnLocalhost(String cmd, String jobName, Runnable onFinish) {
        if (log.isInfoEnabled()) {
            log.info("Job \"{}\" starts to run on localhost.", jobName);
            log.info("The command is: `{}`", cmd);
        }
        WebAppInitializer.executorService.execute(() -> {
            try {
                Process process = Runtime.getRuntime().exec(cmd);
                if (onFinish != null) {
                    onFinish.run();
                }
                if (log.isInfoEnabled()) {
                    String output = IOUtils.toString(process.getInputStream(), Charsets.UTF_8);
                    log.info("The console output is: \n{}", output);
                    log.info("Job \"{}\" finished.", jobName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
