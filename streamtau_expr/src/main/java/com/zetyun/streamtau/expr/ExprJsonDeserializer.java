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

package com.zetyun.streamtau.expr;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.zetyun.streamtau.expr.core.Expr;
import com.zetyun.streamtau.expr.parser.StreamtauExprCompiler;

import java.io.IOException;
import javax.annotation.Nonnull;

public class ExprJsonDeserializer extends StdDeserializer<Expr> {
    private static final long serialVersionUID = -2170982952445776092L;

    protected ExprJsonDeserializer() {
        super(Expr.class);
    }

    @Override
    public Expr deserialize(
        @Nonnull JsonParser parser,
        DeserializationContext ctx
    ) throws IOException {
        String src = parser.getText();
        return StreamtauExprCompiler.INS.parse(src);
    }
}
