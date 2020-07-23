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

package com.zetyun.streamtau.expr.runtime.var;

import com.zetyun.streamtau.runtime.context.ExecContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RtNamedVar extends RtVar {
    private static final long serialVersionUID = -3022297703503518040L;

    private final String name;

    @Override
    public Object eval(ExecContext etx) {
        // TODO
        return null;
    }

    @Override
    public void set(ExecContext etx, Object value) {
    }
}
