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

package com.zetyun.streamtau.core.pea;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public class PeaFactory<I, T, P extends Pea<I, T, P>> {
    public static final int CAPACITY = 64;

    @Getter
    private final Map<T, Class<?>> peaClassMap = new LinkedHashMap<>(CAPACITY);
    private final Map<T, Supplier<P>> peaSupplierMap = new LinkedHashMap<>(CAPACITY);

    @SuppressWarnings("unchecked")
    public P make(T type) {
        Supplier<P> supplier = peaSupplierMap.get(type);
        if (supplier != null) {
            return supplier.get();
        }
        try {
            return (P) peaClassMap.get(type).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot make pea of type \"" + type + "\".", e);
        }
    }

    public Class<?> classOf(T type) {
        return peaClassMap.get(type);
    }

    public void register(T type, Class<?> peaClass) {
        register(type, peaClass, null);
    }

    public void register(T type, Class<?> peaClass, @Nullable Supplier<P> supplier) {
        peaClassMap.put(type, peaClass);
        if (supplier != null) {
            peaSupplierMap.put(type, supplier);
        }
    }
}
