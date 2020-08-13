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

import javax.annotation.Nullable;

public class RtSchemaSingle extends RtSchema {
    private static final long serialVersionUID = 8381945283977704333L;

    public RtSchemaSingle(RtSchemaTypes type) {
        super(type);
    }

    @Nullable
    @Override
    public CompileContext getChild(String name) {
        return null;
    }

    @Nullable
    @Override
    public CompileContext getChild(int index) {
        return null;
    }

    @Override
    public int createIndex(int start) {
        setIndex(start++);
        return start;
    }
}
