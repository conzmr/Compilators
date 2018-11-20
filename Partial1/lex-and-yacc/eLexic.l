%{
#define pId 1
#define pCte 2
#define pSum 3
#define pMul 4
#define pAbr 5
#define pCer 6
#define Error 999
%}
%%
[a-z]+    { return pId; }
[0-9]+    { return pCte; }
"+"     { return pSum; }
"*"     { return pMul; }
"("     { return pAbr; }
")"     { return pCer; }
[\ \t\n]  { ;     }
.     { return Error; }
