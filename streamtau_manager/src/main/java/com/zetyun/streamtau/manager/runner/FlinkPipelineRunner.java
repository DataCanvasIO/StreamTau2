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

import com.zetyun.streamtau.manager.instance.server.FlinkClusterInstance;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.JobDefPodDag;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import javax.annotation.Nonnull;

@Slf4j
public class FlinkPipelineRunner extends SingleServerRunner {
    public void run(
        @Nonnull JobDefPod pod,
        @Nonnull FlinkClusterInstance flinkClusterInstance,
        Runnable onFinish
    ) {
        JobDefPodDag dag = new JobDefPodDag(pod);
        StreamExecutionEnvironment env = flinkClusterInstance.getExecutionEnv(
            dag.getParallelism(),
            null,
            null
        );
        TransformContext.transform(env, dag);
        try {
            env.execute(dag.getAppName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
