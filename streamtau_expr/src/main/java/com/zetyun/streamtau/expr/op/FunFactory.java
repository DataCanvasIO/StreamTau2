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

package com.zetyun.streamtau.expr.op;

import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.AbsEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.AcosEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.AsinEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.AtanEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.CosEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.CoshEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.ExpEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.LogEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.SinEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.SinhEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.TanEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.mathematical.TanhEvaluatorFactory;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class FunFactory {
    @Contract("_ -> new")
    public static @NotNull UnaryOp getUnary(@NotNull String funName) {
        switch (funName) {
            case "abs":
                return new UnaryOp(AbsEvaluatorFactory.INS);
            case "sin":
                return new UnaryOp(SinEvaluatorFactory.INS);
            case "cos":
                return new UnaryOp(CosEvaluatorFactory.INS);
            case "tan":
                return new UnaryOp(TanEvaluatorFactory.INS);
            case "asin":
                return new UnaryOp(AsinEvaluatorFactory.INS);
            case "acos":
                return new UnaryOp(AcosEvaluatorFactory.INS);
            case "atan":
                return new UnaryOp(AtanEvaluatorFactory.INS);
            case "cosh":
                return new UnaryOp(CoshEvaluatorFactory.INS);
            case "sinh":
                return new UnaryOp(SinhEvaluatorFactory.INS);
            case "tanh":
                return new UnaryOp(TanhEvaluatorFactory.INS);
            case "log":
                return new UnaryOp(LogEvaluatorFactory.INS);
            case "exp":
                return new UnaryOp(ExpEvaluatorFactory.INS);
            default:
                throw new ParseCancellationException("Invalid fun name: \"" + funName + "\".");
        }
    }
}
