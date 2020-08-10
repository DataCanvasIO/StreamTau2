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

package com.zetyun.streamtau.streaming;

import com.zetyun.streamtau.streaming.model.Dag;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import org.apache.flink.client.FlinkPipelineTranslationUtil;
import org.apache.flink.client.program.StreamPlanEnvironment;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.jobgraph.JobGraph;

public class StreamBuilder {
    public static JobGraph buildJobGraph(Dag dag) {
        StreamPlanEnvironment env = new StreamPlanEnvironment(new Configuration(), null, 1);
        TransformContext.transform(env, dag);
        return FlinkPipelineTranslationUtil.getJobGraph(env.getPipeline(), new Configuration(), 1);
    }
}
