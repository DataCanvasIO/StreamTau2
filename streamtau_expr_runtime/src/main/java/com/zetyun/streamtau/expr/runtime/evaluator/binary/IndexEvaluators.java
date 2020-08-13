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

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

@Evaluators(
    evaluatorInterface = BinaryEvaluator.class,
    evaluatorFactory = BinaryEvaluatorFactory.class,
    universalEvaluator = BinaryUniversalEvaluator.class
)
public final class IndexEvaluators {
    private IndexEvaluators() {
    }

    public static Object index(@Nonnull Object[] array, @Nonnull Long index) {
        return array[index.intValue()];
    }

    public static Long index(@Nonnull Long[] array, @Nonnull Long index) {
        return array[index.intValue()];
    }

    public static String index(@Nonnull String[] array, @Nonnull Long index) {
        return array[index.intValue()];
    }

    public static Object index(@Nonnull List<?> array, @Nonnull Long index) {
        return array.get(index.intValue());
    }

    public static Object index(@Nonnull Map<String, ?> map, String index) {
        return map.get(index);
    }
}
