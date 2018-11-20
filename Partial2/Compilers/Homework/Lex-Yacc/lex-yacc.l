%{
#include <stdio.h>
#include "y.tab.h"
%}
%%
entero                  return ENTERO;
[0-9]+                  yylval=atoi(yytext); return NUMERO;
[a-zA-Z][a-zA-Z0-9]*    return PALABRA;
;                       return SEMICOLON;
=                       return IGUAL;
\n                      /* ignore end of line */;
[ \t]+                  /* ignore whitespace */;
%%