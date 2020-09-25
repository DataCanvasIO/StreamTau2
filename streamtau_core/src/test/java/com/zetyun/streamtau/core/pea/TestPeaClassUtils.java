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

import com.zetyun.streamtau.core.test.pea.ClassA;
import com.zetyun.streamtau.core.test.pea.ClassB;
import com.zetyun.streamtau.core.test.pea.ClassC;
import com.zetyun.streamtau.core.test.pea.ClassF;
import com.zetyun.streamtau.core.test.pea.ClassG;
import com.zetyun.streamtau.core.test.pea.ClassH;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

public class TestPeaClassUtils {
    @Test
    public void testVisitClassDirect() {
        Set<String> set = new HashSet<>();
        PeaClassUtils.visitClassPeaIds(ClassA.class, (Field f) -> set.add(f.getName()));
        assertThat(set, hasItems("pea1", "pea2"));
    }

    @Test
    public void testVisitClassInherit() {
        Set<String> set = new HashSet<>();
        PeaClassUtils.visitClassPeaIds(ClassB.class, (Field f) -> set.add(f.getName()));
        assertThat(set, hasItems("pea1", "pea2", "pea3"));
    }

    @Test
    public void testVisitClassArray() {
        Set<String> set = new HashSet<>();
        PeaClassUtils.visitClassPeaIds(ClassC.class, (Field f) -> set.add(f.getName()));
        assertThat(set, hasItems("peaArray"));
    }

    @Test
    public void testVisitClassIndirect() {
        Set<String> set = new HashSet<>();
        PeaClassUtils.visitClassPeaIds(ClassF.class, (Field f) -> set.add(f.getName()));
        assertThat(set, hasItems("pea3"));
    }

    @Test
    public void testVisitClassParameterizedIndirect() {
        Set<String> set = new HashSet<>();
        PeaClassUtils.visitClassPeaIds(ClassG.class, (Field f) -> set.add(f.getName()));
        assertThat(set, hasItems("pea3"));
    }

    @Test
    public void testVisitClassArrayIndirect() {
        Set<String> set = new HashSet<>();
        PeaClassUtils.visitClassPeaIds(ClassH.class, (Field f) -> set.add(f.getName()));
        assertThat(set, hasItems("pea3"));
    }
}
