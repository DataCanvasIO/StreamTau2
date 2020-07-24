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

package com.zetyun.streamtau.expr.var;

import com.zetyun.streamtau.expr.exception.ElementNotExist;
import com.zetyun.streamtau.expr.exception.NotRtVar;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.runtime.context.CompileContext;
import com.zetyun.streamtau.runtime.context.ExecContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VarStub implements RtExpr {
    private static final long serialVersionUID = 5088685130019153601L;

    private final CompileContext ctx;

    @Override
    public Object eval(ExecContext etx) {
        throw new NotRtVar(ctx);
    }

    public RtExpr getChild(String name) {
        CompileContext child = ctx.getChild(name);
        if (child != null) {
            return Var.createVar(child);
        }
        throw new ElementNotExist(name, ctx);
    }

    public RtExpr getChild(int index) {
        CompileContext child = ctx.getChild(index);
        if (child != null) {
            return Var.createVar(child);
        }
        throw new ElementNotExist(index, ctx);
    }
}
