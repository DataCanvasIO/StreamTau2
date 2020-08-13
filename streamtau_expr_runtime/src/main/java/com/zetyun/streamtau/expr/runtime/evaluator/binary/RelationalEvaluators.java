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

import java.math.BigDecimal;

@Evaluators(
    evaluatorInterface = BinaryEvaluator.class,
    evaluatorFactory = BinaryEvaluatorFactory.class,
    universalEvaluator = BinaryUniversalEvaluator.class
)
public final class RelationalEvaluators {
    private RelationalEvaluators() {
    }

    public static boolean eq(boolean first, boolean second) {
        return first == second;
    }

    public static boolean eq(int first, int second) {
        return first == second;
    }

    public static boolean eq(long first, long second) {
        return first == second;
    }

    public static boolean eq(double first, double second) {
        return first == second;
    }

    public static boolean eq(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) == 0;
    }

    public static boolean eq(String first, String second) {
        return first.equals(second);
    }

    public static boolean lt(int first, int second) {
        return first < second;
    }

    public static boolean lt(long first, long second) {
        return first < second;
    }

    public static boolean lt(double first, double second) {
        return first < second;
    }

    public static boolean lt(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) < 0;
    }

    public static boolean lt(String first, String second) {
        return first.compareTo(second) < 0;
    }

    public static boolean le(int first, int second) {
        return first <= second;
    }

    public static boolean le(long first, long second) {
        return first <= second;
    }

    public static boolean le(double first, double second) {
        return first <= second;
    }

    public static boolean le(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) <= 0;
    }

    public static boolean le(String first, String second) {
        return first.compareTo(second) <= 0;
    }

    public static boolean gt(int first, int second) {
        return first > second;
    }

    public static boolean gt(long first, long second) {
        return first > second;
    }

    public static boolean gt(double first, double second) {
        return first > second;
    }

    public static boolean gt(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) > 0;
    }

    public static boolean gt(String first, String second) {
        return first.compareTo(second) > 0;
    }

    public static boolean ge(int first, int second) {
        return first >= second;
    }

    public static boolean ge(long first, long second) {
        return first >= second;
    }

    public static boolean ge(double first, double second) {
        return first >= second;
    }

    public static boolean ge(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) >= 0;
    }

    public static boolean ge(String first, String second) {
        return first.compareTo(second) >= 0;
    }

    public static boolean ne(boolean first, boolean second) {
        return first != second;
    }

    public static boolean ne(int first, int second) {
        return first != second;
    }

    public static boolean ne(long first, long second) {
        return first != second;
    }

    public static boolean ne(double first, double second) {
        return first != second;
    }

    public static boolean ne(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) != 0;
    }

    public static boolean ne(String first, String second) {
        return first.compareTo(second) != 0;
    }
}
