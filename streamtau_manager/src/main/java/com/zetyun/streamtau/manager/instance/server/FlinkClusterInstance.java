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

package com.zetyun.streamtau.manager.instance.server;

import org.apache.flink.client.program.ProgramInvocationException;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.net.URL;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface FlinkClusterInstance {
    StreamExecutionEnvironment getExecutionEnv(
        int parallelism,
        @Nullable Collection<String> jarFiles,
        @Nullable Collection<URL> classPaths
    );

    void runPackagedProgram(@Nonnull String path) throws ProgramInvocationException;
}
