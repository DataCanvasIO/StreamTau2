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
import com.zetyun.streamtau.streaming.exception.MissingDependency;
import com.zetyun.streamtau.streaming.model.sink.Sink;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Dag {
    @JsonProperty("operators")
    @Getter
    private Map<String, Operator> operators;

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
    public List<Operator> dependenciesOf(Operator operator) {
        List<String> dependencies = operator.getDependencies();
        List<Operator> ops = new ArrayList<>(dependencies.size());
        for (String dependency : dependencies) {
            Operator op = operators.get(dependency);
            if (op != null) {
                ops.add(op);
            } else {
                throw new MissingDependency(dependency);
            }
        }
        return ops;
    }

    @JsonIgnore
    public List<Sink> getSinks() {
        List<Sink> sinks = new LinkedList<>();
        for (Map.Entry<String, Operator> entry : operators.entrySet()) {
            Operator operator = entry.getValue();
            if (operator instanceof Sink) {
                sinks.add((Sink) operator);
            }
        }
        return sinks;
    }
}
