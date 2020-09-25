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
import com.zetyun.streamtau.manager.runner.RunnerFactory;
import com.zetyun.streamtau.manager.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private JobMapper jobMapper;

    @Override
    public void schedule() throws IOException {
        List<Job> jobs = jobMapper.findByStatus(JobStatus.READY);
        for (Job job : jobs) {
            RunnerFactory.get().run(job, () -> jobMapper.updateStatus(job.getJobId(), JobStatus.FINISHED));
            jobMapper.updateStatus(job.getJobId(), JobStatus.SUBMITTED);
        }
    }
}
