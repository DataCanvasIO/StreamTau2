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
import com.zetyun.streamtau.expr.runtime.RtConst;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nonnull;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class TestWithoutVar {
    private final String exprString;
    private final Object value;

    @Parameterized.Parameters(name = "{index}: {0} ==> {1}")
    @Nonnull
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
            // value
            {"2", 2L},
            {"3.0", 3.0},
            {"'foo'", "foo"},
            {"true", true},
            {"false", false},
            // arithmetic op
            {"1 + 2", 3L},
            {"1 + 2*3", 7L},
            {"(1 + 2)*3", 9L},
            {"(1 + 2)*(5 - (3 + 4))", -6L},
            {"3*1.5 + 2.34", 6.84},
            {"2*-3.14e2", -6.28e2},
            {"5e4+3e3", 53e3},
            // relational & logical op
            {"3 < 4", true},
            {"4.0 == 4", true},
            {"5 != 6", true},
            {"1 <= 2 && 3 > 2", true},
            {"1 > 0.1 and 2 - 2 = 0", true},
            {"not (0.0*2 < 0 || 1*4 > 3 and 6/6 == 1)", false},
            // string op
            {"'abc' startsWith 'a'", true},
            {"'abc' endsWith 'c'", true},
            {"\"abc\" + 'def'", "abcdef"},
            {"'\\\\-\\/-\\b-\\n-\\r-\\t-\\u0020'", "\\-/-\b-\n-\r-\t- "},
            {"\"a\\\"b\"", "a\"b"},
            {"'a\"b'", "a\"b"},
        });
    }

    @Test
    public void test() {
        Expr expr = StreamTauExprCompiler.INS.parse(exprString);
        RtExpr rtExpr = expr.compileIn(null);
        assertThat(rtExpr, instanceOf(RtConst.class));
        assertThat(rtExpr.eval(null), is(value));
    }
}
