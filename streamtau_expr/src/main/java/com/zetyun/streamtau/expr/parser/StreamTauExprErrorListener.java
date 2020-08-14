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

import lombok.Getter;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.LinkedList;
import java.util.List;

public class StreamTauExprErrorListener extends BaseErrorListener {
    @Getter
    private final List<String> errorMessages = new LinkedList<>();

    @Override
    public void syntaxError(
        Recognizer<?, ?> recognizer, Object offendingSymbol,
        int line,
        int charPositionInLine,
        String msg,
        RecognitionException exception
    ) {
        String errorMessage = "Line " + line + ":" + charPositionInLine + ": " + msg + ".\n";
        errorMessages.add(errorMessage);
    }
}
