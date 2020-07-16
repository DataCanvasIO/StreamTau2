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

package com.zetyun.streamtau.expr.runtime.exception;

import com.zetyun.streamtau.expr.runtime.evaluator.tertiary.TertiaryEvaluatorFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FailGetTertiaryEvaluator extends RuntimeException {
    private static final long serialVersionUID = 215358748995402716L;

    private final TertiaryEvaluatorFactory factory;
    private final Class<?> paraType0;
    private final Class<?> paraType1;
    private final Class<?> paraType2;
}
