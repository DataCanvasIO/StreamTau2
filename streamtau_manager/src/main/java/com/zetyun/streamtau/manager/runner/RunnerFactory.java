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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RunnerFactory {
    private static final Map<String, Runner> runnerMap = new HashMap<>();

    static {
        registerRunner("CmdLineApp", new CmdLineRunner());
        registerRunner("JavaJarApp", new JavaJarRunner());
    }

    private RunnerFactory() {
    }

    private static void registerRunner(String type, Runner runner) {
        runnerMap.put(type, runner);
    }

    public static void run(@NotNull Job job, Runnable onFinish) throws IOException {
        Runner runner = runnerMap.get(job.getAppType());
        if (runner == null) {
            throw new StreamTauException("10101", job.getAppType());
        }
        runner.run(job, onFinish);
    }
}
