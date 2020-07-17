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

public class RtAndOp extends RtBinaryOp {
    private static final long serialVersionUID = -709513497487133841L;

    public RtAndOp(RtExpr para0, RtExpr para1) {
        super(null, para0, para1);
    }

    @Override
    public Object eval(ExecContext etx) {
        return (boolean) para0.eval(etx) && (boolean) para1.eval(etx);
    }
}
