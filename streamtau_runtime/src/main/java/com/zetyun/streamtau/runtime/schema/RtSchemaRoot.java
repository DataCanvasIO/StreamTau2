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

import lombok.Getter;

import java.io.Serializable;
import javax.annotation.Nonnull;

public class RtSchemaRoot implements Serializable {
    private static final long serialVersionUID = -5075934637295240308L;

    @Getter
    private final RtSchema root;
    @Getter
    private final int maxIndex;

    public RtSchemaRoot(@Nonnull RtSchema root) {
        this.root = root;
        this.maxIndex = root.createIndex(0);
    }
}
