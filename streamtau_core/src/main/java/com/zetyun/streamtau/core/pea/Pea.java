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

package com.zetyun.streamtau.core.pea;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface Pea<I, T> {
    I getId();

    void setId(I id);

    T getType();

    default Collection<I> children() {
        Set<I> set = new HashSet<>();
        PeaUtils.collectPeaIds(set, this);
        return set;
    }

    default boolean reference(I id) {
        return children().contains(id);
    }

    default void transferAnnex() {
    }
}
