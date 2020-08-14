parser grammar StreamTauExprParser;

options { tokenVocab=StreamTauExprLexer; }

@header {
package com.zetyun.streamtau.expr.antlr4;
}

expr : INT                                      # Int
     | REAL                                     # Real
     | STR                                      # Str
     | BOOL                                     # Bool
     | ID                                       # Var
     | '(' expr ')'                             # Pars
     | fun=ID '(' (expr (',' expr) *) ? ')'     # Fun
     | expr '.' ID                              # StrIndex
     | expr '[' expr ']'                        # Index
     | op=(ADD|SUB) expr                        # PosNeg
     | expr op=(MUL|DIV) expr                   # MulDiv
     | expr op=(ADD|SUB) expr                   # AddSub
     | expr op=(LT|LE|EQ|GT|GE|NE) expr         # Relation
     | expr op=(STARTSWITH|ENDSWITH) expr       # StringOp
     | op=NOT expr                              # Not
     | expr op=AND expr                         # And
     | expr op=OR expr                          # Or
     ;
