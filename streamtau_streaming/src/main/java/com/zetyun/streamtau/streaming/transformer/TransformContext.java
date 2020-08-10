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

package com.zetyun.streamtau.streaming.transformer;

import com.zetyun.streamtau.core.schema.SchemaSpec;
import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.streaming.exception.UnionizeDifferentSchemas;
import com.zetyun.streamtau.streaming.model.Dag;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.model.sink.Sink;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import lombok.Getter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TransformContext {
    @Getter
    private final StreamExecutionEnvironment env;
    @Getter
    private final Dag dag;

    // Map of operator id to StreamNode
    private final Map<String, StreamNode> operatorNodeMap;

    public TransformContext(StreamExecutionEnvironment env, Dag dag) {
        this.env = env;
        this.dag = dag;
        operatorNodeMap = new HashMap<>(dag.getOperators().size());
    }

    public static void transform(StreamExecutionEnvironment env, Dag dag) {
        TransformContext context = new TransformContext(env, dag);
        context.transformDag();
    }

    public void registerStreamNode(String operatorId, StreamNode node) {
        operatorNodeMap.put(operatorId, node);
    }

    public List<StreamNode> getUpstreamNodes(Operator operator) {
        Map<String, Operator> ops = dag.dependenciesOf(operator);
        List<StreamNode> nodes = new ArrayList<>(ops.size());
        for (Map.Entry<String, Operator> entry : ops.entrySet()) {
            String operatorId = entry.getKey();
            StreamNode node = operatorNodeMap.get(operatorId);
            if (node == null) {
                node = TransformerFactory.get().transform(operatorId, entry.getValue(), this);
            }
            nodes.add(node);
        }
        return nodes;
    }

    // TODO: if the generated node should be cached.
    public StreamNode getUnionizedUpstreamNode(Operator operator) {
        List<StreamNode> nodes = getUpstreamNodes(operator);
        if (nodes.size() == 1) {
            return nodes.get(0);
        }
        SchemaSpec schema = null;
        DataStream<RtEvent> dataStream = null;
        StringBuilder name = new StringBuilder("Union of [");
        for (StreamNode node : nodes) {
            if (dataStream == null) {
                schema = node.getSchema();
                dataStream = node.asStream();
                name.append(node.getName());
            } else {
                if (!node.getSchema().equals(schema)) {
                    throw new UnionizeDifferentSchemas(operator);
                }
                dataStream = dataStream.union(node.asStream());
                name.append(", ").append(node.getName());
            }
        }
        StreamNode node = StreamNode.of(dataStream);
        node.setName(name.toString());
        node.setSchema(schema);
        return node;
    }

    private void transformDag() {
        for (Map.Entry<String, Sink> entry : dag.getSinks().entrySet()) {
            TransformerFactory.get().transform(entry.getKey(), entry.getValue(), this);
        }
    }
}
