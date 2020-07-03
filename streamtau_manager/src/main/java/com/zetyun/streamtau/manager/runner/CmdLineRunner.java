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
import com.zetyun.streamtau.manager.pea.app.CmdLineApp;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.pea.misc.Host;
import com.zetyun.streamtau.manager.service.ExecuteService;
import com.zetyun.streamtau.manager.utils.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Slf4j
public class CmdLineRunner implements Runner {
    @Override
    public void run(@NotNull Job job, Runnable onFinish) throws IOException {
        JobDefPod pod = JobDefPod.fromJobDefinition(job.getJobDefinition());
        CmdLineApp cmdLineApp = (CmdLineApp) pod.getApp();
        CmdLine cmdLine = (CmdLine) pod.load(cmdLineApp.getCmdLine());
        Host host = (Host) pod.load(cmdLineApp.getHost());
        if (!host.isLocalhost()) {
            throw new StreamTauException("10102", cmdLineApp.getType());
        }
        ExecuteService executeService = ApplicationContextProvider.getExecuteService();
        executeService.cmdLine(new String[]{"sh", "-c", cmdLine.getCmd()}, onFinish);
    }
}
