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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public final class PeaClassUtils {
    private PeaClassUtils() {
    }

    public static void visitClassPeaIds(Class<?> clazz, Consumer<Field> visitFun) {
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(PeaId.class)) {
                    visitFun.accept(field);
                } else if (field.isAnnotationPresent(PeaId.InIt.class)) {
                    Class<?> fieldType = field.getType();
                    if (fieldType.isArray()) {
                        visitClassPeaIds(fieldType.getComponentType(), visitFun);
                    } else if (Collection.class.isAssignableFrom(fieldType)) {
                        Type type = field.getGenericType();
                        if (type instanceof ParameterizedType) {
                            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
                            if (typeArguments.length > 0) {
                                Type typeArgument = typeArguments[0];
                                if (typeArgument instanceof Class) {
                                    visitClassPeaIds((Class<?>) typeArgument, visitFun);
                                }
                            }
                        }
                    } else if (Map.class.isAssignableFrom(fieldType)) {
                        Type type = field.getGenericType();
                        if (type instanceof ParameterizedType) {
                            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
                            if (typeArguments.length > 1) {
                                Type typeArgument = typeArguments[1];
                                if (typeArgument instanceof Class) {
                                    visitClassPeaIds((Class<?>) typeArgument, visitFun);
                                }
                            }
                        }
                    } else {
                        visitClassPeaIds(fieldType, visitFun);
                    }
                }
            }
        }
    }
}
