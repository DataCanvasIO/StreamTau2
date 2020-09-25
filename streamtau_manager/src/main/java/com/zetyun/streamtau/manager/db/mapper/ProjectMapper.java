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

package com.zetyun.streamtau.manager.db.mapper;

import com.zetyun.streamtau.manager.db.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectMapper {
    List<Project> findAll();

    Project findById(@Param("projectId") Long projectId);

    int insert(@Param("model") Project model);

    int update(@Param("model") Project model);

    int delete(@Param("projectId") Long projectId);

    List<Project> findAllForUser(@Param("userId") String userId);

    Project findByIdForUser(@Param("userId") String userId, @Param("userProjectId") String userProjectId);

    int updateForUser(@Param("userId") String userId, @Param("model") Project model);
}
