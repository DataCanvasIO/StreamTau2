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

import com.zetyun.streamtau.streaming.exception.UnsupportedOperator;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import com.zetyun.streamtau.streaming.transformer.sink.PrintSinkTransformer;
import com.zetyun.streamtau.streaming.transformer.sink.TestCollectSinkTransformer;
import com.zetyun.streamtau.streaming.transformer.source.InPlaceSourceTransformer;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class TransformerFactory {
    private static TransformerFactory INS;

    private final Map<String, Transformer> transformerMap;

    private TransformerFactory() {
        transformerMap = new LinkedHashMap<>(10);
        registerTransformer("prelude.in-place-source", new InPlaceSourceTransformer());
        registerTransformer("prelude.print-sink", new PrintSinkTransformer());
        registerTransformer("test.collect-sink", new TestCollectSinkTransformer());
    }

    public static TransformerFactory get() {
        if (INS == null) {
            INS = new TransformerFactory();
        }
        return INS;
    }

    private void registerTransformer(String type, Transformer transformer) {
        transformerMap.put(type, transformer);
    }

    public StreamNode transform(Operator operator, TransformerContext context) {
        Transformer transformer = transformerMap.get(operator.getFid());
        if (transformer != null) {
            StreamNode node = transformer.transform(operator, context);
            return node.setName(operator.getName());
        }
        throw new UnsupportedOperator(operator);
    }
}
