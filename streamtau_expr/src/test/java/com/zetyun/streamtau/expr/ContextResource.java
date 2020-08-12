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

package com.zetyun.streamtau.expr;

import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.core.schema.SchemaSpec;
import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.runtime.schema.RtSchema;
import com.zetyun.streamtau.runtime.schema.RtSchemaParser;
import com.zetyun.streamtau.runtime.schema.RtSchemaRoot;
import lombok.Getter;
import org.junit.rules.ExternalResource;

public class ContextResource extends ExternalResource {
    private final String ctxFileName;
    private final String[] etxStrings;

    @Getter
    private RtSchemaRoot schema;
    private RtEvent[] events;

    public ContextResource(String ctxFileName, String... etxStrings) {
        this.ctxFileName = ctxFileName;
        this.etxStrings = etxStrings;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        SchemaSpec spec = PeaParser.YAML.parse(
            ContextResource.class.getResourceAsStream(ctxFileName),
            SchemaSpec.class
        );
        schema = new RtSchemaRoot(spec.createRtSchema());
        RtSchemaParser parser = RtSchemaParser.createYamlEventParser(schema);
        events = new RtEvent[etxStrings.length];
        for (int i = 0; i < events.length; i++) {
            events[i] = parser.parse(etxStrings[i]);
        }
    }

    public RtSchema getCtx() {
        return schema.getRoot();
    }

    public RtEvent getEtx(int index) {
        return events[index];
    }
}
