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

package com.zetyun.streamtau.streaming.model;

import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.streaming.model.sink.TestCollectSink;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestDag {
    @Test
    public void testReadDag() throws IOException {
        Dag dag = PeaParser.JSON.parse(
            TestDag.class.getResourceAsStream("/dag/in-place-collect.json"),
            Dag.class
        );
        assertThat(dag.getOperators().size(), is(2));
        assertThat(dag.getSinks().get(0).getFid(), is(Operator.fid(TestCollectSink.class)));
    }
}
