import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class KotlinParser {

    private List<Token> tokens;
    private int currentTokenIndex = 0;
    private Token currentToken = null;
    private StringBuilder codeGenerated = new StringBuilder();;

    public KotlinParser(List<Token> tokens) {
        this.tokens = tokens;
        currentToken = tokens.get(currentTokenIndex);
    }

    private void print(String text, boolean jumpLine) {
        if (jumpLine)
            System.out.println(text);
        else
            System.out.print(text);
    }

    private void updateCurrentToken() {
        currentTokenIndex++;
        currentToken = tokens.get(currentTokenIndex);
    }

    private Token peekToken(int step) {
    	return tokens.get(currentTokenIndex + step);	
    }

    public boolean ParseToKotlin() {
        parseClassDeclaration();
        codeGenerated.append("\n");
        while(currentToken.type == "to") {
        	parseFunction();
        	if(currentTokenIndex + 1 < tokens.size()) {
        		updateCurrentToken();
        	}
        	codeGenerated.append("\n\n");
        }
        codeGenerated.append("}\n");
        print(codeGenerated.toString(), false);
        return true;
    }
    
    
    //PYTHON PARSING, CHANGE TO  KOTLIN 
    private boolean parseFunction() {
    		updateCurrentToken();
	    	if (currentToken.type == "function") {
	    		codeGenerated.append("def "+ currentToken.id);
	    		updateCurrentToken();
	    		if(currentToken.type == "pronoun" && peekToken(1).type == "needs") {
	    			updateCurrentToken();
	    			updateCurrentToken();
	    			codeGenerated.append("(self, "+currentToken.id+"):\t");
	    			updateCurrentToken();
	    		}
	    		else {
	    			codeGenerated.append("(self):\n\t");
	    		}
	    	}
	    	while(currentToken.type != "end") {
	    		if(currentToken.type == "coma") {
	    			codeGenerated.append("\n \t");
		    		updateCurrentToken();
		    	}
	    		if(currentToken.type == "possesive") {
		    		updateCurrentToken();
		    		codeGenerated.append("self.");
		    		do {
		    			codeGenerated.append(currentToken.id);
		    			updateCurrentToken();
		    		} while(currentToken.type != "is" && currentToken.type != "res" && currentToken.type != "sum" );
		    		if(currentToken.type == "res") {
		    			codeGenerated.append(" -");
		    			updateCurrentToken();
		    		}
		    		if(currentToken.type == "sum") {
		    			codeGenerated.append(" +");
		    			updateCurrentToken();
		    		}
		    		if(currentToken.type == "by" || currentToken.type == "is") {
		    			codeGenerated.append(" =");
		    			updateCurrentToken();
		    		}
		    		if(currentToken.type == "in") {
		    			codeGenerated.append("=");
		    			updateCurrentToken();
		    		}
		    		if(currentToken.type == "identifier") {
		    			codeGenerated.append(" self."+currentToken.id);
		    			updateCurrentToken();
		    		}
		    		if(currentToken.type != "coma") {
		    			codeGenerated.append(" "+currentToken.id);
			    		updateCurrentToken();
		    		}
		    	}
	    		if(currentToken.type == "return") {
	    			codeGenerated.append("return ");
	    			updateCurrentToken();
	    			if(currentToken.type != "end") {
	    				codeGenerated.append(currentToken.id);
	    				updateCurrentToken();
	    			}
	    		}
	    		if(currentToken.type == "pronoun") {
	    			updateCurrentToken();
	    			if(currentToken.type =="print" && peekToken(1).type == "opening-parenthesis") {
	    				updateCurrentToken();
	    				codeGenerated.append("print(");
	    				updateCurrentToken();
	    				do {
	    					codeGenerated.append(currentToken.id);
	    					updateCurrentToken();
	    				} while(currentToken.type != "closing-parenthesis");
	    				codeGenerated.append(currentToken.id);
	    				updateCurrentToken();
	    			}
	    			if(currentToken.type == "used") {
			    		updateCurrentToken();
			    		codeGenerated.append("self."+currentToken.id);
			    		updateCurrentToken();
			    		if(currentToken.type == "opening-parenthesis") {
			    			codeGenerated.append(currentToken.id);
			    			updateCurrentToken();
			    			while(currentToken.type != "closing-parenthesis") {
			    				codeGenerated.append(currentToken.id);
			    				updateCurrentToken();
			    			}
			    			codeGenerated.append(currentToken.id);
			    			updateCurrentToken();
			    		}
			    	}
	    		} 
	    	}
	    	return true;
    }

    private boolean parseClassDeclaration() {
        if (currentToken.id.equals("A") && peekToken(1).type.equals("class")) {
            updateCurrentToken();
            codeGenerated.append("public class " + currentToken.id + " {\n");
        }
        else
            return false;

        while (!currentToken.id.contains("He") && !currentToken.id.contains("She") && !currentToken.id.contains("To")) {
            updateCurrentToken();
        }

        // TODO: Check attributes
        if (peekToken(1).id.contains("has")) {
            updateCurrentToken();
            while (!currentToken.id.contains(".")) {
                updateCurrentToken();
                String attributeName = currentToken.id;
                String attributeType = currentToken.type;

                updateCurrentToken();
                updateCurrentToken();

                StringBuilder attributeVal = new StringBuilder();
                if (attributeType.contains("pairID")) {
                    while (!currentToken.id.contains(")")) {
                        attributeVal.append(currentToken.id);
                        updateCurrentToken();
                    }
                    attributeVal.append(")");
                }
                else {
                    attributeVal = new StringBuilder(currentToken.id);
                }
                codeGenerated.append("     " + attributeType + " " + attributeName + " = " + attributeVal + "\n");
                updateCurrentToken();
            }
            updateCurrentToken();
        }

        if (peekToken(1).id.contains("can")) {
            updateCurrentToken();
            HashSet<String> methods = new HashSet<>();
            while (!currentToken.id.equals(".")) {
                updateCurrentToken();
                String methodName = currentToken.id;
                methods.add(methodName);
                updateCurrentToken();
            }
        }

        updateCurrentToken();
        return true;
    }
}
