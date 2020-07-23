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
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestParsingSchemaSpec {
    @BeforeClass
    public static void setupClass() {
    }

    @Test
    public void testScalar() throws IOException {
        String str = "{type: string}";
        SchemaSpec spec = PeaParser.YAML.parse(str, SchemaSpec.class);
        assertThat(spec, instanceOf(SchemaSpecScalar.class));
        SchemaSpecScalar scalar = (SchemaSpecScalar) spec;
        assertThat(scalar.getType(), is(Types.STRING));
    }

    @Test
    public void testDict() throws IOException {
        String str = "{type: object, properties: {a: {type: integer}, b: {type: number}}}";
        SchemaSpec spec = PeaParser.YAML.parse(str, SchemaSpec.class);
        assertThat(spec, instanceOf(SchemaSpecObject.class));
        SchemaSpecObject dict = (SchemaSpecObject) spec;
        assertThat(dict.getType(), is(Types.OBJECT));
        assertThat(dict.getProperties().get("a").getType(), is(Types.INTEGER));
        assertThat(dict.getProperties().get("b").getType(), is(Types.NUMBER));
    }

    @Test
    public void testTuple() throws IOException {
        String str = "{type: array, items: [{type: integer}, {type: string}]}";
        SchemaSpec spec = PeaParser.YAML.parse(str, SchemaSpec.class);
        assertThat(spec, instanceOf(SchemaSpecArray.class));
        SchemaSpecArray array = (SchemaSpecArray) spec;
        assertThat(array.getType(), is(Types.ARRAY));
        assertThat(array.getItems().getSpecs()[0].getType(), is(Types.INTEGER));
        assertThat(array.getItems().getSpecs()[1].getType(), is(Types.STRING));
    }
}
