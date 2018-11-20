import java.util.ArrayList;
import java.util.List;


public class SyntacticAnalysis {
	
	static List<Token> tokens = new ArrayList<Token>();
	
	private enum SyntacticStates {
    	ERROR,
    	PROGRAM,
    	
    	CLN, CLIS, CLASS, CLEND,
    	
    	G_FUNC, 
    	POS_CAN, 
    	
    	GTYPE, GEQUAL, GVAR, GNEXT, G_OP, G_CP,
    	P_TYPE, P_COMA, 
    	CFNEXT, CFUNC, CFEND,
    	
    	FUNC, F_PRO_POS_R, FCOMA, FDEC, DINC, FIS, 
    	FCALL, F_CALL_OP, FATT, F_C_COMA, F_CALL_CP,
    	F_N_U_P, F_POS_ID, F_ID_S_INT, F_IS_D_I, F_ASSIGN,
    	F_B_I, F_BY, F_IN, F_N_ATT, F_ID_OP, F_ID_INT, F_P_IS_D_I,
    	F_P_OP, F_P_STR, F_P_CP,
    	FEND,
    	
    	END;
    	
    	
    	SyntacticStates is;
    	SyntacticStates _class;
    	SyntacticStates has;
    	SyntacticStates can;
    	SyntacticStates to;
    	SyntacticStates used;
    	SyntacticStates needs;
    	SyntacticStates in;
    	SyntacticStates sum;
    	SyntacticStates res;
    	SyntacticStates print;
    	SyntacticStates by;
    	SyntacticStates and;
        SyntacticStates coma;
        SyntacticStates _return;
        SyntacticStates semicolon;
        SyntacticStates openingParenthesis;
        SyntacticStates closingParenthesis;
        SyntacticStates openingKeys;
        SyntacticStates closingKeys;
        SyntacticStates openingBrackets;
        SyntacticStates closingBrackets;
        SyntacticStates add;
        SyntacticStates mul;
        SyntacticStates relational;
        SyntacticStates assignation;
        SyntacticStates integer;
        SyntacticStates id;
        SyntacticStates end;
        SyntacticStates	quotes;
        SyntacticStates colon;
        SyntacticStates string;
        SyntacticStates pronoun;
        SyntacticStates possesive;
        SyntacticStates init;
        
        
    	static {
    		// TYPE
    		PROGRAM.init = CLN;
    		CLN.id = CLIS; 
    		CLIS.is = CLASS;
    		CLASS._class = CLEND;
    		CLEND.end = G_FUNC;
    		
    		G_FUNC.pronoun = POS_CAN; G_FUNC.to = FUNC;
    		POS_CAN.has = GVAR; POS_CAN.can = CFUNC;
    		GVAR.id = GEQUAL;
    		GEQUAL.is = GTYPE;
    		GTYPE.string = GNEXT; GTYPE.integer = GNEXT; GTYPE.openingParenthesis = G_OP;
    		G_OP.integer = P_TYPE; G_OP.string = P_TYPE;
    		P_TYPE.coma = P_COMA;
    		P_COMA.integer = G_CP; P_COMA.string = G_CP;
    		G_CP.closingParenthesis = GNEXT;
    		GNEXT.coma = GVAR; GNEXT.and = GVAR; GNEXT.end = G_FUNC;
    		
    		CFUNC.id = CFNEXT; 
    		CFNEXT.coma = CFUNC; CFNEXT.and = CFUNC; CFNEXT.end = G_FUNC;
    		
    		FUNC.id = F_PRO_POS_R;
    		F_PRO_POS_R.pronoun = F_N_U_P; F_PRO_POS_R.possesive = F_POS_ID; F_PRO_POS_R._return = F_ID_S_INT;
    		
    		F_N_U_P.used = FCALL; F_N_U_P.needs = F_N_ATT; F_N_U_P.print = F_P_OP;
    		F_N_ATT.id = FCOMA;
    		FCALL.id = F_CALL_OP;
    		F_CALL_OP.openingParenthesis = FATT;
    		FATT.id = F_C_COMA; FATT.string = F_C_COMA; FATT.integer = F_C_COMA; 
    		F_C_COMA.closingParenthesis = FCOMA; F_C_COMA.coma = FATT;
    		
    		F_POS_ID.id = F_IS_D_I;
    		F_IS_D_I.is = F_ASSIGN; F_IS_D_I.sum = F_B_I; F_IS_D_I.res = F_B_I; F_IS_D_I.openingBrackets = F_ID_OP;
    		F_ID_OP.integer = F_ID_INT;
    		F_ID_INT.closingBrackets = F_P_IS_D_I;
    		F_P_IS_D_I.is = F_ASSIGN; F_P_IS_D_I.sum = F_B_I; F_P_IS_D_I.res = F_B_I;
    		F_B_I.by = F_BY; F_B_I.in = F_IN;
    		F_BY.integer = FCOMA;
    		F_ASSIGN.integer = FCOMA; F_ASSIGN.string = FCOMA;
    		F_IN.id = FCOMA;
    		FCOMA.coma = F_PRO_POS_R; FCOMA.end = G_FUNC;
    		
    		F_P_OP.openingParenthesis = F_P_STR;
    		F_P_STR.string = F_P_CP; F_P_STR.id = F_P_CP;
    		F_P_CP.add = F_P_STR; F_P_CP.closingParenthesis = FCOMA;
    		
    		F_ID_S_INT.id = FEND; F_ID_S_INT.string = FEND; F_ID_S_INT.integer = FEND;
    		FEND.end = G_FUNC;
    		
        }

