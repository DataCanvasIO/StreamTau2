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

package com.zetyun.streamtau.manager.junit4.mapper;

import com.zetyun.streamtau.manager.db.mapper.JobMapper;
import com.zetyun.streamtau.manager.db.model.Job;
import com.zetyun.streamtau.manager.db.model.JobStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.zetyun.streamtau.manager.helper.ResourceUtils.readObjectFromCsv;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestJobMapper {
    private static List<Job> jobs;

    @Autowired
    private JobMapper jobMapper;

    @BeforeClass
    public static void setupClass() throws IOException {
        jobs = readObjectFromCsv("/db/data/job.csv", Job.class);
    }

    @Test
    public void testFindById() {
        Job model = jobMapper.findById(1L);
        assertThat(model, is(jobs.get(0)));
    }

    @Test
    public void testFindJobOfStatus() {
        List<Job> jobList = jobMapper.findJobOfStatus(JobStatus.READY);
        assertThat(jobList, hasItems(jobs.get(0)));
    }

    @Test
    public void testFindJobOfStatusNone() {
        List<Job> jobList = jobMapper.findJobOfStatus(JobStatus.WAITING);
        assertThat(jobList, empty());
    }

    @Test
    public void testInsert() {
        Job model = new Job();
        model.setJobName("New Asset");
        model.setProjectId(2L);
        model.setAppId("060a01a7-d298-45ac-999e-ab1eb4f921b8");
        model.setAppType("CmdLine");
        model.setVersion(1);
        model.setJobDefinition("{ }");
        model.setJobStatus(JobStatus.READY);
        assertThat(jobMapper.insert(model), is(1));
        assertThat(model.getJobId(), notNullValue());
    }

    @Test
    public void testUpdateJobStatus() {
        assertThat(jobMapper.updateJobStatus(1L, JobStatus.FINISHED), is(1));
        Job model = jobMapper.findById(1L);
        assertThat(model.getJobStatus(), is(JobStatus.FINISHED));
    }

    @Configuration
    @EnableAutoConfiguration
    @MapperScans({@MapperScan("com.zetyun.streamtau.manager.db.mapper")})
    static class Config {
    }
}
