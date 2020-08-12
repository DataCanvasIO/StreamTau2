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

package com.zetyun.streamtau.streaming.runtime.mapper;

import com.zetyun.streamtau.runtime.context.RtEvent;
import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.functions.MapFunction;

@RequiredArgsConstructor
public class SchemaMapperFunction implements MapFunction<RtEvent, RtEvent> {
    private static final long serialVersionUID = 96241259710387195L;

    private final RtSchemaMapping[] mappings;
    private final int numIndexedVars;

    @Override
    public RtEvent map(RtEvent event) throws Exception {
        RtEvent out = new RtEvent(numIndexedVars);
        for (RtSchemaMapping mapping : mappings) {
            mapping.getTarget().set(out, mapping.getValue().eval(event));
        }
        return out;
    }
}
