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

package com.zetyun.streamtau.manager.service.impl;

import com.zetyun.streamtau.manager.db.mapper.JobMapper;
import com.zetyun.streamtau.manager.db.model.Job;
import com.zetyun.streamtau.manager.db.model.JobStatus;
import com.zetyun.streamtau.manager.pea.AssetPea;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.JobService;
import com.zetyun.streamtau.manager.service.ScheduleService;
import com.zetyun.streamtau.manager.service.dto.JobDto;
import com.zetyun.streamtau.manager.service.mapper.JobDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private AssetService assetService;
    @Autowired
    private ScheduleService scheduleService;

    @Override
    public JobDto create(Long projectId, JobDto dto) throws IOException {
        Job job = JobDtoMapper.MAPPER.toModel(dto);
        job.setProjectId(projectId);
        JobDefPod jobDefPod = assetService.synthesizeJobDef(projectId, job.getAppId());
        AssetPea app = jobDefPod.getApp();
        if (job.getJobName() == null) {
            job.setJobName(app.getName());
        }
        job.setAppType(app.getType());
        if (job.getJobStatus() == null) {
            job.setJobStatus(JobStatus.READY);
        }
        job.setJobDefinition(jobDefPod.toJobDefinition());
        job.setVersion(1);
        jobMapper.insert(job);
        if (job.getJobStatus() == JobStatus.READY) {
            scheduleService.schedule();
        }
        return JobDtoMapper.MAPPER.toDto(job);
    }
}
