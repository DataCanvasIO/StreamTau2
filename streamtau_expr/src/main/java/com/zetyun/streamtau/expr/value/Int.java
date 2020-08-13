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

package com.zetyun.streamtau.expr.value;

import com.zetyun.streamtau.runtime.context.CompileContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class Int extends Value {
    @Getter
    private final Long value;

    @Nonnull
    public static Int fromString(String text) {
        return new Int(Long.parseLong(text));
    }

    @Nonnull
    @Override
    public Class<?> calcType(CompileContext ctx) {
        return Long.class;
    }
}
