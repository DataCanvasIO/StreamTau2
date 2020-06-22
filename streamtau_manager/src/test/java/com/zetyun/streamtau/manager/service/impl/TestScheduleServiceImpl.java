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
import com.zetyun.streamtau.manager.service.ScheduleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;

import static com.zetyun.streamtau.manager.helper.ResourceUtils.readJsonCompact;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ScheduleServiceImpl.class})
public class TestScheduleServiceImpl {
    @Autowired
    private ScheduleService scheduleService;

    @MockBean
    private JobMapper jobMapper;

    @Test
    public void testSchedule() throws IOException {
        Job job = new Job();
        job.setJobId(1L);
        job.setJobName("test");
        job.setProjectId(2L);
        job.setAppType("CmdLineApp");
        job.setAppId("CMD");
        job.setVersion(1);
        job.setJobStatus(JobStatus.READY);
        job.setJobDefinition(readJsonCompact("/jobdef/cmdline/cmd_ls.json"));
        when(jobMapper.findJobOfStatus(JobStatus.READY)).thenReturn(Collections.singletonList(job));
        scheduleService.schedule();
        verify(jobMapper, times(1)).findJobOfStatus(JobStatus.READY);
        verify(jobMapper, times(1)).updateJobStatus(1L, JobStatus.SUBMITTED);
        // Not guaranteed for asynchronous running.
        // verify(jobMapper, times(1)).updateJobStatus(1L, JobStatus.FINISHED);
    }
}
