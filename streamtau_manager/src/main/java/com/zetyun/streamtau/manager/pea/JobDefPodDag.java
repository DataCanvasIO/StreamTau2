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

package com.zetyun.streamtau.manager.pea;

import com.zetyun.streamtau.core.schema.SchemaSpec;
import com.zetyun.streamtau.manager.pea.app.FlinkPipelineApp;
import com.zetyun.streamtau.manager.pea.misc.Pipeline;
import com.zetyun.streamtau.manager.pea.misc.SchemaPea;
import com.zetyun.streamtau.streaming.model.Dag;
import com.zetyun.streamtau.streaming.model.Operator;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

public class JobDefPodDag implements Dag {
    private final JobDefPod pod;
    private final FlinkPipelineApp app;
    @Getter
    private final Map<String, Operator> operators;
    @Getter
    private final int parallelism;

    private final Map<String, SchemaSpec> schemas = new HashMap<>();

    public JobDefPodDag(@Nonnull JobDefPod pod) {
        this.pod = pod;
        app = (FlinkPipelineApp) pod.load(pod.getAppId());
        Pipeline pipeline = (Pipeline) pod.load(app.getPipeline());
        this.operators = pipeline.getOperators();
        this.parallelism = app.getParallelism() != null ? app.getParallelism() : 1;
    }

    public String getAppName() {
        return app.getName();
    }

    @Override
    public SchemaSpec getSchema(String schemaId) {
        SchemaSpec schema = schemas.get(schemaId);
        if (schema == null) {
            schema = ((SchemaPea) pod.load(schemaId)).getSchema();
            schemas.put(schemaId, schema);
        }
        return schema;
    }
}
