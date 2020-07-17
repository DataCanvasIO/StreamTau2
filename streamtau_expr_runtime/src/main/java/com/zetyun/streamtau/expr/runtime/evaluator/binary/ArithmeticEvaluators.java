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

package com.zetyun.streamtau.expr.runtime.evaluator.binary;


import com.zetyun.streamtau.expr.annotation.Evaluators;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Evaluators(
    evaluatorInterface = BinaryEvaluator.class,
    evaluatorFactory = BinaryEvaluatorFactory.class,
    universalEvaluator = BinaryUniversalEvaluator.class
)
public class ArithmeticEvaluators {
    public static int add(int value0, int value1) {
        return value0 + value1;
    }

    public static long add(long value0, long value1) {
        return value0 + value1;
    }

    public static double add(double value0, double value1) {
        return value0 + value1;
    }

    public static BigDecimal add(@NotNull BigDecimal value0, BigDecimal value1) {
        return value0.add(value1);
    }

    // This is not arithmetic op, but put here to share the same evaluator factory.
    public static String add(String s0, String s1) {
        return s0 + s1;
    }

    public static int sub(int value0, int value1) {
        return value0 - value1;
    }

    public static long sub(long value0, long value1) {
        return value0 - value1;
    }

    public static double sub(double value0, double value1) {
        return value0 - value1;
    }

    public static BigDecimal sub(@NotNull BigDecimal value0, BigDecimal value1) {
        return value0.subtract(value1);
    }

    public static int mul(int value0, int value1) {
        return value0 * value1;
    }

    public static long mul(long value0, long value1) {
        return value0 * value1;
    }

    public static double mul(double value0, double value1) {
        return value0 * value1;
    }

    public static BigDecimal mul(@NotNull BigDecimal value0, BigDecimal value1) {
        return value0.multiply(value1);
    }

    public static int div(int value0, int value1) {
        return value0 / value1;
    }

    public static long div(long value0, long value1) {
        return value0 / value1;
    }

    public static double div(double value0, double value1) {
        return value0 / value1;
    }

    public static BigDecimal div(@NotNull BigDecimal value0, BigDecimal value1) {
        return value0.divide(value1, RoundingMode.HALF_EVEN);
    }
}
