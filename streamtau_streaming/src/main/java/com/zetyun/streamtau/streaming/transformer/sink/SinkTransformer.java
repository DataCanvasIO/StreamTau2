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

package com.zetyun.streamtau.streaming.transformer.sink;

import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.transformer.Transformer;
import com.zetyun.streamtau.streaming.transformer.TransformerContext;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import lombok.RequiredArgsConstructor;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.function.Function;

@RequiredArgsConstructor
public class SinkTransformer implements Transformer {
    private final Function<Operator, SinkFunction<RtEvent>> sinkFunctionProvider;

    @Override
    public StreamNode transform(Operator operator, TransformerContext context) {
        StreamNode node = getUnionizedUpstreamNode(operator, context);
        return StreamNode.of(
            node.asStream()
                .addSink(sinkFunctionProvider.apply(operator))
                .setParallelism(operator.getParallelism())
        );
    }
}
