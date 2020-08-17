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
import com.zetyun.streamtau.runtime.schema.RtSchemaParser;
import com.zetyun.streamtau.runtime.schema.RtSchemaRoot;
import com.zetyun.streamtau.streaming.exception.InvalidNodeId;
import com.zetyun.streamtau.streaming.exception.UnsupportedOperator;
import com.zetyun.streamtau.streaming.model.Dag;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.model.UnionOperator;
import com.zetyun.streamtau.streaming.runtime.mapper.SchemaStringfyFunction;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import com.zetyun.streamtau.streaming.transformer.node.UnionNodeId;
import lombok.Getter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class TransformContext {
    @Getter
    private final StreamExecutionEnvironment env;
    private final Dag dag;

    // Map of operator id to StreamNode
    private final Map<Object, StreamNode> nodeMap;
    // Map of schema id to RtSchemaRoot
    private final Map<String, RtSchemaRoot> schemaMap;

    public TransformContext(
        @Nonnull StreamExecutionEnvironment env,
        @Nonnull Dag dag
    ) {
        this.env = env;
        this.dag = dag;
        nodeMap = new HashMap<>();
        schemaMap = new HashMap<>();
    }

    public static void transform(@Nonnull StreamExecutionEnvironment env, @Nonnull Dag dag) {
        env.setParallelism(dag.getParallelism());
        TransformContext context = new TransformContext(env, dag);
        context.transformDag();
    }

    private void transformDag() {
        for (String sinkId : dag.getSinkIds()) {
            getStreamNode(sinkId);
        }
    }

    @Nonnull
    private StreamNode doTransform(Object nodeId, @Nonnull Operator operator) {
        Transformer transformer = TransformerFactory.INS.getTransformer(operator.getFid());
        if (transformer != null) {
            StreamNode node = transformer.transform(operator, this);
            node.setName(operator.getName());
            node.setSchemaId(operator.getSchemaId());
            nodeMap.put(nodeId, node);
            return node;
        }
        throw new UnsupportedOperator(operator);
    }

    @Nonnull
    private StreamNode transformNode(Object nodeId) {
        Operator operator;
        if (nodeId instanceof String) {
            operator = dag.getOperator((String) nodeId);
        } else if (nodeId instanceof UnionNodeId) {
            operator = new UnionOperator((UnionNodeId) nodeId);
        } else {
            throw new InvalidNodeId(nodeId);
        }
        return doTransform(nodeId, operator);
    }

    @Nonnull
    private RtSchemaRoot getSchema(@Nonnull String schemaId) {
        RtSchemaRoot schema = schemaMap.get(schemaId);
        if (schema == null) {
            SchemaSpec spec = dag.getSchema(schemaId);
            schema = new RtSchemaRoot(spec.createRtSchema());
            schemaMap.put(schemaId, schema);
        }
        return schema;
    }

    @Nonnull
    public StreamNode getStreamNode(@Nonnull Object nodeId) {
        StreamNode node = nodeMap.get(nodeId);
        if (node == null) {
            node = transformNode(nodeId);
            nodeMap.put(nodeId, node);
        }
        return node;
    }

    @Nullable
    public RtSchemaRoot getSchemaOf(@Nonnull Operator operator) {
        String schemaId = operator.getSchemaId();
        return schemaId != null ? getSchema(schemaId) : null;
    }

    @Nullable
    public RtSchemaRoot getSchemaOf(@Nonnull StreamNode node) {
        String schemaId = node.getSchemaId();
        return schemaId != null ? getSchema(node.getSchemaId()) : null;
    }

    public List<StreamNode> getUpstreamNodes(@Nonnull Operator operator) {
        List<String> dependencies = operator.getValidDependencies();
        return dependencies.stream()
            .map(this::getStreamNode)
            .collect(Collectors.toList());
    }

    @Nonnull
    public StreamNode getUnionizedUpstreamNode(@Nonnull Operator operator) {
        List<String> dependencies = operator.getValidDependencies();
        if (dependencies.size() == 1) {
            return getStreamNode(dependencies.get(0));
        }
        UnionNodeId unionNodeId = new UnionNodeId(dependencies);
        return getStreamNode(unionNodeId);
    }

    @Nullable
    public RtSchemaRoot getInputSchema(@Nonnull Operator operator) {
        return getSchemaOf(getUnionizedUpstreamNode(operator));
    }

    // TODO: if the new node should be registered to context
    public DataStream<RtEvent> toSingleValueStream(StreamNode node) {
        RtSchemaRoot schema = getSchemaOf(node);
        if (schema == null) {
            return node.asDataStream();
        }
        final RtSchemaParser parser = RtSchemaParser.createJsonEventParser(schema);
        return node.asDataStream()
            .map(new SchemaStringfyFunction(parser))
            .setParallelism(node.getParallelism());
    }
}
