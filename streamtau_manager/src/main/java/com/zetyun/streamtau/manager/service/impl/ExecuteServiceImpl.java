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

package com.zetyun.streamtau.manager.service.impl;

import com.google.common.base.Charsets;
import com.zetyun.streamtau.manager.service.ExecuteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;

@Service
@Slf4j
public class ExecuteServiceImpl implements ExecuteService {
    public static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void cmdLine(String[] cmd, Runnable onFinish) {
        if (log.isInfoEnabled()) {
            log.info("The command is: `{}`", String.join(" ", cmd));
        }
        executorService.execute(() -> {
            try {
                Process process = Runtime.getRuntime().exec(cmd);
                if (onFinish != null) {
                    onFinish.run();
                }
                if (log.isInfoEnabled()) {
                    String output = IOUtils.toString(process.getInputStream(), Charsets.UTF_8);
                    String error = IOUtils.toString(process.getErrorStream(), Charsets.UTF_8);
                    log.info("The console output is: \n{}", output);
                    log.info("The console error output is: \n{}", error);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @PreDestroy
    public void onExit() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        log.info("Executor service has been shut down.");
    }
}
