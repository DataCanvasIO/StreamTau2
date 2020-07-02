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

package com.zetyun.streamtau.manager.junit4.pea.file;

import com.zetyun.streamtau.manager.pea.PeaParser;
import com.zetyun.streamtau.manager.pea.file.File;
import com.zetyun.streamtau.manager.pea.file.TxtFile;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestFile {
    @Test
    public void testTypeFromName() {
        assertThat(File.typeFromName("a.txt"), is("TxtFile"));
        assertThat(File.typeFromName("b.jar"), is("JarFile"));
    }

    @Test
    public void testSerialize() throws IOException {
        File file = new TxtFile();
        file.setName("text");
        file.setPath("AAA.txt");
        String json = PeaParser.JSON.stringShowAll(file);
        assertThat(json, is("{\"type\":\"TxtFile\",\"name\":\"text\",\"path\":\"AAA.txt\"}"));
    }
}
