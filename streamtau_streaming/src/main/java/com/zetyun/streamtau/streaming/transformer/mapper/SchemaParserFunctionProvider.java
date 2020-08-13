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

import com.zetyun.streamtau.runtime.ScriptFormat;
import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.runtime.schema.RtSchemaParser;
import com.zetyun.streamtau.runtime.schema.RtSchemaRoot;
import com.zetyun.streamtau.streaming.exception.OperatorHasNoSchema;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.model.mapper.SchemaParser;
import com.zetyun.streamtau.streaming.runtime.mapper.SchemaParserFunction;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import org.apache.flink.api.common.functions.MapFunction;

import javax.annotation.Nonnull;

public class SchemaParserFunctionProvider implements MapFunctionProvider {
    @Override
    public MapFunction<RtEvent, RtEvent> apply(Operator operator, @Nonnull TransformContext context) {
        RtSchemaRoot schema = context.getSchemaOf(operator);
        if (schema == null) {
            throw new OperatorHasNoSchema(operator);
        }
        ScriptFormat format = ((SchemaParser) operator).getFormat();
        if (format == null) {
            format = ScriptFormat.APPLICATION_JSON;
        }
        RtSchemaParser parser = new RtSchemaParser(format, schema);
        return new SchemaParserFunction(parser);
    }
}
