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

package com.zetyun.streamtau.manager.junit4.service.impl;

import com.zetyun.streamtau.manager.db.mapper.JobMapper;
import com.zetyun.streamtau.manager.db.model.Job;
import com.zetyun.streamtau.manager.db.model.JobStatus;
import com.zetyun.streamtau.manager.pea.JobDefPod;
import com.zetyun.streamtau.manager.pea.app.CmdLineApp;
import com.zetyun.streamtau.manager.service.AssetService;
import com.zetyun.streamtau.manager.service.JobService;
import com.zetyun.streamtau.manager.service.ProjectService;
import com.zetyun.streamtau.manager.service.ScheduleService;
import com.zetyun.streamtau.manager.service.dto.JobDto;
import com.zetyun.streamtau.manager.service.impl.JobServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JobServiceImpl.class})
public class TestJobServiceImpl {
    @Autowired
    private JobService jobService;

    @MockBean
    private JobMapper jobMapper;
    @MockBean
    private AssetService assetService;
    @MockBean
    private ProjectService projectService;
    @MockBean
    private ScheduleService scheduleService;

    @Test
    public void testCreate() throws IOException {
        when(projectService.mapProjectId("PRJ")).thenReturn(2L);
        when(assetService.synthesizeJobDef(eq(2L), anyString())).then(args -> {
            String appId = args.getArgument(1);
            JobDefPod pod = new JobDefPod(appId);
            CmdLineApp app = new CmdLineApp();
            app.setId(appId);
            pod.save(app);
            return pod;
        });
        when(jobMapper.insert(any(Job.class))).then(args -> {
            Job model = args.getArgument(0);
            model.setJobId(1L);
            return 1;
        });
        JobDto dto = new JobDto();
        dto.setName("forCreate");
        dto.setAppId("AAA");
        dto.setJobStatus(JobStatus.READY);
        dto = jobService.create("PRJ", dto);
        assertThat(dto.getId(), is(1L));
        assertThat(dto.getName(), is("forCreate"));
        assertThat(dto.getAppType(), is("CmdLineApp"));
        verify(projectService, times(1)).mapProjectId("PRJ");
        verify(assetService, times(1)).synthesizeJobDef(2L, "AAA");
        verify(jobMapper, times(1)).insert(any(Job.class));
    }
}
