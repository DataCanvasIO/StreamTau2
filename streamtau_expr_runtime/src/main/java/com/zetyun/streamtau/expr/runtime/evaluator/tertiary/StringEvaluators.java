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

package com.zetyun.streamtau.expr.runtime.evaluator.tertiary;

import com.zetyun.streamtau.expr.annotation.Evaluators;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Evaluators(
    evaluatorInterface = TertiaryEvaluator.class,
    evaluatorFactory = TertiaryEvaluatorFactory.class,
    universalEvaluator = TertiaryUniversalEvaluator.class
)
public class StringEvaluators {
    @Contract(pure = true)
    public static @NotNull String replace(@NotNull String haystack, String needle, String needle2) {
        return haystack.replace(needle, needle2);
    }

    @Contract(pure = true)
    public static @NotNull String substring(@NotNull String haystack, int start, int end) {
        return haystack.substring(start, end);
    }
}
