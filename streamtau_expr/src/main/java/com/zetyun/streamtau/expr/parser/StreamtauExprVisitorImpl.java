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

package com.zetyun.streamtau.expr.parser;

import com.zetyun.streamtau.expr.antlr4.StreamtauExprParser;
import com.zetyun.streamtau.expr.antlr4.StreamtauExprParserBaseVisitor;
import com.zetyun.streamtau.expr.core.Expr;
import com.zetyun.streamtau.expr.op.BinaryOp;
import com.zetyun.streamtau.expr.op.IndexOp;
import com.zetyun.streamtau.expr.op.OpFactory;
import com.zetyun.streamtau.expr.op.UnaryOp;
import com.zetyun.streamtau.expr.value.Bool;
import com.zetyun.streamtau.expr.value.Int;
import com.zetyun.streamtau.expr.value.Real;
import com.zetyun.streamtau.expr.value.Str;
import com.zetyun.streamtau.expr.var.Var;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StreamtauExprVisitorImpl extends StreamtauExprParserBaseVisitor<Expr> {
    private @NotNull Expr internalVisitBinaryOp(
        int type,
        @NotNull List<StreamtauExprParser.ExprContext> exprList
    ) {
        BinaryOp op = OpFactory.getBinary(type);
        op.setExpr0(visit(exprList.get(0)));
        op.setExpr1(visit(exprList.get(1)));
        return op;
    }

    private @NotNull Expr internalVisitUnaryOp(
        int type,
        StreamtauExprParser.ExprContext expr
    ) {
        UnaryOp op = OpFactory.getUnary(type);
        op.setExpr(visit(expr));
        return op;
    }

    @Override
    public Expr visitInt(@NotNull StreamtauExprParser.IntContext ctx) {
        return Int.fromString(ctx.INT().getText());
    }

    @Override
    public Expr visitReal(@NotNull StreamtauExprParser.RealContext ctx) {
        return Real.fromString(ctx.REAL().getText());
    }

    @Override
    public Expr visitStr(@NotNull StreamtauExprParser.StrContext ctx) {
        String str = ctx.STR().getText();
        return new Str(StringEscapeUtils.unescapeJson(str.substring(1, str.length() - 1)));
    }

    @Override
    public Expr visitBool(@NotNull StreamtauExprParser.BoolContext ctx) {
        return Bool.fromString(ctx.BOOL().getText());
    }

    @Override
    public Expr visitVar(@NotNull StreamtauExprParser.VarContext ctx) {
        return new Var(ctx.ID().getText());
    }

    @Override
    public Expr visitPars(@NotNull StreamtauExprParser.ParsContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Expr visitPosNeg(@NotNull StreamtauExprParser.PosNegContext ctx) {
        return internalVisitUnaryOp(ctx.op.getType(), ctx.expr());
    }

    @Override
    public Expr visitMulDiv(@NotNull StreamtauExprParser.MulDivContext ctx) {
        return internalVisitBinaryOp(ctx.op.getType(), ctx.expr());
    }

    @Override
    public Expr visitAddSub(@NotNull StreamtauExprParser.AddSubContext ctx) {
        return internalVisitBinaryOp(ctx.op.getType(), ctx.expr());
    }

    @Override
    public Expr visitRelation(@NotNull StreamtauExprParser.RelationContext ctx) {
        return internalVisitBinaryOp(ctx.op.getType(), ctx.expr());
    }

    @Override
    public Expr visitNot(@NotNull StreamtauExprParser.NotContext ctx) {
        return internalVisitUnaryOp(ctx.op.getType(), ctx.expr());
    }

    @Override
    public Expr visitAnd(@NotNull StreamtauExprParser.AndContext ctx) {
        return internalVisitBinaryOp(ctx.op.getType(), ctx.expr());
    }

    @Override
    public Expr visitOr(@NotNull StreamtauExprParser.OrContext ctx) {
        return internalVisitBinaryOp(ctx.op.getType(), ctx.expr());
    }

    @Override
    public Expr visitIndex(@NotNull StreamtauExprParser.IndexContext ctx) {
        BinaryOp op = new IndexOp();
        op.setExpr0(visit(ctx.expr().get(0)));
        op.setExpr1(visit(ctx.expr().get(1)));
        return op;
    }

    @Override
    public Expr visitStrIndex(@NotNull StreamtauExprParser.StrIndexContext ctx) {
        BinaryOp op = new IndexOp();
        op.setExpr0(visit(ctx.expr()));
        op.setExpr1(new Str(ctx.ID().getText()));
        return op;
    }

    @Override
    public Expr visitStringOp(@NotNull StreamtauExprParser.StringOpContext ctx) {
        return internalVisitBinaryOp(ctx.op.getType(), ctx.expr());
    }

    @Override
    public Expr visitFun(@NotNull StreamtauExprParser.FunContext ctx) {
        // TODO
        return null;
    }
}
