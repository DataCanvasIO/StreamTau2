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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.zetyun.streamtau.core.pea.PeaId;
import com.zetyun.streamtau.streaming.exception.OperatorHasNoDependency;
import com.zetyun.streamtau.streaming.model.filter.ExprFilter;
import com.zetyun.streamtau.streaming.model.mapper.SchemaMapper;
import com.zetyun.streamtau.streaming.model.mapper.SchemaParser;
import com.zetyun.streamtau.streaming.model.mapper.SchemaStringfy;
import com.zetyun.streamtau.streaming.model.sink.PrintSink;
import com.zetyun.streamtau.streaming.model.sink.TestCollectSink;
import com.zetyun.streamtau.streaming.model.source.InPlaceSource;
import com.zetyun.streamtau.streaming.model.source.LocalFileSource;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JsonTypeInfo(property = "fid", use = JsonTypeInfo.Id.NAME, visible = true)
@JsonSubTypes({
    // Sources
    @JsonSubTypes.Type(InPlaceSource.class),
    @JsonSubTypes.Type(LocalFileSource.class),
    // Sinks
    @JsonSubTypes.Type(PrintSink.class),
    @JsonSubTypes.Type(TestCollectSink.class),
    // Mappers
    @JsonSubTypes.Type(SchemaParser.class),
    @JsonSubTypes.Type(SchemaStringfy.class),
    @JsonSubTypes.Type(SchemaMapper.class),
    // Filters
    @JsonSubTypes.Type(ExprFilter.class),
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString
@EqualsAndHashCode
public abstract class Operator {
    @JsonProperty("fid")
    @Getter
    @Setter
    private String fid;
    @JsonProperty("name")
    @Getter
    @Setter
    private String name;
    @JsonProperty("description")
    @Getter
    private String description;
    @JsonProperty("parallelism")
    @Getter
    private Integer parallelism;
    @JsonProperty("dependencies")
    @Getter
    @Setter
    private List<String> dependencies;
    @JsonProperty("schemaId")
    @Getter
    @Setter
    @PeaId
    private String schemaId;

    @JsonIgnore
    @Nullable
    public static String fid(@Nonnull Class<? extends Operator> clazz) {
        JsonTypeName name = clazz.getAnnotation(JsonTypeName.class);
        if (name != null) {
            return name.value();
        }
        return null;
    }

    @JsonIgnore
    public List<String> getValidDependencies() {
        if (dependencies == null || dependencies.isEmpty()) {
            throw new OperatorHasNoDependency(this);
        }
        return dependencies;
    }
}
