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

import com.zetyun.streamtau.expr.runtime.exception.FailGetUnaryEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Slf4j
public class UnaryUniversalEvaluator implements UnaryEvaluator {
    private static final long serialVersionUID = 8930670424456244088L;

    private final UnaryEvaluatorFactory factory;

    @Override
    public Object eval(@NotNull Object value) {
        Class<?> paraType = value.getClass();
        UnaryEvaluator evaluator = factory.getEvaluator(paraType);
        if (evaluator != this) {
            return evaluator.eval(value);
        }
        throw new FailGetUnaryEvaluator(factory, paraType);
    }
}