    	/**
    	 * The transition for syntactic gets a string and according to the state 
    	 * that it is moves to the reference or goes to the default that is error 
    	 * if it doesn't identifies the type. 
    	 * This won't happened because Lexicon is responsible of that
    	 * @param st
    	 * @return
    	 */
    	SyntacticStates transition(String st) {
            switch (st) {
                case "is": 					return this.is == null? ERROR : this.is;
                case "class": 				return this._class == null? ERROR : this._class;
                case "has":      			return this.has == null? ERROR: this.has;
                case "can":       			return this.can == null? ERROR: this.can;
                case "to":       			return this.to == null? ERROR: this.to;
                case "used":     			return this.used == null? ERROR: this.used;
                case "needs":    			return this.needs == null? ERROR: this.needs;
                case "in":    				return this.in == null? ERROR: this.in;
                case "sum":    				return this.sum == null? ERROR: this.sum;
                case "res":    				return this.res == null? ERROR: this.res;
                case "print":    			return this.print == null? ERROR: this.print;
                case "by":    				return this.by == null? ERROR: this.by;
                case "and":    				return this.and == null? ERROR: this.and;
                case "end":    				return this.end == null? ERROR: this.end;
                case "string":      		return this.string == null? ERROR: this.string;
                case "pronoun":      		return this.pronoun == null? ERROR: this.pronoun;
                case "possesive":      		return this.possesive == null? ERROR: this.possesive;
                case "init":      			return this.init == null? ERROR: this.init;
                case "return":      		return this._return == null? ERROR: this._return;
                case "coma":      			return this.coma == null? ERROR: this.coma;
                case "semicolon": 			return this.semicolon == null? ERROR: this.semicolon;
                case "opening-parenthesis": return this.openingParenthesis == null? ERROR: this.openingParenthesis;
                case "closing-parenthesis": return this.closingParenthesis == null? ERROR: this.closingParenthesis;
                case "opening-bracket":		return this.openingBrackets == null? ERROR: this.openingBrackets;
                case "closing-bracket":		return this.closingBrackets == null? ERROR: this.closingBrackets;
                case "opening-keys": 		return this.openingKeys == null? ERROR: this.openingKeys;
                case "closing-keys":		return this.closingKeys == null? ERROR: this.closingKeys;
                case "add":					return this.add == null? ERROR: this.add;
                case "mul":					return this.mul == null? ERROR: this.mul;
                case "relational":			return this.relational == null? ERROR: this.relational;
                case "assignation":			return this.assignation == null? ERROR: this.assignation;
                case "integer":				return this.integer == null? ERROR: this.integer;
                case "identifier":			return this.id == null? ERROR: this.id;
                case "quotes":				return this.quotes == null? ERROR: this.quotes;
                case "colon":				return this.colon == null? ERROR: this.colon;
                default:					return ERROR;
            }
        }
    }
    
