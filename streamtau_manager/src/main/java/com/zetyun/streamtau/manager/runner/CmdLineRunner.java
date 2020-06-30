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
import com.zetyun.streamtau.manager.db.model.Job;
import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.app.CmdLineApp;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.pea.plat.HostPlat;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CmdLineRunner implements Runner {
    private static final Logger logger = LoggerFactory.getLogger(CmdLineRunner.class);

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

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
        if (logger.isInfoEnabled()) {
            logger.info("Job \"{}\" starts to run on localhost.", jobName);
            logger.info("The command is: `{}`", cmd);
        }
        executorService.execute(() -> {
            try {
                Process process = Runtime.getRuntime().exec(cmd);
                if (onFinish != null) {
                    onFinish.run();
                }
                if (logger.isInfoEnabled()) {
                    String output = IOUtils.toString(process.getInputStream(), Charsets.UTF_8);
                    logger.info("The console output is: \n{}", output);
                    logger.info("Job \"{}\" finished.", jobName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
