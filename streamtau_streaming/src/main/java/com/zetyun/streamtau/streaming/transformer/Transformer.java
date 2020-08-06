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
import com.zetyun.streamtau.streaming.exception.InvalidUsingOfOperator;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.List;
import java.util.stream.Collectors;

public interface Transformer {
    StreamNode transform(Operator operator, TransformerContext context);

    default List<StreamNode> getUpstreamNodes(Operator operator, TransformerContext context) {
        return context.getDag().dependenciesOf(operator).stream()
            .map(op -> TransformerFactory.get().transform(op, context))
            .collect(Collectors.toList());
    }

    default StreamNode getUnionizedUpstreamNode(Operator operator, TransformerContext context) {
        List<StreamNode> nodes = getUpstreamNodes(operator, context);
        if (nodes.size() == 0) {
            throw new InvalidUsingOfOperator(operator, "get upstream of an operator with no dependencies");
        }
        if (nodes.size() == 1) {
            return nodes.get(0);
        }
        DataStream<RtEvent> dataStream = null;
        for (StreamNode node : nodes) {
            if (dataStream == null) {
                dataStream = node.asStream();
            } else {
                dataStream = dataStream.union(node.asStream());
            }
        }
        return StreamNode.of(dataStream);
    }
}
