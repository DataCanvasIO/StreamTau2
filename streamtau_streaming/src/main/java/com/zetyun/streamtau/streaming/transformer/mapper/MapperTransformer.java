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

package com.zetyun.streamtau.streaming.transformer.mapper;

import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import com.zetyun.streamtau.streaming.transformer.Transformer;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import lombok.RequiredArgsConstructor;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class MapperTransformer implements Transformer {
    private final MapFunctionProvider mapFunctionProvider;

    @Nonnull
    @Override
    public StreamNode transform(@Nonnull Operator operator, @Nonnull TransformContext context) {
        StreamNode node = context.getUnionizedUpstreamNode(operator);
        SingleOutputStreamOperator<RtEvent> stream = node.asDataStream()
            .map(mapFunctionProvider.apply(operator, context));
        Integer parallelism = operator.getParallelism();
        stream.setParallelism(parallelism == null ? node.getParallelism() : parallelism);
        return StreamNode.of(stream);
    }
}
