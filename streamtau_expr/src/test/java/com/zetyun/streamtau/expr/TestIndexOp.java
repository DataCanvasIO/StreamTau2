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

import com.zetyun.streamtau.expr.core.Expr;
import com.zetyun.streamtau.expr.parser.StreamtauExprCompiler;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import lombok.RequiredArgsConstructor;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nonnull;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class TestIndexOp {
    @ClassRule
    public static ContextResource res = new ContextResource(
        "/schema/composite_vars.yml",
        "{"
            + "anIntArray: [1, 2, 3], "
            + "aStrArray: [foo, bar], "
            + "aList: [1, abc], "
            + "aMap: {a: 1, b: abc},"
            + "aTuple: [10, tuple],"
            + "aDict: {foo: 2.5, bar: TOM}"
            + "}",
        "{"
            + "anIntArray: [4, 5, 6], "
            + "aStrArray: [a, b], "
            + "aList: [def, 1], "
            + "aMap: {a: def, b: 1},"
            + "aTuple: [20, TUPLE],"
            + "aDict: {foo: 3.4, bar: JERRY}"
            + "}"
    );

    private final String exprString;
    private final Object value0;
    private final Object value1;

    @Parameterized.Parameters(name = "{index}: {0} ==> {1}, {2}")
    @Nonnull
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
            {"anIntArray[0]", 1L, 4L},
            {"aStrArray[1]", "bar", "b"},
            {"anIntArray[0] + anIntArray[1]", 3L, 9L},
            {"aStrArray[0]+aStrArray[1]", "foobar", "ab"},
            {"aList[0]", 1L, "def"},
            {"aMap.a", 1L, "def"},
            {"aTuple[0]", 10L, 20L},
            {"aTuple[1]", "tuple", "TUPLE"},
            {"aDict.foo", 2.5, 3.4},
            {"aDict['bar']", "TOM", "JERRY"},
        });
    }

    @Test
    public void test() {
        Expr expr = StreamtauExprCompiler.INS.parse(exprString);
        RtExpr rtExpr = expr.compileIn(res.getCtx());
        assertThat(rtExpr.eval(res.getEtx(0)), is(value0));
        assertThat(rtExpr.eval(res.getEtx(1)), is(value1));
    }
}
