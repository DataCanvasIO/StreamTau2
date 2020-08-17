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

import com.zetyun.streamtau.core.pod.Pod;

import javax.annotation.Nonnull;

public interface Pea<I, T, P extends Pea<I, T, P>> {
    I getId();

    void setId(I id);

    T getType();

    @SuppressWarnings("unchecked")
    default I transfer(
        @Nonnull Pod<I, T, P> pod0,
        @Nonnull Pod<I, T, P> pod1
    ) {
        PeaUtils.replacePeaIds(this, (I i) -> pod0.transfer(i, pod1));
        return pod1.save((P) this);
    }
}
