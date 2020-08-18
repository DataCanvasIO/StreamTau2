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

package com.zetyun.streamtau.streaming.transformer.source;

import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import com.zetyun.streamtau.streaming.transformer.Transformer;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface GeneralSourceTransformer extends Transformer {
    @Nonnull
    DataStreamSource<?> transformSource(
        @Nonnull Operator operator,
        @Nonnull TransformContext context
    );

    @Nonnull
    @Override
    default StreamNode transform(@Nonnull Operator operator, @Nonnull TransformContext context) {
        DataStreamSource<?> source = transformSource(operator, context);
        Integer parallelism = operator.getParallelism();
        if (parallelism != null) {
            source.setParallelism(parallelism);
        }
        SingleOutputStreamOperator<RtEvent> stream = source.map(RtEvent::singleValue);
        if (parallelism != null) {
            stream.setParallelism(parallelism);
        }
        return StreamNode.of(stream);
    }
}
