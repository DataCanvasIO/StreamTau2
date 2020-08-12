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

package com.zetyun.streamtau.streaming.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.core.schema.SchemaSpec;
import com.zetyun.streamtau.streaming.exception.MissingOperator;
import com.zetyun.streamtau.streaming.exception.MissingSchema;
import com.zetyun.streamtau.streaming.model.sink.Sink;
import lombok.Getter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Dag {
    @JsonProperty("operators")
    @Getter
    private Map<String, Operator> operators;
    @JsonProperty("schemas")
    @Getter
    private Map<String, SchemaSpec> schemas;

    @Override
    public String toString() {
        try {
            return PeaParser.YAML.stringShowAll(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @JsonIgnore
    public Set<String> getSinkIds() {
        Set<String> sinkIds = new HashSet<>();
        for (Map.Entry<String, Operator> entry : operators.entrySet()) {
            Operator operator = entry.getValue();
            if (operator instanceof Sink) {
                sinkIds.add(entry.getKey());
            }
        }
        return sinkIds;
    }

    @JsonIgnore
    public Operator getOperator(String operatorId) {
        if (!operators.containsKey(operatorId)) {
            throw new MissingOperator(operatorId);
        }
        return operators.get(operatorId);
    }

    @JsonIgnore
    public SchemaSpec getSchema(String schemaId) {
        if (!schemas.containsKey(schemaId)) {
            throw new MissingSchema(schemaId);
        }
        return schemas.get(schemaId);
    }
}
