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
import com.zetyun.streamtau.expr.exception.ElementNotExist;
import com.zetyun.streamtau.expr.exception.VarIndexError;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.expr.runtime.var.RtVar;
import com.zetyun.streamtau.runtime.context.CompileContext;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class Var extends AbstractExpr {
    private final String name;

    @Contract("_ -> new")
    public static @NotNull RtExpr createVar(@NotNull CompileContext ctx) {
        int index = ctx.getIndex();
        if (index >= 0) {
            return new RtVar(index);
        }
        if (ctx.getJavaClass() == Void.class) {
            return new VarStub(ctx);
        }
        throw new VarIndexError(ctx);
    }

    @Override
    public Class<?> calcType(@NotNull CompileContext ctx) {
        CompileContext child = ctx.getChild(name);
        if (child != null) {
            return ctx.getChild(name).getJavaClass();
        }
        throw new ElementNotExist(name, ctx);
    }

    @Override
    public RtExpr compileIn(@NotNull CompileContext ctx) {
        CompileContext child = ctx.getChild(name);
        if (child != null) {
            return createVar(child);
        }
        throw new ElementNotExist(name, ctx);
    }
}
