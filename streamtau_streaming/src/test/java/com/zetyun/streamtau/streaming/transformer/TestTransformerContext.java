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

package com.zetyun.streamtau.streaming.transformer;

import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.streaming.model.Dag;
import com.zetyun.streamtau.streaming.model.TestDag;
import com.zetyun.streamtau.streaming.model.sink.TestCollectSink;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestTransformerContext {
    @ClassRule
    public static MiniClusterWithClientResource cluster =
        new MiniClusterWithClientResource(
            new MiniClusterResourceConfiguration.Builder()
                .setNumberSlotsPerTaskManager(2)
                .setNumberTaskManagers(1)
                .build()
        );

    @Test
    public void testTransform() throws Exception {
        Dag dag = PeaParser.JSON.parse(
            TestDag.class.getResourceAsStream("/dag/in-place-collect.json"),
            Dag.class
        );
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        TransformerContext.transform(env, dag);
        TestCollectSink.Runtime.clear();
        env.execute();
        List<Integer> values = TestCollectSink.Runtime.getValues().stream()
            .map(x -> (Integer) ((RtEvent) x).getSingleValue())
            .collect(Collectors.toList());
        assertThat(values, is(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }
}
