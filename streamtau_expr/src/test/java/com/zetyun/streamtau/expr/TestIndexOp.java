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

package com.zetyun.streamtau.expr;

import com.zetyun.streamtau.expr.core.CompileContext;
import com.zetyun.streamtau.expr.core.Expr;
import com.zetyun.streamtau.expr.core.SimpleCompileContext;
import com.zetyun.streamtau.expr.parser.StreamtauExprCompiler;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.expr.runtime.context.ExecContext;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class TestIndexOp {
    private static CompileContext ctx;
    private static ExecContext etx1;
    private static ExecContext etx2;

    private final String exprString;
    private final Object value1;
    private final Object value2;

    @BeforeClass
    public static void setupClass() {
        ctx = new SimpleCompileContext() {
            @Override
            protected void init() {
                typeMap.put("anIntArray", Long[].class);
                typeMap.put("aStrArray", String[].class);
                typeMap.put("aList", List.class);
                typeMap.put("aMap", Map.class);
            }
        };
        etx1 = ctx.createExecContext();
        etx1.setNamed("anIntArray", new Long[]{1L, 2L, 3L});
        etx1.setNamed("aStrArray", new String[]{"foo", "bar", "foobar"});
        List<Object> list1 = new LinkedList<>();
        list1.add(1L);
        list1.add("abc");
        etx1.setNamed("aList", list1);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("a", 1L);
        map1.put("b", "abc");
        etx1.setNamed("aMap", map1);
        etx2 = ctx.createExecContext();
        etx2.setNamed("anIntArray", new Long[]{4L, 5L});
        etx2.setNamed("aStrArray", new String[]{"a", "b"});
        List<Object> list2 = new LinkedList<>();
        list2.add("def");
        list2.add(3L);
        etx2.setNamed("aList", list2);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("a", "def");
        map2.put("b", 3L);
        etx2.setNamed("aMap", map2);
    }

    @Contract(pure = true)
    @Parameterized.Parameters(name = "{index}: {0} ==> {1}, {2}")
    public static @NotNull Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
            {"anIntArray[0]", 1L, 4L},
            {"aStrArray[1]", "bar", "b"},
            {"anIntArray[0] + anIntArray[1]", 3L, 9L},
            {"aStrArray[0]+aStrArray[1]", "foobar", "ab"},
            {"aList[0]", 1L, "def"},
            {"aMap.a", 1L, "def"},
        });
    }

    @Test
    public void test() {
        Expr expr = StreamtauExprCompiler.INS.parse(exprString);
        RtExpr rtExpr = expr.compileIn(ctx);
        assertThat(rtExpr.eval(etx1), is(value1));
        assertThat(rtExpr.eval(etx2), is(value2));
    }
}
