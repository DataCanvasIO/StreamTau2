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

package com.zetyun.streamtau.streaming.exception;

import com.zetyun.streamtau.streaming.model.Operator;
import lombok.Getter;

import javax.annotation.Nonnull;

public class OperatorHasNoDependency extends RuntimeException {
    private static final long serialVersionUID = -1071379778626087571L;

    @Getter
    private final Operator operator;

    public OperatorHasNoDependency(@Nonnull Operator operator) {
        super(
            "Cannot get dependencies of operator \"" + operator.getName() + "\" which has none."
        );
        this.operator = operator;
    }
}
