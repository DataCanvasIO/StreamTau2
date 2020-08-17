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

import com.google.common.collect.ImmutableMap;
import com.zetyun.streamtau.core.pea.Pea;
import com.zetyun.streamtau.core.pea.PeaId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

public class TestPod {
    private static MapPod<Integer, String, TestPea> mapPod;

    @BeforeClass
    public static void setupClass() {
        mapPod = new MapPod<>();
        mapPod.save(new TestPea(1));
        mapPod.save(new TestPea(2));
        mapPod.save(new TestPea(3));
        mapPod.save(new TestPea(4));
        mapPod.save(new TestPea(5));
        ClassA objA = new ClassA(10);
        objA.setPea1(1);
        objA.setPea2(2);
        mapPod.save(objA);
        ClassB objB = new ClassB(11);
        objB.setPea1(3);
        objB.setPea2(4);
        objB.setPea3(5);
        mapPod.save(objB);
        ClassC objC = new ClassC(12);
        objC.setPeaArray(new int[]{2, 3, 4, 5});
        mapPod.save(objC);
        ClassD objD = new ClassD(13);
        objD.setPeaList(Arrays.asList(2, 3, 4, 5));
        mapPod.save(objD);
        ClassE objE = new ClassE(14);
        objE.setPeaMap(ImmutableMap.<String, Integer>builder()
            .put("a", 2)
            .put("b", 3)
            .put("c", 4)
            .put("d", 5)
            .build());
        mapPod.save(objE);
        ClassF objF = new ClassF(15);
        Media media = new Media();
        media.setPea3(5);
        objF.setMedia(media);
        mapPod.save(objF);
    }

    @Test
    public void testTransferSimple() {
        MapPod<Integer, String, TestPea> newPod = new MapPod<>();
        mapPod.transfer(10, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(10, 1, 2));
    }

    @Test
    public void testCollectPeaIdsInherit() {
        MapPod<Integer, String, TestPea> newPod = new MapPod<>();
        mapPod.transfer(11, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(11, 3, 4, 5));
    }

    @Test
    public void testCollectPeaIdsArray() {
        MapPod<Integer, String, TestPea> newPod = new MapPod<>();
        mapPod.transfer(12, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(12, 2, 3, 4, 5));
    }

    @Test
    public void testCollectPeaIdsCollection() {
        MapPod<Integer, String, TestPea> newPod = new MapPod<>();
        mapPod.transfer(13, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(13, 2, 3, 4, 5));
    }

    @Test
    public void testCollectPeaIdsMap() {
        MapPod<Integer, String, TestPea> newPod = new MapPod<>();
        mapPod.transfer(14, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(14, 2, 3, 4, 5));
    }

    @Test
    public void testCollectPeaIdsIndirect() {
        MapPod<Integer, String, TestPea> newPod = new MapPod<>();
        mapPod.transfer(15, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(15, 5));
    }

    @AllArgsConstructor
    @ToString
    static class TestPea implements Pea<Integer, String, TestPea> {
        @Getter
        @Setter
        private Integer id;

        @Override
        public String getType() {
            return getClass().getSimpleName();
        }
    }

    @ToString(callSuper = true)
    static class ClassA extends TestPea {
        @Getter
        @Setter
        @PeaId
        protected int pea1;
        @Getter
        @Setter
        @PeaId
        protected int pea2;

        public ClassA(Integer id) {
            super(id);
        }
    }

    @ToString(callSuper = true)
    static class ClassB extends ClassA {
        @Getter
        @Setter
        @PeaId
        private int pea3;

        public ClassB(Integer id) {
            super(id);
        }
    }

    @ToString(callSuper = true)
    static class ClassC extends TestPea {
        @Setter
        @PeaId
        private int[] peaArray;

        public ClassC(Integer id) {
            super(id);
        }
    }

    @ToString(callSuper = true)
    static class ClassD extends TestPea {
        @Setter
        @PeaId
        private List<Integer> peaList;

        public ClassD(Integer id) {
            super(id);
        }
    }

    @ToString(callSuper = true)
    static class ClassE extends TestPea {
        @Setter
        @PeaId
        private Map<String, Integer> peaMap;

        public ClassE(Integer id) {
            super(id);
        }
    }

    @ToString
    static class Media {
        @Setter
        @PeaId
        private int pea3;
    }

    @ToString(callSuper = true)
    static class ClassF extends TestPea {
        @Setter
        @PeaId.InIt
        private Media media;

        public ClassF(Integer id) {
            super(id);
        }
    }
}
