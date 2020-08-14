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

import com.zetyun.streamtau.expr.core.Expr;
import com.zetyun.streamtau.expr.parser.StreamtauExprCompiler;
import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.runtime.schema.RtSchemaRoot;
import com.zetyun.streamtau.streaming.exception.MissingInputSchema;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.model.filter.ExprFilter;
import com.zetyun.streamtau.streaming.runtime.filter.ExprFilterFunction;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import org.apache.flink.api.common.functions.FilterFunction;

import javax.annotation.Nonnull;

public class ExprFilterFunctionProvider implements FilterFunctionProvider {
    @Override
    public FilterFunction<RtEvent> apply(Operator operator, @Nonnull TransformContext context) {
        RtSchemaRoot inputSchema = context.getInputSchema(operator);
        if (inputSchema == null) {
            throw new MissingInputSchema(operator);
        }
        Expr expr = StreamtauExprCompiler.INS.parse(((ExprFilter) operator).getExpr());
        return new ExprFilterFunction(expr.compileIn(inputSchema.getRoot()));
    }
}
