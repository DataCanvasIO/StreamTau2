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

import com.zetyun.streamtau.runtime.context.CompileContext;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class RtSchemaDict extends RtSchema {
    private static final long serialVersionUID = -1122265270144992921L;

    @Getter
    @Setter
    private Map<String, RtSchema> children;

    public RtSchemaDict() {
        super(RtSchemaTypes.DICT);
    }

    public RtSchema getChild(String name) {
        return children.get(name);
    }

    @Override
    public CompileContext getChild(int index) {
        return null;
    }

    @Override
    public int createIndex(int start) {
        setIndex(-1);
        for (RtSchema s : children.values()) {
            start = s.createIndex(start);
        }
        return start;
    }
}
