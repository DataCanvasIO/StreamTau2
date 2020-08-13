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

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@RequiredArgsConstructor
public class Var extends AbstractExpr {
    private final String name;

    @Nonnull
    public static RtExpr createVar(@Nonnull CompileContext ctx) {
        int index = ctx.getIndex();
        if (index >= 0) {
            return new RtVar(index);
        }
        if (ctx.getJavaClass() == Void.class) {
            return new VarStub(ctx);
        }
        throw new VarIndexError(ctx);
    }

    @Nonnull
    @Override
    public Class<?> calcType(@Nullable CompileContext ctx) {
        CompileContext child = Objects.requireNonNull(ctx).getChild(name);
        if (child != null) {
            return ctx.getChild(name).getJavaClass();
        }
        throw new ElementNotExist(name, ctx);
    }

    @Nonnull
    @Override
    public RtExpr compileIn(@Nullable CompileContext ctx) {
        CompileContext child = Objects.requireNonNull(ctx).getChild(name);
        if (child != null) {
            return createVar(child);
        }
        throw new ElementNotExist(name, ctx);
    }
}
