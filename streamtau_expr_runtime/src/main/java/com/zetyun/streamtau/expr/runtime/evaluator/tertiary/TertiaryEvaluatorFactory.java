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

import com.zetyun.streamtau.expr.runtime.evaluator.EvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.exception.FailGetTertiaryEvaluator;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Slf4j
public class TertiaryEvaluatorFactory extends EvaluatorFactory<TertiaryEvaluator> {
    private static final long serialVersionUID = -2339963717462816482L;

    @Nonnull
    private static String id(@Nullable Class<?> paraType0, @Nullable Class<?> paraType1, @Nullable Class<?> paraType2) {
        if (paraType0 != null && paraType1 != null && paraType2 != null) {
            return paraId(paraType0) + paraId(paraType1) + paraId(paraType2);
        }
        return "Universal";
    }

    public TertiaryEvaluator getEvaluator(Class<?> paraType0, Class<?> paraType1, Class<?> paraType2) {
        TertiaryEvaluator evaluator = lookup.get(id(paraType0, paraType1, paraType2));
        if (evaluator != null) {
            return evaluator;
        }
        evaluator = lookup.get(id(null, null, null));
        if (evaluator != null) {
            log.debug("Use universal evaluator in \"{}\".", getClass().getSimpleName());
            return evaluator;
        }
        throw new FailGetTertiaryEvaluator(this, paraType0, paraType1, paraType2);
    }

    public Class<?> getType(Class<?> paraType0, Class<?> paraType1, Class<?> paraType2) {
        if (paraType0 != null && paraType1 != null) {
            Class<?> type = types.get(id(paraType0, paraType1, paraType2));
            if (type != null) {
                return type;
            }
        }
        return Object.class;
    }
}
