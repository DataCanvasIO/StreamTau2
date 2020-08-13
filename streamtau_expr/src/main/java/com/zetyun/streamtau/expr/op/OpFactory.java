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

import com.zetyun.streamtau.expr.antlr4.StreamtauExprParser;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.arithmetic.AddEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.arithmetic.DivEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.arithmetic.MulEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.arithmetic.SubEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.relational.EqEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.relational.GeEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.relational.GtEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.relational.LeEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.relational.LtEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.relational.NeEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.string.EndsWithEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.string.StartsWithEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.arithmetic.NegEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.unary.arithmetic.PosEvaluatorFactory;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import javax.annotation.Nonnull;

public final class OpFactory {
    private OpFactory() {
    }

    @Nonnull
    public static UnaryOp getUnary(int type) {
        switch (type) {
            case StreamtauExprParser.ADD:
                return new UnaryOp(PosEvaluatorFactory.INS);
            case StreamtauExprParser.SUB:
                return new UnaryOp(NegEvaluatorFactory.INS);
            case StreamtauExprParser.NOT:
                return new NotOp();
            default:
                throw new ParseCancellationException("Invalid operator type: " + type);
        }
    }

    @Nonnull
    public static BinaryOp getBinary(int type) {
        switch (type) {
            case StreamtauExprParser.ADD:
                return new BinaryOp(AddEvaluatorFactory.INS);
            case StreamtauExprParser.SUB:
                return new BinaryOp(SubEvaluatorFactory.INS);
            case StreamtauExprParser.MUL:
                return new BinaryOp(MulEvaluatorFactory.INS);
            case StreamtauExprParser.DIV:
                return new BinaryOp(DivEvaluatorFactory.INS);
            case StreamtauExprParser.LT:
                return new BinaryOp(LtEvaluatorFactory.INS);
            case StreamtauExprParser.LE:
                return new BinaryOp(LeEvaluatorFactory.INS);
            case StreamtauExprParser.EQ:
                return new BinaryOp(EqEvaluatorFactory.INS);
            case StreamtauExprParser.GT:
                return new BinaryOp(GtEvaluatorFactory.INS);
            case StreamtauExprParser.GE:
                return new BinaryOp(GeEvaluatorFactory.INS);
            case StreamtauExprParser.NE:
                return new BinaryOp(NeEvaluatorFactory.INS);
            case StreamtauExprParser.AND:
                return new AndOp();
            case StreamtauExprParser.OR:
                return new OrOp();
            case StreamtauExprParser.STARTSWITH:
                return new BinaryOp(StartsWithEvaluatorFactory.INS);
            case StreamtauExprParser.ENDSWITH:
                return new BinaryOp(EndsWithEvaluatorFactory.INS);
            default:
                throw new ParseCancellationException("Invalid operator type: " + type);
        }
    }
}
