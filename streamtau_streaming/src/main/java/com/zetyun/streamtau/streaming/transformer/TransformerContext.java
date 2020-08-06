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

import com.zetyun.streamtau.streaming.model.Dag;
import com.zetyun.streamtau.streaming.model.sink.Sink;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

@RequiredArgsConstructor
public final class TransformerContext {
    @Getter
    private final StreamExecutionEnvironment env;
    @Getter
    private final Dag dag;

    public static void transform(StreamExecutionEnvironment env, Dag dag) {
        TransformerContext context = new TransformerContext(env, dag);
        context.transformDag();
    }

    private void transformDag() {
        for (Sink sink : dag.getSinks()) {
            TransformerFactory.get().transform(sink, this);
        }
    }
}
