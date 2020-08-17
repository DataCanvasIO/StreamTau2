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

package com.zetyun.streamtau.core.pod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zetyun.streamtau.core.pea.Pea;
import com.zetyun.streamtau.core.pea.PeaId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class TestFilePod {
    private FilePod<String, FilePea> filePod;

    @Before
    public void setup() {
        URL res = TestFilePod.class.getResource("/pod/");
        filePod = Mockito.spy(new FilePod<>(res.toString(), FilePea.class));
    }

    @Test
    public void testLoad() throws IOException {
        FilePea pea = filePod.load("first.json");
        assertThat(pea.getId(), is("first.json"));
        assertThat(pea.getType(), is("FilePea"));
        assertThat(pea.getName(), is("The first file pea"));
        assertThat(Arrays.asList(pea.getChildren()), hasItems("second.yml", "third.yml"));
        verify(filePod, times(1)).load("first.json");
    }

    @Test
    public void testTransfer() throws IOException {
        MapPod<String, String, FilePea> mapPod = new MapPod<>();
        filePod.transfer("first.json", mapPod);
        verify(filePod, times(1)).load("first.json");
        verify(filePod, times(1)).load("second.yml");
        verify(filePod, times(1)).load("third.yml");
        verify(filePod, times(3)).transfer(anyString(), eq(mapPod));
        assertThat(mapPod.peaMap.size(), is(3));
        assertThat(mapPod.peaMap.keySet(), hasItems("first.json", "second.yml", "third.yml"));
    }

    @ToString
    public static class FilePea implements Pea<String, String, FilePea> {
        @Getter
        @Setter
        private String id;
        @JsonProperty("type")
        @Getter
        private String type;
        @JsonProperty("name")
        @Getter
        private String name;
        @PeaId
        @Setter
        @Getter
        private String[] children;
    }
}
