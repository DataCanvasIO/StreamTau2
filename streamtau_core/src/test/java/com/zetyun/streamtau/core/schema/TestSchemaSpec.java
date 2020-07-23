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

package com.zetyun.streamtau.core.schema;

import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.runtime.schema.RtSchema;
import com.zetyun.streamtau.runtime.schema.RtSchemaDict;
import com.zetyun.streamtau.runtime.schema.RtSchemaNode;
import com.zetyun.streamtau.runtime.schema.RtSchemaTuple;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestSchemaSpec {
    @BeforeClass
    public static void setupClass() {
    }

    @Test
    public void testCreateIndex() throws IOException {
        SchemaSpec spec = PeaParser.YAML.parse(
            TestSchemaSpec.class.getResourceAsStream("/schema/example.yml"),
            SchemaSpec.class
        );
        RtSchema schema = new RtSchema(spec.createRtNode());
        assertThat(schema.getMaxIndex(), is(3));
        assertThat(schema.getSchema(), instanceOf(RtSchemaDict.class));
        RtSchemaDict dict = (RtSchemaDict) schema.getSchema();
        assertThat(dict.getIndex(), is(-1));
        RtSchemaNode a = dict.getChild("a");
        assertThat(a.getIndex(), is(0));
        RtSchemaNode b = dict.getChild("b");
        assertThat(b, instanceOf(RtSchemaTuple.class));
        assertThat(b.getIndex(), is(-1));
        RtSchemaTuple tuple = (RtSchemaTuple) b;
        assertThat(tuple.getChildren()[0].getIndex(), is(1));
        assertThat(tuple.getChildren()[1].getIndex(), is(2));
    }
}
