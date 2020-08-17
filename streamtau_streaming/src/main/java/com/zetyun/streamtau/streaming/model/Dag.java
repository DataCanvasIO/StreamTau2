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

import com.zetyun.streamtau.core.schema.SchemaSpec;
import com.zetyun.streamtau.streaming.exception.MissingOperator;
import com.zetyun.streamtau.streaming.model.sink.Sink;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface Dag {
    Map<String, Operator> getOperators();

    SchemaSpec getSchema(String schemaId);

    int getParallelism();

    default Operator getOperator(String operatorId) {
        Map<String, Operator> operators = getOperators();
        if (operators.containsKey(operatorId)) {
            return operators.get(operatorId);
        }
        throw new MissingOperator(operatorId);
    }

    default Set<String> getSinkIds() {
        Set<String> sinkIds = new HashSet<>();
        for (Map.Entry<String, Operator> entry : getOperators().entrySet()) {
            Operator operator = entry.getValue();
            if (operator instanceof Sink) {
                sinkIds.add(entry.getKey());
            }
        }
        return sinkIds;
    }
}
