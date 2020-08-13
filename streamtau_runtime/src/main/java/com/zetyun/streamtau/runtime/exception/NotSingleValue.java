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

import com.zetyun.streamtau.runtime.context.RtEvent;
import lombok.Getter;

import javax.annotation.Nonnull;

public class NotSingleValue extends RuntimeException {
    private static final long serialVersionUID = -3358746522758589369L;

    @Getter
    private final RtEvent event;

    public NotSingleValue(@Nonnull RtEvent event) {
        super(
            "Event is not a single value:\n" + event
        );
        this.event = event;
    }
}
