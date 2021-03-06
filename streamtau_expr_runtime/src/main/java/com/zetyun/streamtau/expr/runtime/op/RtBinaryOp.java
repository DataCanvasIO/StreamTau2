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

import javax.annotation.Nonnull;

public abstract class RtBinaryOp implements RtExpr {
    private static final long serialVersionUID = 1131738354159693880L;

    @Nonnull
    protected final RtExpr para0;
    @Nonnull
    protected final RtExpr para1;

    protected RtBinaryOp(@Nonnull RtExpr para0, @Nonnull RtExpr para1) {
        this.para0 = para0;
        this.para1 = para1;
    }
}
