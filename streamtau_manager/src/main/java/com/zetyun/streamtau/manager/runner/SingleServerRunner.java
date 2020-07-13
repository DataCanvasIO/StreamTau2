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
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.app.CmdLineApp;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.pea.server.Server;

import java.io.IOException;

public class SingleServerRunner implements Runner {
    @Override
    public void run(Job job, Runnable onFinish) throws IOException {
        JobDefPod pod = JobDefPod.fromJobDefinition(job.getJobDefinition());
        CmdLineApp cmdLineApp = (CmdLineApp) pod.getApp();
        CmdLine cmdLine = (CmdLine) pod.load(cmdLineApp.getCmdLine());
        Server server = (Server) pod.load(cmdLineApp.getServer());
        Class<?> clazz = this.getClass();
    }
}
