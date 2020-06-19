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

import com.zetyun.streamtau.manager.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@ConditionalOnProperty(name = "file.storage.type", havingValue = "local", matchIfMissing = true)
@EnableConfigurationProperties(LocalFileStorageProperties.class)
public class LocalFileStorageService implements StorageService {
    private final Path root;

    @Autowired
    public LocalFileStorageService(LocalFileStorageProperties properties) {
        String dir = properties.getDir();
        if (dir == null || dir.isEmpty()) {
            root = Paths.get(System.getProperty("java.io.tmpdir"), "StreamTau");
        } else {
            root = Paths.get(dir).toAbsolutePath();
        }
    }

    @Override
    public String createFile(String extension) throws IOException {
        if (!Files.isDirectory(root)) {
            Files.createDirectory(root);
        }
        Path path = Files.createTempFile(root, "STA_", "." + extension);
        return path.toUri().toString();
    }

    @Override
    public void saveFile(String uri, MultipartFile file) throws IOException {
        file.transferTo(Paths.get(URI.create(uri)));
    }
}
