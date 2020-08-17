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

package com.zetyun.streamtau.streaming.run;

import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.core.pod.FilePod;
import com.zetyun.streamtau.core.schema.SchemaSpec;
import com.zetyun.streamtau.streaming.model.Dag;
import com.zetyun.streamtau.streaming.model.Operator;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DagFilePod extends FilePod<String, DagPea> implements Dag {
    @Getter
    private final Map<String, Operator> operators;
    private final Map<String, SchemaSpec> schemas = new HashMap<>();

    public DagFilePod(String baseUrl, String pipelineFile) {
        super(baseUrl, DagPea.class);
        try {
            PipelinePea pipeline = (PipelinePea) load(pipelineFile);
            operators = pipeline.getOperators();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Read file \"" + pipelineFile + "\" error!");
        }
    }

    public SchemaSpec getSchema(String schemaId) {
        SchemaSpec schema = schemas.get(schemaId);
        if (schema == null) {
            try {
                SchemaPea schemaPea = (SchemaPea) load(schemaId);
                schema = schemaPea.getSchema();
                schemas.put(schemaId, schema);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Read file \"" + schemaId + "\" error!");
            }
        }
        return schema;
    }

    public int getParallelism() {
        return 1;
    }

    @Override
    public String toString() {
        try {
            return PeaParser.YAML.stringShowAll(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
