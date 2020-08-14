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

package com.zetyun.streamtau.streaming.transformer.filter;

import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.transformer.SingleOutputTransformer;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import lombok.RequiredArgsConstructor;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class FilterTransformer implements SingleOutputTransformer {
    private final FilterFunctionProvider filterFunctionProvider;

    @Nonnull
    @Override
    public SingleOutputStreamOperator<RtEvent> transformNode(
        @Nonnull StreamNode node,
        @Nonnull Operator operator,
        @Nonnull TransformContext context
    ) {
        if (operator.getSchemaId() == null) {
            operator.setSchemaId(node.getSchemaId());
        }
        return node.asDataStream()
            .filter(filterFunctionProvider.apply(operator, context));
    }
}
