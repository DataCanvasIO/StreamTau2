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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class PeaUtils {
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static <O> boolean doCollection(
        Collection<O> collection,
        Predicate<O> doOne,
        Predicate<Collection<O>> doAll
    ) {
        if (doAll == null) {
            for (O o : collection) {
                if (!doOne.test(o)) {
                    return false;
                }
            }
        } else {
            return doAll.test(collection);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <I> boolean searchPeaIds(
        Object obj,
        Predicate<I> doOne,
        Predicate<Collection<I>> doAll
    ) {
        if (obj == null) {
            return true;
        }
        try {
            for (Class<?> clazz = obj.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    Class<?> fieldType = field.getType();
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (field.isAnnotationPresent(PeaId.class)) {
                        if (fieldType.isArray()) {
                            for (int i = 0; i < Array.getLength(value); i++) {
                                if (!doOne.test((I) Array.get(value, i))) {
                                    return false;
                                }
                            }
                        } else if (Collection.class.isAssignableFrom(fieldType)) {
                            if (!doCollection((Collection<I>) value, doOne, doAll)) {
                                return false;
                            }
                        } else if (Map.class.isAssignableFrom(fieldType)) {
                            if (!doCollection(((Map<?, I>) value).values(), doOne, doAll)) {
                                return false;
                            }
                        } else {
                            if (!doOne.test((I) value)) {
                                return false;
                            }
                        }
                    } else {
                        if (fieldType.isArray()) {
                            for (int i = 0; i < Array.getLength(value); i++) {
                                if (!searchPeaIds(Array.get(value, i), doOne, doAll)) {
                                    return false;
                                }
                            }
                        } else if (Collection.class.isAssignableFrom(fieldType)) {
                            if (!doCollection((Collection<?>) value,
                                o -> searchPeaIds(o, doOne, doAll), null)) {
                                return false;
                            }
                        } else if (Map.class.isAssignableFrom(fieldType)) {
                            if (!doCollection(((Map<?, ?>) value).values(),
                                o -> searchPeaIds(o, doOne, doAll), null)) {
                                return false;
                            }
                        } else {
                            if (!searchPeaIds(value, doOne, doAll)) {
                                return false;
                            }
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("PANIC in searchPeaId().");
        }
        return true;
    }
}
