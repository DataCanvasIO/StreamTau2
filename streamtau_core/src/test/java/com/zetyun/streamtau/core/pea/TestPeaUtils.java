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
import com.zetyun.streamtau.core.pod.MapPod;
import com.zetyun.streamtau.core.test.pea.BasePea;
import com.zetyun.streamtau.core.test.pea.ClassA;
import com.zetyun.streamtau.core.test.pea.ClassB;
import com.zetyun.streamtau.core.test.pea.ClassC;
import com.zetyun.streamtau.core.test.pea.ClassD;
import com.zetyun.streamtau.core.test.pea.ClassE;
import com.zetyun.streamtau.core.test.pea.ClassF;
import com.zetyun.streamtau.core.test.pea.Media;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

public class TestPeaUtils {
    @Test
    public void testVisitSimple() {
        ClassA objA = new ClassA(10);
        objA.setPea1(1);
        objA.setPea2(2);
        Set<Integer> set = new HashSet<>(2);
        PeaUtils.visitPeaIds(objA, set::add);
        assertThat(set, hasItems(1, 2));
    }

    @Test
    public void testVisitInherit() {
        ClassB objB = new ClassB(10);
        objB.setPea1(1);
        objB.setPea2(2);
        objB.setPea3(3);
        Set<Integer> set = new HashSet<>(3);
        PeaUtils.visitPeaIds(objB, set::add);
        assertThat(set, hasItems(1, 2, 3));
    }

    @Test
    public void testVisitArray() {
        ClassC objC = new ClassC(10);
        objC.setPeaArray(new Integer[]{1, 2, 3, 4});
        Set<Integer> set = new HashSet<>(4);
        PeaUtils.visitPeaIds(objC, set::add);
        assertThat(set, hasItems(1, 2, 3, 4));
    }

    @Test
    public void testVisitCollection() {
        ClassD objD = new ClassD(10);
        objD.setPeaList(Arrays.asList(1, 2, 3, 4));
        MapPod<Integer, String, BasePea> newPod = new MapPod<>();
        Set<Integer> set = new HashSet<>(4);
        PeaUtils.visitPeaIds(objD, set::add);
        assertThat(set, hasItems(1, 2, 3, 4));
    }

    @Test
    public void testVisitMap() {
        ClassE objE = new ClassE(10);
        objE.setPeaMap(ImmutableMap.<String, Integer>builder()
            .put("a", 1)
            .put("b", 2)
            .put("c", 3)
            .put("d", 4)
            .build());
        Set<Integer> set = new HashSet<>(4);
        PeaUtils.visitPeaIds(objE, set::add);
        assertThat(set, hasItems(1, 2, 3, 4));
    }

    @Test
    public void testVisitIndirect() {
        ClassF objF = new ClassF(10);
        Media media = new Media();
        media.setPea3(1);
        objF.setMedia(media);
        Set<Integer> set = new HashSet<>(1);
        PeaUtils.visitPeaIds(objF, set::add);
        assertThat(set, hasItems(1));
    }
}
