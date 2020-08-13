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

package com.zetyun.streamtau.streaming.transformer.source;

import com.zetyun.streamtau.runtime.context.RtEvent;
import com.zetyun.streamtau.streaming.exception.InvalidOperatorParameter;
import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.model.source.LocalFileSource;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import com.zetyun.streamtau.streaming.transformer.Transformer;
import com.zetyun.streamtau.streaming.transformer.node.StreamNode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.Nonnull;

public class LocalFileSourceTransformer implements Transformer {
    @Nonnull
    private URL getFileUrl(@Nonnull LocalFileSource operator) {
        String path = operator.getPath();
        URL url;
        if (path.startsWith("classpath://")) {
            url = getClass().getResource(path.substring("classpath://".length()));
        } else if (path.startsWith("classpath:")) {
            url = getClass().getResource(path.substring("classpath:".length()));
        } else {
            try {
                url = new URL(path);
            } catch (MalformedURLException e) {
                throw new InvalidOperatorParameter(operator, "path", path);
            }
        }
        return url;
    }

    @Nonnull
    @Override
    public StreamNode transform(@Nonnull Operator operator, @Nonnull TransformContext context) {
        DataStreamSource<String> stream0 = context.getEnv()
            .readTextFile(getFileUrl((LocalFileSource) operator).toString());
        Integer parallelism = operator.getParallelism();
        if (parallelism != null) {
            stream0.setParallelism(parallelism);
        }
        SingleOutputStreamOperator<RtEvent> stream = stream0.map(RtEvent::singleValue);
        if (parallelism != null) {
            stream.setParallelism(parallelism);
        }
        return StreamNode.of(stream);
    }
}
