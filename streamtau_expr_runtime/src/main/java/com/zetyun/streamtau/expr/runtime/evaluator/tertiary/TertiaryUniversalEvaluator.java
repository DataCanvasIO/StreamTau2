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

import com.zetyun.streamtau.expr.runtime.exception.FailGetTertiaryEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TertiaryUniversalEvaluator implements TertiaryEvaluator {
    private static final long serialVersionUID = -7714679083305736089L;

    private final TertiaryEvaluatorFactory factory;

    @Override
    public Object eval(Object value0, Object value1, Object value2) {
        Class<?> paraType0 = value0.getClass();
        Class<?> paraType1 = value1.getClass();
        Class<?> paraType2 = value2.getClass();
        TertiaryEvaluator evaluator = factory.getEvaluator(paraType0, paraType1, paraType2);
        if (evaluator != this) {
            return evaluator.eval(value0, value1, value2);
        }
        throw new FailGetTertiaryEvaluator(factory, paraType0, paraType1, paraType2);
    }
}
