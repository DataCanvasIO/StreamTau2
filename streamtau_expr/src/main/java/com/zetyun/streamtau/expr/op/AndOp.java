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

import com.zetyun.streamtau.expr.runtime.RtConst;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.expr.runtime.op.RtAndOp;
import com.zetyun.streamtau.expr.runtime.op.RtBinaryOp;
import com.zetyun.streamtau.runtime.context.CompileContext;

public class AndOp extends BinaryOp {
    public AndOp() {
        super(null);
    }

    @Override
    public RtExpr compileIn(CompileContext ctx) {
        RtExpr rtExpr0 = expr0.compileIn(ctx);
        RtExpr rtExpr1 = expr1.compileIn(ctx);
        RtBinaryOp rt = new RtAndOp(rtExpr0, rtExpr1);
        if (rtExpr0 instanceof RtConst && rtExpr1 instanceof RtConst) {
            return new RtConst(rt.eval(null));
        }
        return rt;
    }

    @Override
    public Class<?> calcType(CompileContext ctx) {
        return Boolean.class;
    }
}
