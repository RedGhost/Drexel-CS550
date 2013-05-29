/* this is the lex file for the sample mini language at
 * for CS550, Assignment 2
 * Modified by Mike Kopack for CS550, 2009 Spring Qtr.
 * Should be at the same level of completeness as the Lecture 2c
 * C++ version.
 */

import java_cup.runtime.Symbol;
%%
%cup
%%
";" {return new Symbol(sym.SEMI); }
"+" {return new Symbol(sym.PLUS); }
"-" {return new Symbol(sym.MINUS); }
"*" {return new Symbol(sym.TIMES); }
"," {return new Symbol(sym.COMMA); }
":=" {return new Symbol(sym.ASSIGN); }
"(" {return new Symbol(sym.LPAREN); }
")" {return new Symbol(sym.RPAREN); }
"[" {return new Symbol(sym.LBRACKET); } // Assignment 2 (mwa29)
"]" {return new Symbol(sym.RBRACKET); } // Assignment 2 (mwa29)
"if" {return new Symbol(sym.IF); }
"then" {return new Symbol(sym.THEN); }
"else" {return new Symbol(sym.ELSE); }
"not" {return new Symbol(sym.NOT); } // Assignment 2 (mks62)
"fi" {return new Symbol(sym.FI);}
"while" {return new Symbol(sym.WHILE); }
"do" {return new Symbol(sym.DO); }
"od" {return new Symbol(sym.OD); }
"proc" {return new Symbol(sym.PROC); }
"end" {return new Symbol(sym.END); }
"repeat" {return new Symbol(sym.REPEAT); }
"until" {return new Symbol(sym.UNTIL); }
"return" {return new Symbol(sym.RETURN); }
"cons" {return new Symbol(sym.CONS); }
"car" {return new Symbol(sym.CAR); }
"cdr" {return new Symbol(sym.CDR); }
"nullp" {return new Symbol(sym.NULLP); }
"intp" {return new Symbol(sym.INTP); }
"listp" {return new Symbol(sym.LISTP); }
"||" {return new Symbol(sym.CONCAT); }
[0-9]+ {return new Symbol(sym.NUMBER, new Integer(yytext())); }
[a-zA-Z]+ {return new Symbol(sym.ID, new String(yytext())); }
[ \t\r\n\f] {/* ignore white space */}
. {System.err.println("Illegal character: "+yytext());}
