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
import com.zetyun.streamtau.runtime.schema.RtSchemaParser;
import com.zetyun.streamtau.runtime.schema.RtSchemaRoot;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.runtime.mapper.SchemaStringfyFunction;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import org.apache.flink.streaming.api.datastream.DataStream;

public class SinkUtils {
    // TODO: if the new node should be registered to context
    public static DataStream<RtEvent> beforeSink(Operator operator, TransformContext context) {
        StreamNode upstreamNode = context.getUnionizedUpstreamNode(operator);
        RtSchemaRoot schema = context.getSchemaOf(upstreamNode);
        if (schema == null) {
            return upstreamNode.asDataStream();
        }
        final RtSchemaParser parser = RtSchemaParser.createJsonEventParser(schema);
        return upstreamNode.asDataStream()
            .map(new SchemaStringfyFunction(parser));
    }
}