    /**
     * Get Error get the state and according to it gets a number 
     * that passes to the printError(number) function
     * @param syntacticState
     * @return
     */
    public static Integer getError(SyntacticStates syntacticState) {
    	switch (syntacticState) {
			default:						return 0;
		}
    }
    
    /**
     * The function of print error check for the position that is given and 
     * shows the items that the state was expecting
     * @param position
     */
    public static void printError(Integer position) {
    	switch (position) {
			default:
				System.out.println("Error Type: That shouldn't be there");
				break;
		}
    }
    
    private static void printSyntacticSymbolsTable() {
    	System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
		System.out.println("Syntax accepted");
		System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
		System.out.println("Start Syntatic symbol table");
		for (int i = 0; i < tokens.size(); i++) {
				System.out.println("--------------------------------------------------------");
    			System.out.println("Id:       "+ tokens.get(i).id);
    			System.out.println("Type:     "+ tokens.get(i).type);
    			System.out.println("Position: "+ tokens.get(i).initCharacter);
    			System.out.println("Line:     "+ tokens.get(i).line);
		}
		System.out.println("--------------------------------------------------------");
		System.out.println("End Syntatic symbol table");
    }
    
    protected boolean syntacticAnalysis() {
    	boolean isSyntaxError = false;
    	SyntacticStates syntacticState = SyntacticStates.PROGRAM;
    	int positionState = 0;
    	for (int i = 0; i < tokens.size(); i++) {
    		syntacticState = syntacticState.transition(tokens.get(i).type);
    		System.out.println(syntacticState + " " + tokens.get(i).id + " " + tokens.get(i).type);
			if (tokens.get(i).type == "identifier" && syntacticState == SyntacticStates.CLIS) {
				tokens.get(i).type = "class";
			} else if (tokens.get(i).type == "string" && syntacticState == SyntacticStates.GNEXT) {
				if (tokens.get(i-2).type == "identifier") {
					tokens.get(i-2).type = "stringID";
				}
			} else if (tokens.get(i).type == "integer" && syntacticState == SyntacticStates.GNEXT) {
				if (tokens.get(i-2).type == "identifier") {
					tokens.get(i-2).type = "integerID";
				}
			} else if (tokens.get(i).type == "closing-parenthesis" && syntacticState == SyntacticStates.GNEXT) {
				if (tokens.get(i-6).type == "identifier") {
					tokens.get(i-6).type = "pairID";
				}
			} else if (tokens.get(i).type == "identifier" && syntacticState == SyntacticStates.CFNEXT) {
				tokens.get(i).type = "createFunction";
			} else if (tokens.get(i).type == "identifier" && syntacticState == SyntacticStates.F_PRO_POS_R) {
				tokens.get(i).type = "function";
			} 
			else if (syntacticState == SyntacticStates.ERROR && !isSyntaxError) {
    			System.out.println("--------------------------------------------------------");
    			System.out.println("Syntactic Error");
    			System.out.println("--------------------------------------------------------");
    			System.out.println("Id:         "+ tokens.get(i).id);
    			System.out.println("Type:       "+ tokens.get(i).type);
        		printError(positionState);
        		System.out.println("Position:   "+ tokens.get(i).initCharacter);
        		System.out.println("Line:       "+ tokens.get(i).line);
    			System.out.println("--------------------------------------------------------");
    			isSyntaxError = true;
    			break;
    		}
    		positionState = getError(syntacticState);	
    	}	
    	for (int i = 0; i < tokens.size(); i++) {
        	if (tokens.get(i).type == "function") {
        		System.out.println(tokens.get(i).id + " " + tokens.get(i).type);
        	}
        }
    	if (!tokens.isEmpty() && !isSyntaxError) { 
    		printSyntacticSymbolsTable();
    	}
		return isSyntaxError;
    }
}
