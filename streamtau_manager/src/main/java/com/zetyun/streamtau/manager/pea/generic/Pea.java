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

package com.zetyun.streamtau.manager.pea.generic;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface Pea<I, T> {
    I getId();

    void setId(I id);

    T getType();

    default void transferAnnex() {
    }

    @SuppressWarnings("unchecked")
    default Collection<I> children() {
        Set<I> set = new HashSet<>();
        Field[] fields = getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(PeaId.class)) {
                    field.setAccessible(true);
                    set.add((I) field.get(this));
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("This cannot happen.");
        }
        return set;
    }

    default boolean reference(I id) {
        Field[] fields = getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(PeaId.class)) {
                    field.setAccessible(true);
                    if (id.equals(field.get(this))) {
                        return true;
                    }
                }
            }
            return false;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("This cannot happen.");
        }
    }
}
