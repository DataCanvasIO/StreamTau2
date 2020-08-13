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

import com.zetyun.streamtau.expr.runtime.exception.FailGetBinaryEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Slf4j
public class BinaryUniversalEvaluator implements BinaryEvaluator {
    private static final long serialVersionUID = -3997468333071387621L;

    private final BinaryEvaluatorFactory factory;

    @Override
    public Object eval(@Nonnull Object value0, @Nonnull Object value1) {
        Class<?> paraType0 = value0.getClass();
        Class<?> paraType1 = value1.getClass();
        BinaryEvaluator evaluator = factory.getEvaluator(paraType0, paraType1);
        if (evaluator != this) {
            return evaluator.eval(value0, value1);
        }
        throw new FailGetBinaryEvaluator(factory, paraType0, paraType1);
    }
}
