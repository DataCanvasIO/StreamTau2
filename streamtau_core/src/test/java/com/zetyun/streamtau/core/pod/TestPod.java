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

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

public class TestPod {
    @Test
    public void testTransferSimple() {
        MapPod<Integer, String, BasePea> mapPod = new MapPod<>();
        mapPod.save(new BasePea(1));
        mapPod.save(new BasePea(2));
        ClassA objA = new ClassA(10);
        objA.setPea1(1);
        objA.setPea2(2);
        mapPod.save(objA);
        MapPod<Integer, String, BasePea> newPod = new MapPod<>();
        mapPod.transfer(10, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(10, 1, 2));
    }

    @Test
    public void testTransferInherit() {
        MapPod<Integer, String, BasePea> mapPod = new MapPod<>();
        mapPod.save(new BasePea(1));
        mapPod.save(new BasePea(2));
        mapPod.save(new BasePea(3));
        ClassB objB = new ClassB(10);
        objB.setPea1(1);
        objB.setPea2(2);
        objB.setPea3(3);
        mapPod.save(objB);
        MapPod<Integer, String, BasePea> newPod = new MapPod<>();
        mapPod.transfer(10, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(10, 1, 2, 3));
    }

    @Test
    public void testTransferArray() {
        MapPod<Integer, String, BasePea> mapPod = new MapPod<>();
        mapPod.save(new BasePea(1));
        mapPod.save(new BasePea(2));
        mapPod.save(new BasePea(3));
        mapPod.save(new BasePea(4));
        ClassC objC = new ClassC(10);
        objC.setPeaArray(new Integer[]{1, 2, 3, 4});
        mapPod.save(objC);
        MapPod<Integer, String, BasePea> newPod = new MapPod<>();
        mapPod.transfer(10, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(10, 1, 2, 3, 4));
    }

    @Test
    public void testTransferCollection() {
        MapPod<Integer, String, BasePea> mapPod = new MapPod<>();
        mapPod.save(new BasePea(1));
        mapPod.save(new BasePea(2));
        mapPod.save(new BasePea(3));
        mapPod.save(new BasePea(4));
        ClassD objD = new ClassD(10);
        objD.setPeaList(Arrays.asList(1, 2, 3, 4));
        mapPod.save(objD);
        MapPod<Integer, String, BasePea> newPod = new MapPod<>();
        mapPod.transfer(10, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(10, 1, 2, 3, 4));
    }

    @Test
    public void testTransferMap() {
        MapPod<Integer, String, BasePea> mapPod = new MapPod<>();
        mapPod.save(new BasePea(1));
        mapPod.save(new BasePea(2));
        mapPod.save(new BasePea(3));
        mapPod.save(new BasePea(4));
        ClassE objE = new ClassE(10);
        objE.setPeaMap(ImmutableMap.<String, Integer>builder()
            .put("a", 1)
            .put("b", 2)
            .put("c", 3)
            .put("d", 4)
            .build());
        mapPod.save(objE);
        MapPod<Integer, String, BasePea> newPod = new MapPod<>();
        mapPod.transfer(10, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(10, 1, 2, 3, 4));
    }

    @Test
    public void testTransferIndirect() {
        MapPod<Integer, String, BasePea> mapPod = new MapPod<>();
        mapPod.save(new BasePea(1));
        ClassF objF = new ClassF(10);
        Media media = new Media();
        media.setPea3(1);
        objF.setMedia(media);
        mapPod.save(objF);
        MapPod<Integer, String, BasePea> newPod = new MapPod<>();
        mapPod.transfer(10, newPod);
        assertThat(newPod.peaMap.keySet(), hasItems(10, 1));
    }
}
