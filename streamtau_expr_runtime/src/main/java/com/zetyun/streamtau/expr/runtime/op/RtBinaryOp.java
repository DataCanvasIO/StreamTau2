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
import com.zetyun.streamtau.expr.runtime.context.ExecContext;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.BinaryEvaluator;

public class RtBinaryOp implements RtExpr {
    private static final long serialVersionUID = -8130035605710130085L;

    protected final RtExpr para0;
    protected final RtExpr para1;
    private final BinaryEvaluator evaluator;

    public RtBinaryOp(BinaryEvaluator evaluator, RtExpr para0, RtExpr para1) {
        this.evaluator = evaluator;
        this.para0 = para0;
        this.para1 = para1;
    }

    @Override
    public Object eval(ExecContext etx) {
        return evaluator.eval(para0.eval(etx), para1.eval(etx));
    }
}
