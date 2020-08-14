lexer grammar StreamTauExprLexer;

@header {
package com.zetyun.streamtau.expr.antlr4;
}

INT        : '-'? NUM ;
REAL       : '-'? (NUM '.' NUM EXP? | NUM EXP) ;
STR        : '\'' (ESC | ~['\\])* '\'' | '"' (ESC | ~["\\])* '"' ;
BOOL       : 'true' | 'false' ;

// operators
ADD        : '+' ;
SUB        : '-' ;
MUL        : '*' ;
DIV        : '/' ;

LT         : '<' ;
LE         : '<=' ;
EQ         : '==' | '=' ;
GT         : '>' ;
GE         : '>=' ;
NE         : '<>' | '!=' ;

AND        : 'and' | '&&' ;
OR         : 'or' | '||' ;
NOT        : 'not' | '!' ;

STARTSWITH : 'startsWith' ;
ENDSWITH   : 'endsWith' ;

ID         : (ALPHA | '_' | '$') (ALPHA | DIGIT | '_' )* ;

WS         : [ \t]+ -> skip ;
NL         : ('\r'? '\n')+ -> skip ;

LPAR       : '(' ;
RPAR       : ')' ;
COMMA      : ',' ;
DOT        : '.' ;
LBRCK      : '[' ;
RBRCK      : ']' ;

fragment
ALPHA      : [a-zA-Z] ;

fragment
DIGIT      : [0-9] ;

fragment
HEX        : [0-9a-fA-F] ;

fragment
NUM        : '0' | [1-9] [0-9]* ;

fragment
EXP        : [Ee] [+\-]? NUM ;

fragment
ESC        : '\\' (["\\/bfnrt] | UNICODE) ;

fragment
UNICODE    : 'u' HEX HEX HEX HEX ;
