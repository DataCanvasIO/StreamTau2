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

package com.zetyun.streamtau.manager.boot;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.Nonnull;

@SpringBootApplication(scanBasePackages = {
    "com.zetyun.streamtau.manager",
    "com.zetyun.streamtau.frontend",
})
@MapperScans({@MapperScan("com.zetyun.streamtau.manager.db.mapper")})
@Slf4j
public class WebAppInitializer extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(WebAppInitializer.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(@Nonnull SpringApplicationBuilder builder) {
        return builder.sources(WebAppInitializer.class);
    }
}
