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

package com.zetyun.streamtau.manager.controller.protocol;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import javax.annotation.Nonnull;

public class ResponseDataSerializer extends StdSerializer<Object> {
    private static final long serialVersionUID = -9199555910614396321L;

    protected ResponseDataSerializer() {
        super(Object.class);
    }

    // Fix type id missing induced by type erasure.
    @Override
    public void serialize(Object obj, JsonGenerator gen, @Nonnull SerializerProvider provider) throws IOException {
        if (obj instanceof Iterable) {
            gen.writeStartArray();
            for (Object item : (Iterable<?>) obj) {
                provider.defaultSerializeValue(item, gen);
            }
            gen.writeEndArray();
        } else {
            provider.defaultSerializeValue(obj, gen);
        }
    }
}
