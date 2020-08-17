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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class PeaUtils {
    private PeaUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <I> void replacePeaIds(Object obj, Function<I, I> replaceFun) {
        if (obj == null) {
            return;
        }
        try {
            for (Class<?> clazz = obj.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    Class<?> fieldType = field.getType();
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value == null) {
                        continue;
                    }
                    if (field.isAnnotationPresent(PeaId.class)) {
                        if (fieldType.isArray()) {
                            for (int i = 0; i < Array.getLength(value); i++) {
                                I oldValue = (I) Array.get(value, i);
                                I newValue = replaceFun.apply(oldValue);
                                if (newValue != oldValue) {
                                    Array.set(value, i, newValue);
                                }
                            }
                        } else if (Collection.class.isAssignableFrom(fieldType)) {
                            Collection<I> collection = (Collection<I>) value;
                            for (I o : collection) {
                                I newValue = replaceFun.apply(o);
                                if (newValue != o) {
                                    collection.remove(o);
                                    collection.add(newValue);
                                }
                            }
                        } else if (Map.class.isAssignableFrom(fieldType)) {
                            Map<Object, I> map = (Map<Object, I>) value;
                            for (Map.Entry<Object, I> entry : map.entrySet()) {
                                I oldValue = entry.getValue();
                                I newValue = replaceFun.apply(oldValue);
                                if (newValue != oldValue) {
                                    map.put(entry.getKey(), newValue);
                                }
                            }
                        } else {
                            I newValue = replaceFun.apply((I) value);
                            if (newValue != value) {
                                field.set(obj, newValue);
                            }
                        }
                    } else if (field.isAnnotationPresent(PeaId.InIt.class)) {
                        if (fieldType.isArray()) {
                            for (int i = 0; i < Array.getLength(value); i++) {
                                replacePeaIds(Array.get(value, i), replaceFun);
                            }
                        } else if (Collection.class.isAssignableFrom(fieldType)) {
                            for (Object o : (Collection<Object>) value) {
                                replacePeaIds(o, replaceFun);
                            }
                        } else if (Map.class.isAssignableFrom(fieldType)) {
                            for (Object o : ((Map<?, Object>) value).values()) {
                                replacePeaIds(o, replaceFun);
                            }
                        } else {
                            replacePeaIds(value, replaceFun);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("PANIC in PeaUtils::replacePeaIds.");
        }
    }

    @SuppressWarnings("unchecked")
    public static <I> void collectPeaIds(Set<I> set, Object obj) {
        if (obj == null) {
            return;
        }
        try {
            for (Class<?> clazz = obj.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                Class<?> superClass = clazz.getSuperclass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    Class<?> fieldType = field.getType();
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value == null) {
                        continue;
                    }
                    if (field.isAnnotationPresent(PeaId.class)) {
                        if (fieldType.isArray()) {
                            for (int i = 0; i < Array.getLength(value); i++) {
                                set.add((I) Array.get(value, i));
                            }
                        } else if (Collection.class.isAssignableFrom(fieldType)) {
                            set.addAll((Collection<I>) value);
                        } else if (Map.class.isAssignableFrom(fieldType)) {
                            set.addAll(((Map<?, I>) value).values());
                        } else {
                            set.add((I) value);
                        }
                    } else if (field.isAnnotationPresent(PeaId.InIt.class)) {
                        if (fieldType.isArray()) {
                            for (int i = 0; i < Array.getLength(value); i++) {
                                collectPeaIds(set, Array.get(value, i));
                            }
                        } else if (Collection.class.isAssignableFrom(fieldType)) {
                            for (Object o : (Collection<Object>) value) {
                                collectPeaIds(set, o);
                            }
                        } else if (Map.class.isAssignableFrom(fieldType)) {
                            for (Object o : ((Map<?, Object>) value).values()) {
                                collectPeaIds(set, o);
                            }
                        } else {
                            collectPeaIds(set, value);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("PANIC in PeaUtils::collectPeaIds.");
        }
    }
}
