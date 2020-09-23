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

import com.zetyun.streamtau.manager.properties.StreamingProperties;
import com.zetyun.streamtau.manager.service.ServerService;
import com.zetyun.streamtau.manager.service.StorageService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;

@Configuration
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext ctx) throws BeansException {
        context = ctx;
    }

    @Nonnull
    public static StorageService getStorageService() {
        return context.getBean(StorageService.class);
    }

    @Nonnull
    public static ServerService getServerService() {
        return context.getBean(ServerService.class);
    }

    @Nonnull
    public static StreamingProperties getStreamingProperties() {
        return context.getBean(StreamingProperties.class);
    }
}
