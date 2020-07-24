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
public class TestIndexOp {
    private static RtSchema ctx;
    private static ExecContext etx1;
    private static ExecContext etx2;

    private final String exprString;
    private final Object value1;
    private final Object value2;

    @BeforeClass
    public static void setupClass() throws IOException {
        SchemaSpec spec = PeaParser.YAML.parse(
            TestIndexOp.class.getResourceAsStream("/schema/composite_vars.yml"),
            SchemaSpec.class
        );
        ctx = spec.createRtSchema();
        RtSchemaParser parser = RtSchemaParser.createYamlEventParser(ctx);
        etx1 = parser.parse(
            "{"
                + "anIntArray: [1, 2, 3], "
                + "aStrArray: [foo, bar], "
                + "aList: [1, abc], "
                + "aMap: {a: 1, b: abc},"
                + "aTuple: [10, tuple],"
                + "aDict: {foo: 2.5, bar: TOM}"
                + "}"
        );
        etx2 = parser.parse(
            "{"
                + "anIntArray: [4, 5, 6], "
                + "aStrArray: [a, b], "
                + "aList: [def, 1], "
                + "aMap: {a: def, b: 1},"
                + "aTuple: [20, TUPLE],"
                + "aDict: {foo: 3.4, bar: JERRY}"
                + "}"
        );
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
            {"aTuple[0]", 10L, 20L},
            {"aTuple[1]", "tuple", "TUPLE"},
            {"aDict.foo", 2.5, 3.4},
            {"aDict['bar']", "TOM", "JERRY"},
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
