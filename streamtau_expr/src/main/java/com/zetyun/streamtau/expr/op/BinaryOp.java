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

package com.zetyun.streamtau.expr.op;

import com.zetyun.streamtau.expr.core.AbstractExpr;
import com.zetyun.streamtau.expr.core.Expr;
import com.zetyun.streamtau.expr.runtime.RtConst;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.BinaryEvaluator;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.BinaryEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.op.RtBinaryOpWithEvaluator;
import com.zetyun.streamtau.runtime.context.CompileContext;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class BinaryOp extends AbstractExpr {
    private final BinaryEvaluatorFactory factory;
    @Setter
    protected Expr expr0;
    @Setter
    protected Expr expr1;

    @Nonnull
    @Override
    public RtExpr compileIn(CompileContext ctx) {
        RtExpr rtExpr0 = expr0.compileIn(ctx);
        RtExpr rtExpr1 = expr1.compileIn(ctx);
        BinaryEvaluator evaluator = factory.getEvaluator(expr0.typeIn(ctx), expr1.typeIn(ctx));
        RtBinaryOpWithEvaluator rt = new RtBinaryOpWithEvaluator(evaluator, rtExpr0, rtExpr1);
        if (rtExpr0 instanceof RtConst && rtExpr1 instanceof RtConst) {
            return new RtConst(rt.eval(null));
        }
        return rt;
    }

    @Nonnull
    @Override
    public Class<?> calcType(CompileContext ctx) {
        return factory.getType(expr0.typeIn(ctx), expr1.typeIn(ctx));
    }
}
