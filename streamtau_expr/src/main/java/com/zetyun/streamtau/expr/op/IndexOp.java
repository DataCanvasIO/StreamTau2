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

import com.zetyun.streamtau.expr.exception.InvalidIndexValue;
import com.zetyun.streamtau.expr.runtime.RtConst;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.index.IndexEvaluatorFactory;
import com.zetyun.streamtau.expr.var.VarStub;
import com.zetyun.streamtau.runtime.context.CompileContext;

public class IndexOp extends BinaryOp {
    public IndexOp() {
        super(IndexEvaluatorFactory.INS);
    }

    @Override
    public RtExpr compileIn(CompileContext ctx) {
        RtExpr rtExpr0 = expr0.compileIn(ctx);
        if (rtExpr0 instanceof VarStub) {
            VarStub stub = (VarStub) rtExpr0;
            RtExpr rtExpr1 = expr1.compileIn(ctx);
            if (rtExpr1 instanceof RtConst) {
                Object index = rtExpr1.eval(null);
                if (index instanceof Number) {
                    return stub.getChild(((Number) index).intValue());
                } else if (index instanceof String) {
                    return stub.getChild((String) index);
                }
                throw new InvalidIndexValue(index);
            }
            throw new InvalidIndexValue(rtExpr1);
        }
        return super.compileIn(ctx);
    }
}
