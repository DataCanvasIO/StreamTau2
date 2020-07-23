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

import com.zetyun.streamtau.core.pea.PeaParser;
import com.zetyun.streamtau.core.schema.SchemaSpec;
import com.zetyun.streamtau.expr.core.Expr;
import com.zetyun.streamtau.expr.parser.StreamtauExprCompiler;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.runtime.context.ExecContext;
import com.zetyun.streamtau.runtime.schema.RtSchema;
import com.zetyun.streamtau.runtime.schema.RtSchemaParser;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class TestWithVar {
    private static RtSchema ctx;
    private static ExecContext etx1;
    private static ExecContext etx2;

    private final String exprString;
    private final Object value1;
    private final Object value2;

    @BeforeClass
    public static void setupClass() throws IOException {
        SchemaSpec spec = PeaParser.YAML.parse(
            TestWithVar.class.getResourceAsStream("/schema/simple_vars.yml"),
            SchemaSpec.class
        );
        ctx = new RtSchema(spec.createRtNode());
        RtSchemaParser parser = RtSchemaParser.createYamlEventParser(ctx);
        etx1 = parser.parse("{anInt: 2, aReal: 3.0, aStr: foo, aBool: true}");
        etx2 = parser.parse("{anInt: 3, aReal: 4.0, aStr: bar, aBool: false}");
    }

    @Contract(pure = true)
    @Parameterized.Parameters(name = "{index}: {0} ==> {1}, {2}")
    public static @NotNull Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
            {"anInt", 2L, 3L},
            {"aReal", 3.0, 4.0},
            {"aStr", "foo", "bar"},
            {"aBool", true, false},
            {"1 + anInt", 3L, 4L},
            {"1 + 2*aReal", 7.0, 9.0},
            // short-circuit, there must be a var to prevent const optimization
            {"false and anInt/0", false, false},
            {"true or anInt/0", true, true},
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
