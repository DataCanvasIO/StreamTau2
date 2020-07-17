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

import java.util.List;
import java.util.Map;

@Evaluators(
    evaluatorInterface = BinaryEvaluator.class,
    evaluatorFactory = BinaryEvaluatorFactory.class,
    universalEvaluator = BinaryUniversalEvaluator.class
)
public class IndexEvaluators {
    @Contract(pure = true)
    public static Object index(@NotNull Object[] array, @NotNull Long index) {
        return array[index.intValue()];
    }

    @Contract(pure = true)
    public static Long index(@NotNull Long[] array, @NotNull Long index) {
        return array[index.intValue()];
    }

    public static String index(@NotNull String[] array, @NotNull Long index) {
        return array[index.intValue()];
    }

    public static Object index(@NotNull List<?> array, @NotNull Long index) {
        return array.get(index.intValue());
    }

    public static Object index(@NotNull Map<String, ?> map, String index) {
        return map.get(index);
    }
}
