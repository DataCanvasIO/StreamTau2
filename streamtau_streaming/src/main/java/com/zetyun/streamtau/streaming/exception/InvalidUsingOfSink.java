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

import com.zetyun.streamtau.streaming.transformer.node.DataStreamSinkNode;
import lombok.Getter;

public class InvalidUsingOfSink extends RuntimeException {
    private static final long serialVersionUID = 5830188353617036177L;

    @Getter
    private final DataStreamSinkNode node;
    @Getter
    private final String reason;

    public InvalidUsingOfSink(DataStreamSinkNode node, String reason) {
        super(
            "Invalid use of sink \"" + node.getName() + "\": " + reason + "."
        );
        this.node = node;
        this.reason = reason;
    }
}
