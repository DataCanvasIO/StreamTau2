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

package com.zetyun.streamtau.expr.runtime.evaluator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public abstract class EvaluatorFactory<EvaluatorT> implements Serializable {
    private static final long serialVersionUID = -1064698862664341357L;

    protected final Map<String, EvaluatorT> lookup;
    protected final Map<String, Class<?>> types;

    protected EvaluatorFactory() {
        lookup = new HashMap<>();
        types = new HashMap<>();
    }

    @Nonnull
    protected static String paraId(Class<?> paraType) {
        if (List.class.isAssignableFrom(paraType)) {
            return "List";
        }
        if (Map.class.isAssignableFrom(paraType)) {
            return "Map";
        }
        return paraType.getSimpleName().replace("[]", "Array");
    }
}
