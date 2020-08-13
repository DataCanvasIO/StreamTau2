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

package com.zetyun.streamtau.expr.parser;

import com.zetyun.streamtau.expr.antlr4.StreamtauExprLexer;
import com.zetyun.streamtau.expr.antlr4.StreamtauExprParser;
import com.zetyun.streamtau.expr.core.Expr;
import com.zetyun.streamtau.expr.exception.ExprSyntaxError;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import javax.annotation.Nonnull;

public class StreamtauExprCompiler {
    public static final StreamtauExprCompiler INS = new StreamtauExprCompiler();

    private final StreamtauExprErrorListener errorListener;
    private final StreamtauExprVisitorImpl visitor;

    private StreamtauExprCompiler() {
        errorListener = new StreamtauExprErrorListener();
        visitor = new StreamtauExprVisitorImpl();
    }

    @Nonnull
    private StreamtauExprParser getParser(String input) {
        CharStream stream = CharStreams.fromString(input);
        StreamtauExprLexer lexer = new StreamtauExprLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        StreamtauExprParser parser = new StreamtauExprParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        return parser;
    }

    private void collectParseError() {
        List<String> errorMessages = errorListener.getErrorMessages();
        if (!errorMessages.isEmpty()) {
            throw new ExprSyntaxError(errorMessages);
        }
    }

    public Expr parse(String input) {
        StreamtauExprParser parser = getParser(input);
        ParseTree tree = parser.expr();
        collectParseError();
        return visitor.visit(tree);
    }
}
