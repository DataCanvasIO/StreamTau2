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
import com.zetyun.streamtau.expr.parser.StreamTauExprCompiler;
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
public class TestFun {
    @ClassRule
    public static ContextResource res = new ContextResource(
        "/schema/simple_vars.yml",
        "{anInt: 2, aReal: 3.0, aStr: foo, aBool: true}",
        "{anInt: 3, aReal: 4.0, aStr: bar, aBool: false}"
    );

    private final String exprString;
    private final Object value0;
    private final Object value1;

    @Parameterized.Parameters(name = "{index}: {0} ==> {1}, {2}")
    @Nonnull
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
            {"abs(anInt)", 2L, 3L},
        });
    }

    @Test
    public void test() {
        Expr expr = StreamTauExprCompiler.INS.parse(exprString);
        RtExpr rtExpr = expr.compileIn(res.getCtx());
        assertThat(rtExpr.eval(res.getEtx(0)), is(value0));
        assertThat(rtExpr.eval(res.getEtx(1)), is(value1));
    }
}
