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
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class RtIndexedVar extends RtVar {
    private static final long serialVersionUID = -1139156670924180115L;

    private final int index;

    @Override
    public Object eval(@NotNull ExecContext etx) {
        return etx.get(index);
    }

    @Override
    public void set(@NotNull ExecContext etx, Object value) {
        etx.set(index, value);
    }
}
