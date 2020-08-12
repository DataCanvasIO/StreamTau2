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

import com.zetyun.streamtau.expr.parser.StreamtauExprCompiler;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.expr.runtime.var.RtVar;
import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.runtime.schema.RtSchemaRoot;
import com.zetyun.streamtau.streaming.exception.InvalidMappingTarget;
import com.zetyun.streamtau.streaming.exception.MissingInputSchema;
import com.zetyun.streamtau.streaming.exception.OperatorHasNoSchema;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.model.mapper.SchemaMapper;
import com.zetyun.streamtau.streaming.model.mapper.SchemaMapping;
import com.zetyun.streamtau.streaming.runtime.mapper.RtSchemaMapping;
import com.zetyun.streamtau.streaming.runtime.mapper.SchemaMapperFunction;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import org.apache.flink.api.common.functions.MapFunction;

public class SchemaMapperFunctionProvider implements MapFunctionProvider {
    @Override
    public MapFunction<RtEvent, RtEvent> apply(Operator operator, TransformContext context) {
        RtSchemaRoot inputSchema = context.getInputSchema(operator);
        RtSchemaRoot outputSchema = context.getSchemaOf(operator);
        if (inputSchema == null) {
            throw new MissingInputSchema(operator);
        }
        if (outputSchema == null) {
            throw new OperatorHasNoSchema(operator);
        }
        SchemaMapping[] schemaMappings = ((SchemaMapper) operator).getMappings();
        RtSchemaMapping[] rtSchemaMappings = new RtSchemaMapping[schemaMappings.length];
        int i = 0;
        for (SchemaMapping mapping : ((SchemaMapper) operator).getMappings()) {
            String target = mapping.getTarget();
            RtExpr rtTarget = StreamtauExprCompiler.INS.parse(target).compileIn(outputSchema.getRoot());
            if (!(rtTarget instanceof RtVar)) {
                throw new InvalidMappingTarget(target, outputSchema);
            }
            String value = mapping.getValue();
            RtExpr rtValue = StreamtauExprCompiler.INS.parse(value).compileIn(inputSchema.getRoot());
            rtSchemaMappings[i++] = new RtSchemaMapping((RtVar) rtTarget, rtValue);
        }
        return new SchemaMapperFunction(rtSchemaMappings, outputSchema.getMaxIndex());
    }
}
