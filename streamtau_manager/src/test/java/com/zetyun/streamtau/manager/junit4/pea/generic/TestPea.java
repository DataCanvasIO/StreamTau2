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

package com.zetyun.streamtau.manager.junit4.pea.generic;

import com.google.common.collect.ImmutableMap;
import com.zetyun.streamtau.manager.pea.generic.Pea;
import com.zetyun.streamtau.manager.pea.generic.PeaId;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

public class TestPea {
    @Before
    public void setup() {
    }

    @Test
    public void testChildrenSimple() {
        ClassA objA = new ClassA();
        objA.setPea1(1);
        objA.setPea2(2);
        assertThat(objA.children(), hasItems(1, 2));
    }

    @Test
    public void testChildrenInherit() {
        ClassB objB = new ClassB();
        objB.setPea1(1);
        objB.setPea2(2);
        objB.setPea3(3);
        assertThat(objB.children(), hasItems(1, 2, 3));
    }

    @Test
    public void testChildrenArray() {
        ClassC objC = new ClassC();
        objC.setPea1(1);
        objC.setPea2(2);
        objC.setPeaArray(new int[]{3, 4, 5, 6});
        assertThat(objC.children(), hasItems(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void testChildrenCollection() {
        ClassD objD = new ClassD();
        objD.setPea1(1);
        objD.setPea2(2);
        objD.setPeaList(Arrays.asList(3, 4, 5, 6));
        assertThat(objD.children(), hasItems(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void testChildrenMap() {
        ClassE objE = new ClassE();
        objE.setPea1(1);
        objE.setPea2(2);
        objE.setPeaMap(ImmutableMap.<String, Integer>builder()
            .put("a", 3)
            .put("b", 4)
            .put("c", 5)
            .put("d", 6)
            .build());
        assertThat(objE.children(), hasItems(1, 2, 3, 4, 5, 6));

    }

    @Test
    public void testChildrenIndirect() {
        ClassF objF = new ClassF();
        objF.setPea1(1);
        objF.setPea2(2);
        Media media = new Media();
        media.setPea3(3);
        objF.setMedia(media);
        assertThat(objF.children(), hasItems(1, 2, 3));
    }

    static class ClassA implements Pea<Integer, String> {
        @Setter
        @PeaId
        protected int pea1;
        @Setter
        @PeaId
        protected int pea2;
        @Getter
        @Setter
        private Integer id;

        @Override
        public String getType() {
            return getClass().getSimpleName();
        }
    }

    static class ClassB extends ClassA {
        @Setter
        @PeaId
        private int pea3;
    }

    static class ClassC extends ClassA {
        @Setter
        @PeaId
        private int[] peaArray;
    }

    static class ClassD extends ClassA {
        @Setter
        @PeaId
        private List<Integer> peaList;
    }

    static class ClassE extends ClassA {
        @Setter
        @PeaId
        private Map<String, Integer> peaMap;
    }

    static class Media {
        @Setter
        @PeaId
        private int pea3;
    }

    static class ClassF extends ClassA {
        @Setter
        private Media media;
    }
}
