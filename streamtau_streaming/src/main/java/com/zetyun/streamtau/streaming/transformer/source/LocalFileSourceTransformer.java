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

import com.zetyun.streamtau.streaming.model.Operator;
import com.zetyun.streamtau.streaming.model.source.LocalFileSource;
import com.zetyun.streamtau.streaming.transformer.TransformContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStreamSource;

import javax.annotation.Nonnull;

@Slf4j
public class LocalFileSourceTransformer implements GeneralSourceTransformer {
    @Nonnull
    @Override
    public DataStreamSource<?> transformSource(
        @Nonnull Operator operator,
        @Nonnull TransformContext context
    ) {
        if (log.isInfoEnabled()) {
            log.info("Current working directory: {}", System.getProperty("user.dir"));
        }
        return context.getEnv()
            .readTextFile(((LocalFileSource) operator).getPath());
    }
}
