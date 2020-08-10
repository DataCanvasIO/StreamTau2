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
import com.zetyun.streamtau.streaming.exception.MissingDependency;
import com.zetyun.streamtau.streaming.exception.OperatorHasNoDependency;
import com.zetyun.streamtau.streaming.model.sink.Sink;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Operator> dependenciesOf(Operator operator) {
        List<String> dependencies = operator.getDependencies();
        if (dependencies == null || dependencies.isEmpty()) {
            throw new OperatorHasNoDependency(operator);
        }
        Map<String, Operator> operators = new HashMap<>(dependencies.size());
        for (String dependency : dependencies) {
            Operator op = this.operators.get(dependency);
            if (op != null) {
                operators.put(dependency, op);
            } else {
                throw new MissingDependency(dependency);
            }
        }
        return operators;
    }

    @JsonIgnore
    public Map<String, Sink> getSinks() {
        Map<String, Sink> sinks = new HashMap<>();
        for (Map.Entry<String, Operator> entry : operators.entrySet()) {
            Operator operator = entry.getValue();
            if (operator instanceof Sink) {
                sinks.put(entry.getKey(), (Sink) operator);
            }
        }
        return sinks;
    }

    @JsonIgnore
    public SchemaSpec schemaOf(Operator operator) {
        String schemaId = operator.getSchemaId();
        if (schemaId != null) {
            return schemas.get(operator.getSchemaId());
        }
        return null;
    }
}
