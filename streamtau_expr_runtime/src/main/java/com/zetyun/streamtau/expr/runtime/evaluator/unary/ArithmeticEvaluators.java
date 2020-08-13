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

package com.zetyun.streamtau.expr.runtime.evaluator.unary;

import com.zetyun.streamtau.expr.annotation.Evaluators;

import java.math.BigDecimal;

@Evaluators(
    evaluatorInterface = UnaryEvaluator.class,
    evaluatorFactory = UnaryEvaluatorFactory.class,
    universalEvaluator = UnaryUniversalEvaluator.class
)
public final class ArithmeticEvaluators {
    private ArithmeticEvaluators() {
    }

    public static int pos(int value) {
        return value;
    }

    public static long pos(long value) {
        return value;
    }

    public static double pos(double value) {
        return value;
    }

    public static BigDecimal pos(BigDecimal value) {
        return value;
    }

    public static int neg(int value) {
        return -value;
    }

    public static long neg(long value) {
        return -value;
    }

    public static double neg(double value) {
        return -value;
    }

    public static BigDecimal neg(BigDecimal value) {
        return value.negate();
    }
}
