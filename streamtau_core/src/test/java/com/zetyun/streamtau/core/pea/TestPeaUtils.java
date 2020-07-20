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

package com.zetyun.streamtau.core.pea;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.zetyun.streamtau.core.pea.PeaUtils.collectPeaIds;
import static com.zetyun.streamtau.core.pea.PeaUtils.replacePeaIds;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestPeaUtils {
    @Before
    public void setup() {
    }

    @Test
    public void testCollectPeaIdsSimple() {
        ClassA objA = new ClassA();
        objA.setPea1(1);
        objA.setPea2(2);
        Set<Integer> set = new HashSet<>();
        collectPeaIds(set, objA);
        assertThat(set, hasItems(1, 2));
    }

    @Test
    public void testCollectPeaIdsInherit() {
        ClassB objB = new ClassB();
        objB.setPea1(1);
        objB.setPea2(2);
        objB.setPea3(3);
        Set<Integer> set = new HashSet<>();
        collectPeaIds(set, objB);
        assertThat(set, hasItems(1, 2, 3));
    }

    @Test
    public void testCollectPeaIdsArray() {
        ClassC objC = new ClassC();
        objC.setPeaArray(new int[]{3, 4, 5, 6});
        Set<Integer> set = new HashSet<>();
        collectPeaIds(set, objC);
        assertThat(set, hasItems(3, 4, 5, 6));
    }

    @Test
    public void testCollectPeaIdsCollection() {
        ClassD objD = new ClassD();
        objD.setPeaList(Arrays.asList(3, 4, 5, 6));
        Set<Integer> set = new HashSet<>();
        collectPeaIds(set, objD);
        assertThat(set, hasItems(3, 4, 5, 6));
    }

    @Test
    public void testCollectPeaIdsMap() {
        ClassE objE = new ClassE();
        objE.setPeaMap(ImmutableMap.<String, Integer>builder()
            .put("a", 3)
            .put("b", 4)
            .put("c", 5)
            .put("d", 6)
            .build());
        Set<Integer> set = new HashSet<>();
        collectPeaIds(set, objE);
        assertThat(set, hasItems(3, 4, 5, 6));
    }

    @Test
    public void testCollectPeaIdsIndirect() {
        ClassF objF = new ClassF();
        Media media = new Media();
        media.setPea3(3);
        objF.setMedia(media);
        Set<Integer> set = new HashSet<>();
        collectPeaIds(set, objF);
        assertThat(set, hasItems(3));
    }

    @Test
    public void testReplacePeaIdsInherit() {
        ClassB objB = new ClassB();
        objB.setPea1(1);
        objB.setPea2(2);
        objB.setPea3(3);
        replacePeaIds(objB, (Integer x) -> x + 3);
        assertThat(objB.getPea1(), is(4));
        assertThat(objB.getPea2(), is(5));
        assertThat(objB.getPea3(), is(6));
    }

    static class ClassA {
        @Getter
        @Setter
        @PeaId
        protected int pea1;
        @Getter
        @Setter
        @PeaId
        protected int pea2;
    }

    static class ClassB extends ClassA {
        @Getter
        @Setter
        @PeaId
        private int pea3;
    }

    static class ClassC {
        @Setter
        @PeaId
        private int[] peaArray;
    }

    static class ClassD {
        @Setter
        @PeaId
        private List<Integer> peaList;
    }

    static class ClassE {
        @Setter
        @PeaId
        private Map<String, Integer> peaMap;
    }

    static class Media {
        @Setter
        @PeaId
        private int pea3;
    }

    static class ClassF {
        @Setter
        @PeaId.InIt
        private Media media;
    }
}
