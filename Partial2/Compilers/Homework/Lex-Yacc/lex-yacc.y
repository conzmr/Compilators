%{
#include <stdio.h>
#include <string.h>
 
void yyerror(const char *str)
{
        fprintf(stderr,"error: %s\n",str);
}
 
int yywrap()
{
        return 1;
} 
  
int main()
{
        yyparse();
} 

%}

%token NUMERO PALABRA ENTERO SEMICOLON IGUAL

%%
commands: /* empty */
        | commands command
        ;

command:
        creacion_entero
        |
        asignacion_palabra
        ;

creacion_entero:
        ENTERO PALABRA SEMICOLON
        {
		printf("\tCreacion de entero!\n");
        }
        ;

asignacion_palabra:
        PALABRA IGUAL NUMERO SEMICOLON
        {
                printf("\tAsignacion de palabra a %d\n", $3);
        }
        ;
%%