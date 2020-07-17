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

package com.zetyun.streamtau.expr.runtime.context;

import java.util.HashMap;
import java.util.Map;

public class SimpleExecContext implements ExecContext {
    private static final long serialVersionUID = 4296554739886078865L;

    private final Object[] indexedVars;
    private final Map<String, Object> namedVars;

    public SimpleExecContext(int numIndexedVars) {
        indexedVars = new Object[numIndexedVars];
        namedVars = new HashMap<>();
    }

    @Override
    public Object getIndexed(int index) {
        return indexedVars[index];
    }

    @Override
    public void setIndexed(int index, Object value) {
        indexedVars[index] = value;
    }

    @Override
    public Object getNamed(String name) {
        return namedVars.get(name);
    }

    @Override
    public void setNamed(String name, Object value) {
        namedVars.put(name, value);
    }
}
