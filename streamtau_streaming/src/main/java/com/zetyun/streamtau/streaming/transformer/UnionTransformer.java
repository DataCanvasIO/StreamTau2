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

import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.streaming.exception.UnionizeDifferentSchemas;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.List;

public class UnionTransformer implements Transformer {
    @Override
    public StreamNode transform(Operator operator, TransformContext context) {
        List<StreamNode> nodes = context.getUpstreamNodes(operator);
        String schemaId = null;
        DataStream<RtEvent> dataStream = null;
        StringBuilder name = new StringBuilder("Union of [");
        for (StreamNode node : nodes) {
            if (dataStream == null) {
                schemaId = node.getSchemaId();
                dataStream = node.asDataStream();
                name.append(node.getName());
            } else {
                String sid = node.getSchemaId();
                if (sid == null && schemaId != null || sid != null && !sid.equals(schemaId)) {
                    throw new UnionizeDifferentSchemas(operator);
                }
                dataStream = dataStream.union(node.asDataStream());
                name.append(", ").append(node.getName());
            }
        }
        operator.setName(name.toString());
        operator.setSchemaId(schemaId);
        return StreamNode.of(dataStream);
    }
}
