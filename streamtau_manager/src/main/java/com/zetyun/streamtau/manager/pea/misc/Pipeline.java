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

package com.zetyun.streamtau.manager.pea.misc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.zetyun.streamtau.core.pea.PeaId;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.streaming.model.Operator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@JsonTypeName("Pipeline")
@EqualsAndHashCode(callSuper = true)
public class Pipeline extends AssetPea {
    @Schema(
        description = "Specify the pipeline of a stream app.",
        example = "{\"A\": {\"fid\": \"source\"}, \"B\": {\"fid\": \"sink\", \"dependencies\": [\"A\"]}}"
    )
    @JsonProperty("operators")
    @Getter
    @Setter
    @PeaId.InIt
    private Map<String, Operator> operators;
}
