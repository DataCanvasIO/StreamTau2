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

package com.zetyun.streamtau.expr.exception;

import lombok.Getter;

public class InvalidIndexValue extends RuntimeException {
    private static final long serialVersionUID = 5206879022237837242L;

    @Getter
    private final Object index;

    public InvalidIndexValue(Object index) {
        super(
            "The index value must be an integer/string const in a tuple/dict, but is \"" + index + "\"."
        );
        this.index = index;
    }
}
