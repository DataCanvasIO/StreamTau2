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

package com.zetyun.streamtau.streaming.run;

import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.streaming.runtime.sink.TestCollectSinkFunction;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@FixMethodOrder()
public class TestRun {
    @ClassRule
    public static MiniClusterWithClientResource cluster =
        new MiniClusterWithClientResource(
            new MiniClusterResourceConfiguration.Builder()
                .setNumberSlotsPerTaskManager(2)
                .setNumberTaskManagers(1)
                .build()
        );

    @Before
    public void setup() {
        TestCollectSinkFunction.clear();
    }

    private void runCase(String pipelineFile) throws Exception {
        URL res = getClass().getResource("/dag/");
        DagFilePod filePod = new DagFilePod(res.toString(), pipelineFile);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        TransformContext.transform(env, filePod);
        env.execute();
    }

    private void checkCollectSinkAgainstFile(String fileName) throws URISyntaxException, IOException {
        Stream<String> lines = Files.lines(Paths.get(getClass().getResource(fileName).toURI()));
        List<RtEvent> values = TestCollectSinkFunction.getValues();
        Iterator<RtEvent> eit = values.stream().iterator();
        Iterator<String> sit = lines.iterator();
        while (eit.hasNext() && sit.hasNext()) {
            RtEvent event = eit.next();
            String line = sit.next();
            assertThat(event.getSingleValue(), is(line));
        }
        if (eit.hasNext()) {
            fail("More items in collect sink.");
        }
        if (sit.hasNext()) {
            fail("More items in file.");
        }
    }

    @Test
    public void testInPlaceCollect() throws Exception {
        runCase("in-place-collect.json");
        List<Object> values = TestCollectSinkFunction.getValues().stream()
            .map(x -> (Integer) x.getSingleValue())
            .collect(Collectors.toList());
        assertThat(values, is(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }

    @Test
    public void testSchemaParser() throws Exception {
        runCase("schema-parser.yml");
        checkCollectSinkAgainstFile("/result/name-gender-scores.json.txt");
    }

    @Test
    public void testSchemaStringfy() throws Exception {
        runCase("schema-stringfy.yml");
        List<RtEvent> values = TestCollectSinkFunction.getValues();
        assertThat(values.get(0).getSingleValue(),
            is("---\ngender: F\nname: Alice\nscores:\n  english: 80\n  maths: 100\n"));
    }

    @Test
    public void testSchemaMapper() throws Exception {
        runCase("schema-mapper.yml");
        checkCollectSinkAgainstFile("/result/name-gender-total-score.json.txt");
    }

    @Test
    public void testSchemaMapperUnion() throws Exception {
        runCase("schema-mapper-union.yml");
        checkCollectSinkAgainstFile("/result/name-gender-selected-score.json.txt");
    }

    @Test
    public void testExprFilter() throws Exception {
        runCase("expr-filter.yml");
        checkCollectSinkAgainstFile("/result/name-gender-scores-filtered.json.txt");
    }
}
