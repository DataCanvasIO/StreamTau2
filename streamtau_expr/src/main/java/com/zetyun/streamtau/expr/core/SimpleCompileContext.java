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

package com.zetyun.streamtau.expr.core;

import com.zetyun.streamtau.expr.runtime.context.ExecContext;
import com.zetyun.streamtau.expr.runtime.context.SimpleExecContext;
import org.apache.commons.configuration2.Configuration;

import java.util.HashMap;
import java.util.Map;

public abstract class SimpleCompileContext implements CompileContext {
    protected final Map<String, Class<?>> typeMap = new HashMap<>();
    protected final Map<String, Integer> indexMap = new HashMap<>();

    public SimpleCompileContext() {
        init();
    }

    protected abstract void init();

    @Override
    public int getIndex(String name) {
        Integer index = indexMap.get(name);
        return index != null ? index : -1;
    }

    @Override
    public Class<?> get(String name) {
        return typeMap.get(name);
    }

    @Override
    public Configuration getConf() {
        return null;
    }

    @Override
    public ExecContext createExecContext() {
        SimpleExecContext etx = new SimpleExecContext(indexMap.size());
        return etx;
    }
}
