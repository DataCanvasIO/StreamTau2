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

package com.zetyun.streamtau.runtime.exception;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import javax.annotation.Nonnull;

public class MissingRequiredItem extends RuntimeException {
    private static final long serialVersionUID = 903768026611818763L;

    @Getter
    private final JsonNode jsonNode;
    @Getter
    private final int index;

    public MissingRequiredItem(@Nonnull JsonNode jsonNode, int index) {
        super(
            "Missing required item at index \"" + index + "\" in json node: " + jsonNode
        );
        this.jsonNode = jsonNode;
        this.index = index;
    }
}
