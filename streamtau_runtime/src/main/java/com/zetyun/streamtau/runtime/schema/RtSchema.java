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

package com.zetyun.streamtau.runtime.schema;

import com.zetyun.streamtau.runtime.context.CompileContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class RtSchema implements CompileContext {
    private static final long serialVersionUID = -3071467129758460657L;

    @Getter
    private final RtSchemaTypes type;
    @Getter
    @Setter
    private int index;

    @Override
    public Class<?> getJavaClass() {
        return type.getJavaClass();
    }

    @Override
    public CompileContext getChild(String name) {
        return null;
    }

    @Override
    public CompileContext getChild(int index) {
        return null;
    }

    public int createIndex(int start) {
        index = start++;
        return start;
    }
}
