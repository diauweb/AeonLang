grammar Aeon;

@header {
	package xiaodai.aeon.parser;
}

WS: [ \t\u000C\r\n]+ -> skip;

ID: [A-Za-z_0-9]+;

QUOTED
	: '"' (EscapeSequence | ~('\\' | '"'))* '"'
	| '\'' ( EscapeSequence | ~('\'' | '\\'))* '\'';

fragment EscapeSequence:
	'\\' [abfnrtvz"'\\]
	| '\\' '\r'? '\n'
	| UtfEscape;

fragment UtfEscape: '\\' 'u{' HexDigit+ '}';
fragment HexDigit: [0-9a-fA-F];

function:
	'[' (functionArgs ':')? chainExpression (';' chainExpression)* ';'? ']';

functionArgs: ID (',' ID)* (',,' ID)?;

dereference: '$' ID*;

identifier
    : ID
    | QUOTED;

expressionPart
	: identifier #exprId
	| dereference #exprDeref
	| '(' chainExpression ')' #exprBlock
	| function #exprFunc
	;

expression: expressionPart+;

chainExpression: expression ( '|' expression)*;

statement: chainExpression ';';

program: statement+;
