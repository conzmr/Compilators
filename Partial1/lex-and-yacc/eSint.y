%{
   #include <stdio.h>
%}
%token pId 1
%token pCte 2
%token pSum 3
%token pMul 4
%token pAbr 5
%token pCer 6
%start Expresion
%%
Expresion : Termino RestoExpr ;
RestoExpr : pSum Termino RestoExpr ;
      | ;
Termino : Factor RestoTerm ;
RestoTerm : pMul Factor RestoTerm ;
      | ;
Factor : pId ;
      | pCte ;
      | pAbr Expresion pCer ;
%%
main () {
 yyparse ();
}
yyerror(char* msg) {
   fprintf(stderr, "%s\n", msg);
}
