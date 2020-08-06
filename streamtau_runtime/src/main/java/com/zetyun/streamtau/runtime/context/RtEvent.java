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

package com.zetyun.streamtau.runtime.context;

import com.zetyun.streamtau.runtime.exception.NotSingleValue;

public class RtEvent implements ExecContext {
    private static final long serialVersionUID = 4296554739886078865L;

    private final Object[] indexedVars;

    public RtEvent(int numIndexedVars) {
        indexedVars = new Object[numIndexedVars];
    }

    public static RtEvent singleValue(Object value) {
        RtEvent event = new RtEvent(1);
        event.set(0, value);
        return event;
    }

    @SuppressWarnings("unchecked")
    public <T> T getSingleValue() {
        if (indexedVars.length != 1) {
            throw new NotSingleValue(this);
        }
        return (T) indexedVars[0];
    }

    @Override
    public Object get(int index) {
        return indexedVars[index];
    }

    @Override
    public void set(int index, Object value) {
        indexedVars[index] = value;
    }

    @Override
    public String toString() {
        if (indexedVars.length == 0) {
            return "(empty)";
        }
        if (indexedVars.length == 1) {
            return indexedVars[0].toString();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indexedVars.length; i++) {
            sb.append(String.format("%03d", i));
            sb.append(": ");
            sb.append(indexedVars[i]);
            sb.append("\n");
        }
        return sb.toString();
    }
}
