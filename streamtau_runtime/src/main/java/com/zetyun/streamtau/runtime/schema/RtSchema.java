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
import org.jetbrains.annotations.NotNull;

import static com.zetyun.streamtau.runtime.Constants.ROOT_VAR_NAME;

public class RtSchema implements CompileContext {
    private static final long serialVersionUID = 4363200842331592676L;

    @Getter
    private final RtSchemaNode schema;
    @Getter
    private final int maxIndex;

    public RtSchema(RtSchemaNode schema) {
        this.schema = schema;
        maxIndex = createIndex(schema, 0);
    }

    private int createIndex(@NotNull RtSchemaNode schema, int start) {
        switch (schema.getType()) {
            case DICT:
                RtSchemaDict obj = (RtSchemaDict) schema;
                obj.setIndex(-1);
                for (RtSchemaNode s : obj.getChildren().values()) {
                    start = createIndex(s, start);
                }
                break;
            case TUPLE:
                RtSchemaTuple tuple = (RtSchemaTuple) schema;
                tuple.setIndex(-1);
                for (RtSchemaNode s : tuple.getChildren()) {
                    start = createIndex(s, start);
                }
                break;
            default:
                schema.setIndex(start++);
        }
        return start;
    }

    @Override
    public int getIndex(@NotNull String name) {
        if (name.equals(ROOT_VAR_NAME)) {
            return schema.getIndex();
        }
        if (schema instanceof RtSchemaDict) {
            RtSchemaDict dict = (RtSchemaDict) schema;
            return dict.getChild(name).getIndex();
        }
        return -1;
    }

    @Override
    public Class<?> get(@NotNull String name) {
        if (name.equals(ROOT_VAR_NAME)) {
            return schema.getJavaClass();
        }
        if (schema instanceof RtSchemaDict) {
            RtSchemaDict dict = (RtSchemaDict) schema;
            return dict.getChild(name).getJavaClass();
        }
        return null;
    }
}
