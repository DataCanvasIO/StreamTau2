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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Evaluators(
    evaluatorInterface = UnaryEvaluator.class,
    evaluatorFactory = UnaryEvaluatorFactory.class,
    universalEvaluator = UnaryUniversalEvaluator.class
)
public class StringEvaluators {
    public static int length(@NotNull String str) {
        return str.length();
    }

    @Contract(pure = true)
    public static @NotNull String toLowerCase(@NotNull String str) {
        return str.toLowerCase();
    }

    @Contract(pure = true)
    public static @NotNull String toUpperCase(@NotNull String str) {
        return str.toUpperCase();
    }

    @Contract(pure = true)
    public static @NotNull String trim(@NotNull String str) {
        return str.trim();
    }
}
