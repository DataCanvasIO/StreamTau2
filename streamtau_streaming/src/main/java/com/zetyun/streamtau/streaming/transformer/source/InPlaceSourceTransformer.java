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
import com.zetyun.streamtau.streaming.model.source.InPlaceSource;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import com.zetyun.streamtau.streaming.transformer.Transformer;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;

import java.util.List;
import java.util.stream.Collectors;

public class InPlaceSourceTransformer implements Transformer {
    @Override
    public StreamNode transform(Operator operator, TransformContext context) {
        List<RtEvent> eventList = ((InPlaceSource) operator).getElements().stream()
            .map(RtEvent::singleValue)
            .collect(Collectors.toList());
        return StreamNode.of(
            context.getEnv()
                .fromCollection(eventList)
                .setParallelism(operator.getParallelism())
        );
    }
}
