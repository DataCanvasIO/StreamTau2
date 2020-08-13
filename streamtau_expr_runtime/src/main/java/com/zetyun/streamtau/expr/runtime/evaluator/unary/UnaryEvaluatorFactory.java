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

import com.zetyun.streamtau.expr.runtime.evaluator.EvaluatorFactory;
import com.zetyun.streamtau.expr.runtime.exception.FailGetUnaryEvaluator;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Slf4j
public abstract class UnaryEvaluatorFactory extends EvaluatorFactory<UnaryEvaluator> {
    private static final long serialVersionUID = -464551349014289660L;

    @Nonnull
    private static String id(@Nullable Class<?> paraType) {
        if (paraType != null) {
            return paraId(paraType);
        }
        return "Universal";
    }

    public UnaryEvaluator getEvaluator(Class<?> paraType) {
        UnaryEvaluator evaluator = lookup.get(id(paraType));
        if (evaluator != null) {
            return evaluator;
        }
        evaluator = lookup.get(id(null));
        if (evaluator != null) {
            log.debug("Use universal evaluator in \"{}\".", getClass().getSimpleName());
            return evaluator;
        }
        throw new FailGetUnaryEvaluator(this, paraType);
    }

    public Class<?> getType(Class<?> paraType) {
        if (paraType != null) {
            Class<?> type = types.get(id(paraType));
            if (type != null) {
                return type;
            }
        }
        return Object.class;
    }
}
