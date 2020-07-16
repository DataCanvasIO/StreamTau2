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

import com.zetyun.streamtau.expr.core.CompileContext;
import com.zetyun.streamtau.expr.core.Expr;
import com.zetyun.streamtau.expr.runtime.HasValue;
import com.zetyun.streamtau.expr.runtime.RtConst;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.BinaryEvaluator;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.BinaryEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.op.RtBinaryOp;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class BinaryOp implements Expr {
    private final BinaryEvaluatorFactory factory;
    @Setter
    private Expr expr0;
    @Setter
    private Expr expr1;

    @Override
    public HasValue compileIn(CompileContext ctx) {
        HasValue hasValue0 = expr0.compileIn(ctx);
        HasValue hasValue1 = expr1.compileIn(ctx);
        BinaryEvaluator evaluator = factory.getEvaluator(expr0.typeIn(ctx), expr1.typeIn(ctx));
        RtBinaryOp rt = new RtBinaryOp(evaluator, hasValue0, hasValue1);
        if (hasValue0 instanceof RtConst && hasValue1 instanceof RtConst) {
            return new RtConst(rt.eval(null));
        }
        return rt;
    }

    @Override
    public Class<?> typeIn(CompileContext ctx) {
        return factory.getType(expr0.typeIn(ctx), expr1.typeIn(ctx));
    }
}
