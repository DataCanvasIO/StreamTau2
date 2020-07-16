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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Evaluators(
    evaluatorInterface = BinaryEvaluator.class,
    evaluatorFactory = BinaryEvaluatorFactory.class,
    universalEvaluator = BinaryUniversalEvaluator.class
)
public class StringEvaluators {
    public static char charAt(@NotNull String str, int index) {
        return str.charAt(index);
    }

    @Contract(pure = true)
    public static int indexOf(@NotNull String haystack, String needle) {
        return haystack.indexOf(needle);
    }

    public static int lastIndexOf(@NotNull String haystack, String needle) {
        return haystack.lastIndexOf(needle);
    }

    @Contract(pure = true)
    public static @NotNull String substring(@NotNull String haystack, int start) {
        return haystack.substring(start);
    }

    public static boolean contains(@NotNull String haystack, String needle) {
        return haystack.contains(needle);
    }

    public static boolean endsWith(@NotNull String haystack, String needle) {
        return haystack.endsWith(needle);
    }

    public static boolean matches(@NotNull String haystack, String needle) {
        return haystack.matches(needle);
    }

    public static boolean notContains(@NotNull String haystack, String needle) {
        return !haystack.contains(needle);
    }

    public static boolean notEndsWith(@NotNull String haystack, String needle) {
        return !haystack.endsWith(needle);
    }

    public static boolean notMatches(@NotNull String haystack, String needle) {
        return !haystack.matches(needle);
    }

    public static boolean notStartsWith(@NotNull String haystack, String needle) {
        return !haystack.startsWith(needle);
    }

    public static boolean startsWith(@NotNull String haystack, String needle) {
        return haystack.startsWith(needle);
    }
}
