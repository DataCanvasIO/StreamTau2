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

package com.zetyun.streamtau.expr.runtime.op;

import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.UnaryEvaluator;
import com.zetyun.streamtau.runtime.context.ExecContext;

public class RtUnaryOp implements RtExpr {
    private static final long serialVersionUID = 1720398654388186131L;

    protected final RtExpr para;
    private final UnaryEvaluator evaluator;

    public RtUnaryOp(UnaryEvaluator evaluator, RtExpr para) {
        this.evaluator = evaluator;
        this.para = para;
    }

    @Override
    public Object eval(ExecContext etx) {
        return evaluator.eval(para.eval(etx));
    }
}
