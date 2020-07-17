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

import com.zetyun.streamtau.expr.core.AbstractExpr;
import com.zetyun.streamtau.expr.core.CompileContext;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.expr.runtime.var.RtIndexedVar;
import com.zetyun.streamtau.expr.runtime.var.RtNamedVar;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class Var extends AbstractExpr {
    private final String name;

    @Override
    public Class<?> calcType(@NotNull CompileContext ctx) {
        return ctx.get(name);
    }

    @Override
    public RtExpr compileIn(@NotNull CompileContext ctx) {
        int index = ctx.getIndex(name);
        if (index >= 0) {
            return new RtIndexedVar(index);
        }
        return new RtNamedVar(name);
    }
}
