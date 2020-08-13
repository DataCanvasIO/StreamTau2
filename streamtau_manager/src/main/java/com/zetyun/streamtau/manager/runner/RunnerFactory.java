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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Slf4j
public class RunnerFactory {
    private static RunnerFactory INS;

    private final Map<String, Runner> runnerMap;

    private RunnerFactory() {
        runnerMap = new LinkedHashMap<>(10);
        registerRunner("CmdLineApp", new CmdLineRunner());
        registerRunner("JavaJarApp", new JavaJarRunner());
        registerRunner("FlinkJarApp", new FlinkJarRunner());
    }

    public static RunnerFactory get() {
        if (INS == null) {
            INS = new RunnerFactory();
        }
        return INS;
    }

    private void registerRunner(String type, Runner runner) {
        runnerMap.put(type, runner);
    }

    public void run(@Nonnull Job job, @Nullable Runnable onFinish) throws IOException {
        Runner runner = runnerMap.get(job.getAppType());
        if (runner == null) {
            throw new StreamTauException("10101", job.getAppType());
        }
        runner.run(job, onFinish);
    }
}
