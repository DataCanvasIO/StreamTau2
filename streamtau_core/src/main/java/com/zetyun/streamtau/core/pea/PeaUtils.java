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

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import javax.annotation.Nonnull;

public final class PeaUtils {
    private PeaUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <I> void traversePeaIds(Object obj, PeaIdManipulateMethods<I> methods) {
        if (obj == null) {
            return;
        }
        try {
            for (Class<?> clazz = obj.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(PeaId.class)) {
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        if (value == null) {
                            continue;
                        }
                        Class<?> fieldType = field.getType();
                        if (fieldType.isArray()) {
                            methods.doWithArray((I[]) value);
                        } else if (List.class.isAssignableFrom(fieldType)) {
                            methods.doWithList((List<I>) value);
                        } else if (Collection.class.isAssignableFrom(fieldType)) {
                            methods.doWithCollection((Collection<I>) value);
                        } else if (Map.class.isAssignableFrom(fieldType)) {
                            methods.doWithMap((Map<Object, I>) value);
                        } else {
                            methods.doWithScalar(obj, field);
                        }
                    } else if (field.isAnnotationPresent(PeaId.InIt.class)) {
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        if (value == null) {
                            continue;
                        }
                        Class<?> fieldType = field.getType();
                        if (fieldType.isArray()) {
                            for (int i = 0; i < Array.getLength(value); i++) {
                                traversePeaIds(Array.get(value, i), methods);
                            }
                        } else if (Collection.class.isAssignableFrom(fieldType)) {
                            for (Object o : (Collection<Object>) value) {
                                traversePeaIds(o, methods);
                            }
                        } else if (Map.class.isAssignableFrom(fieldType)) {
                            for (Object o : ((Map<?, Object>) value).values()) {
                                traversePeaIds(o, methods);
                            }
                        } else {
                            traversePeaIds(value, methods);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("PANIC in PeaUtils::replacePeaIds.");
        }
    }

    public static <I> void replacePeaIds(Object obj, UnaryOperator<I> replaceFun) {
        traversePeaIds(obj, new ReplaceMethods<>(replaceFun));
    }

    public static <I> void visitPeaIds(Object obj, Consumer<I> visitFun) {
        traversePeaIds(obj, new VisitOnlyMethods<>(visitFun));
    }

    public interface PeaIdManipulateMethods<I> {
        void doWithScalar(@Nonnull Object obj, @Nonnull Field field) throws IllegalAccessException;

        void doWithArray(@Nonnull I[] value);

        void doWithList(@Nonnull List<I> value);

        void doWithCollection(@Nonnull Collection<I> value);

        void doWithMap(@Nonnull Map<Object, I> value);
    }

    @RequiredArgsConstructor
    private static class ReplaceMethods<I> implements PeaIdManipulateMethods<I> {
        private final UnaryOperator<I> replaceFun;

        @SuppressWarnings("unchecked")
        @Override
        public void doWithScalar(@Nonnull Object obj, @Nonnull Field field) throws IllegalAccessException {
            I oldValue = (I) field.get(obj);
            I newValue = replaceFun.apply(oldValue);
            if (newValue != oldValue) {
                field.set(obj, newValue);
            }
        }

        @Override
        public void doWithArray(@Nonnull I[] value) {
            for (int i = 0; i < value.length; i++) {
                I oldValue = value[i];
                I newValue = replaceFun.apply(oldValue);
                if (newValue != oldValue) {
                    value[i] = newValue;
                }
            }
        }

        @Override
        public void doWithList(@Nonnull List<I> value) {
            value.replaceAll(replaceFun);
        }

        @Override
        public void doWithCollection(@Nonnull Collection<I> value) {
            for (I oldValue : value) {
                I newValue = replaceFun.apply(oldValue);
                if (newValue != oldValue) {
                    value.remove(oldValue);
                    value.add(newValue);
                }
            }
        }

        @Override
        public void doWithMap(@Nonnull Map<Object, I> value) {
            for (Map.Entry<Object, I> entry : value.entrySet()) {
                I oldValue = entry.getValue();
                I newValue = replaceFun.apply(oldValue);
                if (newValue != oldValue) {
                    value.put(entry.getKey(), newValue);
                }
            }
        }
    }

    @RequiredArgsConstructor
    private static class VisitOnlyMethods<I> implements PeaIdManipulateMethods<I> {
        private final Consumer<I> visitFun;

        @SuppressWarnings("unchecked")
        @Override
        public void doWithScalar(@Nonnull Object obj, @Nonnull Field field) throws IllegalAccessException {
            visitFun.accept((I) field.get(obj));
        }

        @Override
        public void doWithArray(@Nonnull I[] value) {
            for (I id : value) {
                visitFun.accept(id);
            }
        }

        @Override
        public void doWithList(@Nonnull List<I> value) {
            doWithCollection(value);
        }

        @Override
        public void doWithCollection(@Nonnull Collection<I> value) {
            value.forEach(visitFun);
        }

        @Override
        public void doWithMap(@Nonnull Map<Object, I> value) {
            doWithCollection(value.values());
        }
    }
}
