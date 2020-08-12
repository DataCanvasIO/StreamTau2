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
import com.zetyun.streamtau.runtime.schema.RtSchemaRoot;
import com.zetyun.streamtau.streaming.exception.InvalidNodeId;
import com.zetyun.streamtau.streaming.model.Dag;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.model.UnionOperator;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import com.zetyun.streamtau.streaming.transformer.node.UnionNodeId;
import lombok.Getter;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TransformContext {
    @Getter
    private final StreamExecutionEnvironment env;
    @Getter
    private final Dag dag;

    // Map of operator id to StreamNode
    private final Map<Object, StreamNode> nodeMap;
    // Map of schema id to RtSchemaRoot
    private final Map<String, RtSchemaRoot> schemaMap;

    public TransformContext(StreamExecutionEnvironment env, Dag dag) {
        this.env = env;
        this.dag = dag;
        nodeMap = new HashMap<>(dag.getOperators().size() + 5);
        Map<String, SchemaSpec> schemas = dag.getSchemas();
        schemaMap = new HashMap<>(schemas == null ? 0 : schemas.size());
    }

    public static void transform(StreamExecutionEnvironment env, Dag dag) {
        TransformContext context = new TransformContext(env, dag);
        context.transformDag();
    }

    private StreamNode transformNode(Object nodeId) {
        if (nodeId instanceof String) {
            return TransformerFactory.get().transform(
                nodeId,
                dag.getOperator((String) nodeId),
                this
            );
        } else if (nodeId instanceof UnionNodeId) {
            return TransformerFactory.get().transform(
                nodeId,
                new UnionOperator((UnionNodeId) nodeId),
                this
            );
        }
        throw new InvalidNodeId(nodeId);
    }

    private RtSchemaRoot getSchema(@NotNull String schemaId) {
        RtSchemaRoot schema = schemaMap.get(schemaId);
        if (schema == null) {
            SchemaSpec spec = dag.getSchema(schemaId);
            schema = new RtSchemaRoot(spec.createRtSchema());
            schemaMap.put(schemaId, schema);
        }
        return schema;
    }

    public void registerStreamNode(Object nodeId, StreamNode node) {
        nodeMap.put(nodeId, node);
    }

    public StreamNode getStreamNode(@NotNull Object nodeId) {
        StreamNode node = nodeMap.get(nodeId);
        if (node == null) {
            node = transformNode(nodeId);
            nodeMap.put(nodeId, node);
        }
        return node;
    }

    public RtSchemaRoot getSchemaOf(@NotNull Operator operator) {
        String schemaId = operator.getSchemaId();
        return schemaId != null ? getSchema(schemaId) : null;
    }

    public RtSchemaRoot getSchemaOf(@NotNull StreamNode node) {
        String schemaId = node.getSchemaId();
        return schemaId != null ? getSchema(node.getSchemaId()) : null;
    }

    public List<StreamNode> getUpstreamNodes(Operator operator) {
        List<String> dependencies = operator.getValidDependencies();
        return dependencies.stream()
            .map(this::getStreamNode)
            .collect(Collectors.toList());
    }

    public StreamNode getUnionizedUpstreamNode(Operator operator) {
        List<String> dependencies = operator.getValidDependencies();
        if (dependencies.size() == 1) {
            return getStreamNode(dependencies.get(0));
        }
        UnionNodeId unionNodeId = new UnionNodeId(dependencies);
        return getStreamNode(unionNodeId);
    }

    public RtSchemaRoot getInputSchema(@NotNull Operator operator) {
        return getSchemaOf(getUnionizedUpstreamNode(operator));
    }

    private void transformDag() {
        for (String sinkId : dag.getSinkIds()) {
            getStreamNode(sinkId);
        }
    }
}
