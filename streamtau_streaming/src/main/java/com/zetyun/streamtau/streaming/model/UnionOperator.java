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

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.zetyun.streamtau.streaming.transformer.node.UnionNodeId;
import lombok.ToString;

import javax.annotation.Nonnull;

@JsonTypeName("internal.union")
@ToString(callSuper = true)
public class UnionOperator extends Operator {
    public UnionOperator(@Nonnull UnionNodeId nodeId) {
        super();
        setFid("internal.union");
        setDependencies(nodeId.getIdList());
    }
}
