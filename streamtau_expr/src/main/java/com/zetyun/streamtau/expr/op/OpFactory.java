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
import com.zetyun.streamtau.expr.runtime.evaluator.binary.logical.AndEvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.evaluator.binary.logical.OrEvaluatorFactory;
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
import com.zetyun.streamtau.expr.runtime.evaluator.unary.logical.NotEvaluatorFactory;

public class OpFactory {
    public static UnaryOp getUnary(int type) {
        switch (type) {
            case StreamtauExprParser.ADD:
                return new UnaryOp(PosEvaluatorFactory.get());
            case StreamtauExprParser.SUB:
                return new UnaryOp(NegEvaluatorFactory.get());
            case StreamtauExprParser.NOT:
                return new UnaryOp(NotEvaluatorFactory.get());
            default:
                throw new IllegalArgumentException("Invalid operator type: " + type);
        }
    }

    public static BinaryOp getBinary(int type) {
        switch (type) {
            case StreamtauExprParser.ADD:
                return new BinaryOp(AddEvaluatorFactory.get());
            case StreamtauExprParser.SUB:
                return new BinaryOp(SubEvaluatorFactory.get());
            case StreamtauExprParser.MUL:
                return new BinaryOp(MulEvaluatorFactory.get());
            case StreamtauExprParser.DIV:
                return new BinaryOp(DivEvaluatorFactory.get());
            case StreamtauExprParser.LT:
                return new BinaryOp(LtEvaluatorFactory.get());
            case StreamtauExprParser.LE:
                return new BinaryOp(LeEvaluatorFactory.get());
            case StreamtauExprParser.EQ:
                return new BinaryOp(EqEvaluatorFactory.get());
            case StreamtauExprParser.GT:
                return new BinaryOp(GtEvaluatorFactory.get());
            case StreamtauExprParser.GE:
                return new BinaryOp(GeEvaluatorFactory.get());
            case StreamtauExprParser.NE:
                return new BinaryOp(NeEvaluatorFactory.get());
            case StreamtauExprParser.AND:
                return new BinaryOp(AndEvaluatorFactory.get());
            case StreamtauExprParser.OR:
                return new BinaryOp(OrEvaluatorFactory.get());
            case StreamtauExprParser.STARTSWITH:
                return new BinaryOp(StartsWithEvaluatorFactory.get());
            case StreamtauExprParser.ENDSWITH:
                return new BinaryOp(EndsWithEvaluatorFactory.get());
            default:
                throw new IllegalArgumentException("Invalid operator type: " + type);
        }
    }
}
