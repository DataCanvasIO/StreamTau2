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
import com.zetyun.streamtau.manager.pea.PeaParser;
import com.zetyun.streamtau.manager.pea.app.CmdLineApp;
import com.zetyun.streamtau.manager.pea.misc.CmdLine;
import com.zetyun.streamtau.manager.pea.plat.HostPlat;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CmdLineRunner implements Runner {
    private static final Logger logger = LoggerFactory.getLogger(CmdLineRunner.class);

    @Override
    public void run(Job job, Runnable onFinish) throws IOException {
        JobDefPod pod = PeaParser.JSON.parse(job.getJobDefinition(), JobDefPod.class);
        CmdLineApp cmdLineApp = (CmdLineApp) pod.getApp();
        CmdLine cmdLine = (CmdLine) pod.load(cmdLineApp.getCmdLine());
        HostPlat hostPlat = (HostPlat) pod.load(cmdLineApp.getHost());
        String host = hostPlat.getHostname();
        String cmd = cmdLine.getCmd();
        if (host.equals("localhost")) {
            logger.info("Command line job \"{}\" starts to run.", job.getJobName());
            Process process = Runtime.getRuntime().exec(cmd);
            IOUtils.copy(process.getInputStream(), System.out);
            onFinish.run();
            logger.info("Command line app \"{}\" finished.", job.getJobName());
        } else {
            throw new StreamTauException("10102");
        }
    }
}
