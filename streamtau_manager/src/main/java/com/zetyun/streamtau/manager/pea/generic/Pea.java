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

package com.zetyun.streamtau.manager.pea.generic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.zetyun.streamtau.manager.pea.generic.PeaUtils.searchPeaIds;

public interface Pea<I, T> {
    I getId();

    void setId(I id);

    T getType();

    default void transferAnnex() {
    }

    default Collection<I> children() {
        Set<I> set = new HashSet<>();
        searchPeaIds(
            this,
            (I o) -> {
                set.add(o);
                return true;
            },
            (Collection<I> o) -> {
                set.addAll(o);
                return true;
            }
        );
        return set;
    }

    default boolean reference(I id) {
        return searchPeaIds(
            this,
            (I o) -> !id.equals(o),
            (Collection<I> o) -> !o.contains(id)
        );
    }
}
