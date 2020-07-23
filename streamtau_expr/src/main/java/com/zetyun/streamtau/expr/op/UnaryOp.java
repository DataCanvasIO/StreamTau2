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
import com.zetyun.streamtau.expr.runtime.evaluator.unary.UnaryEvaluator;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.UnaryEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.op.RtUnaryOp;
import com.zetyun.streamtau.runtime.context.CompileContext;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class UnaryOp extends AbstractExpr {
    private final UnaryEvaluatorFactory factory;
    @Setter
    protected Expr expr;

    @Override
    public RtExpr compileIn(CompileContext ctx) {
        RtExpr rtExpr = expr.compileIn(ctx);
        UnaryEvaluator evaluator = factory.getEvaluator(expr.typeIn(ctx));
        RtUnaryOp rt = new RtUnaryOp(evaluator, rtExpr);
        if (rtExpr instanceof RtConst) {
            return new RtConst(rt.eval(null));
        }
        return rt;
    }

    @Override
    public Class<?> calcType(CompileContext ctx) {
        return factory.getType(expr.typeIn(ctx));
    }
}
