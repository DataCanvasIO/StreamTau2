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

import com.zetyun.streamtau.manager.db.model.UserProject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestUserProjectMapper {
    private static List<UserProject> userProjects;

    @Autowired
    private UserProjectMapper userProjectMapper;

    @BeforeClass
    public static void setupClass() throws IOException {
        userProjects = readObjectFromCsv("/db/data/user_project.csv", UserProject.class);
    }

    @Test
    public void testFindAll() {
        List<UserProject> modelList = userProjectMapper.findAll();
        assertThat(modelList.size(), is(userProjects.size()));
        for (UserProject userProject : userProjects) {
            assertThat(modelList, hasItem(userProject));
        }
    }

    @Test
    public void testAddToUser() {
        UserProject model = new UserProject("user1", 3L, null);
        assertThat(userProjectMapper.addToUser(model), is(1));
        assertThat(model.getUserProjectId(), notNullValue());
    }

    @Test
    public void testDeleteFromUser() {
        UserProject model = new UserProject("user1", null, "14b96595-f7f1-4800-a98a-c3d44d9c7e03");
        assertThat(userProjectMapper.deleteFromUser(model), is(1));
    }

    @Test
    public void testDeleteFromUserNotOwn() {
        UserProject model = new UserProject("user1", null, "62f6fe03-520f-4a57-972f-f3f849aab6b8");
        assertThat(userProjectMapper.deleteFromUser(model), is(0));
    }

    @Configuration
    @EnableAutoConfiguration
    static class Config {
    }
}
