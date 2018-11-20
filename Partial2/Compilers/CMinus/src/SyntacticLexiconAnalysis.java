import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class SyntacticLexiconAnalysis {
	
	static List<Character> item = new ArrayList<Character>();
	static List<String> ids = new ArrayList<String>();
	static List<Character> errorItem = new ArrayList<Character>();
	static List<Character> errorType = new ArrayList<Character>();
	static List<CharacterType> characters = new ArrayList<CharacterType>();
	static List<Token> tokens = new ArrayList<Token>();
	static List<Error> errors = new ArrayList<Error>();
	static String error = "";
	static Integer linebreak = 1;
	static Integer characterLocation = 0;
	static List<String> parenthesis = new ArrayList<String>();

    private enum LexiconStates {

        Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, ERROR;
    	LexiconStates letter;
        LexiconStates number;
        LexiconStates capital;
        LexiconStates space;
        LexiconStates symbol;
        LexiconStates relational;
        LexiconStates equal;
        
    	static {
            // Initial state of the automata
            Q0.space = Q0; Q0.letter = Q1; Q0.number = Q2; Q0.symbol = Q5; Q0.equal = Q6;  Q0.relational = Q8;
            
            // Word
            Q1.space = Q0; Q1.letter = Q1; Q1.capital = Q1; Q1.number = Q1; Q1.symbol = Q5; Q1.relational = Q8;
            
            // Number or Float
            Q2.space = Q0; Q2.number = Q2;  Q2.symbol = Q5;
            
            // Symbol
            Q5.space = Q0; Q5.letter = Q1; Q5.symbol = Q5; Q5.number = Q2;
            
            // Equal
            Q6.space = Q0; Q6.equal = Q7;
            Q7.space = Q0; Q7.equal = Q0;
            
            Q8.space = Q0; Q8.equal = Q9; 
            Q9.space = Q0; Q9.relational = Q0; 
            
            ERROR.letter = ERROR; ERROR.equal = ERROR; ERROR.space = Q0;  ERROR.symbol = Q5;
        }
    	
    	/**
    	 * The transition for lexicon gets a char and according to the state 
    	 * that it is moves to the reference or goes to the default that is error 
    	 * if it doesn't identifies the type. 
    	 * @param
    	 * @return
    	 */
        LexiconStates transition(char ch) {
        	String type = "";
        	/**
        	 * Set the type after the character it's checked
        	 */
        	if (isLetter(ch)) {
        		type = "letter";
        	} else if (isNumber(ch)) {
        		type = "number";
        	} else if (isCapLetter(ch)) {
        		type = "capLetter";
        	} else if (ch == ' ') {
        		type = "space";
        	} else if (ch == '.') {
        		type = "period";
        	} else if (isSymbol(ch)) {
        		type = "symbol";
        	} else if (ch == '=') {
        		type = "equal";
        	} else if (isRelational(ch)) {
        		type = "relational";
        	}
            switch (type) {
                case "letter":    return this.letter == null? ERROR : this.letter;
                case "number":    return this.number == null? ERROR: this.number;
                case "capLetter": return this.capital == null? ERROR: this.capital;
                case "space":     return this.space == null? ERROR: this.space;
                case "symbol":    return this.symbol == null? ERROR: this.symbol;
                case "equal":     return this.equal == null? ERROR: this.equal;
                case "relational":return this.relational == null? ERROR: this.relational;
                    
                default:
                    return ERROR;
            }
        }

    }
    
    /** 
     * This function gets a char and check if is letter
     * this is used to know what type it is 
     * for the transition table for the lexicon states
     * @param ch
     * @return
     */
    public static boolean isLetter(char ch) {
    	if (ch == 'a' || ch == 'b' || ch == 'c' || ch == 'd' || ch == 'e' || ch == 'f' || ch == 'g' || ch == 'h' || ch == 'i' || 
    			ch == 'j' || ch == 'k' || ch == 'l' || ch == 'm' || ch == 'n' || ch == 'o' || ch == 'p' || ch == 'q' || ch == 'r'|| 
    			ch == 's' || ch == 't' || ch == 'u' || ch == 'v' || ch == 'w' || ch == 'x' || ch == 'y' || ch == 'z') {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /** 
     * This function gets a char and check if is a number
     * this is used to know what type it is 
     * for the transition table for the lexicon states
     * @param ch
     * @return
     */
    public static boolean isNumber(char ch) {
    	if (ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9') {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /** 
     * This function gets a char and check if is a capital letter
     * this is used to know what type it is 
     * for the transition table for the lexicon states
     * @param ch
     * @return
     */
    public static boolean isCapLetter(char ch) {
    	if (ch == 'A' || ch == 'B' || ch == 'C' || ch == 'D' || ch == 'E' || ch == 'F' || ch == 'G' || ch == 'H' || ch == 'I' || 
    			ch == 'J' || ch == 'K' || ch == 'L' || ch == 'M' || ch == 'N' || ch == 'O' || ch == 'P' || ch == 'Q' || ch == 'R' || 
    			ch == 'S' || ch == 'T' || ch == 'U' || ch == 'V' || ch == 'W' || ch == 'X' || ch == 'Y' || ch == 'Z') {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /** 
     * This function gets a char and check if is a symbol
     * this is used to know what type it is 
     * for the transition table for the lexicon states
     * @param ch
     * @return
     */
    public static boolean isSymbol(char ch) {
    	if (ch == ';' || ch == ',' || ch == '(' || ch == ')' || ch == '{' || ch == '}' || ch == '[' || ch == ']' || ch == '|' || 
    			ch == '&' || ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' ) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static boolean isRelational(char ch) {
    	if (ch == '!' || ch == '<' || ch == '>') {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static void toSyntax() {
    	String id = "";
    	while (!item.isEmpty()) {
    		id += item.remove(0);
    	}
    	if (id != "") {
        	ids.add(id);
    	}
    }
    
    public static void toSyntax(char ch) {
    	item.add(ch);
    	String id = "";
    	while (!item.isEmpty()) {
    		id += item.remove(0);
    	}
    	if (id != "") {
        	ids.add(id);
    	}
    }
    
    /**
     * This function reads a file and use US-ASCII to 
     * get every char in the file that is selected
     * It also counts the line break and character location
     * And save everything 
     * @param filename
     * @return
     */
    public static String readFile(String filename) {
    	String str = "";
		try {
			boolean space = false;
			boolean isFirst = false;
			InputStream in = new FileInputStream(filename);
			Reader r = new InputStreamReader(in, "US-ASCII");
			int intch;
			while ((intch = r.read()) != -1) {
				char aChar = (char) intch;
				characterLocation ++;
				if (aChar == ' ' || aChar == '\t' || aChar == '\r') {
					characterLocation --;
					space = true;
				} else if (aChar == '\n'){
					space = true;
					linebreak++;
					characterLocation --;
				}
				else {
					if (space) {
						if (!isFirst) { str += ""; } 
						else { str += " "; }
						space = false;
					}
					isFirst = true;
					CharacterType characterType = new CharacterType(aChar, linebreak, characterLocation); 
					characters.add(characterType);
					str += aChar;
				}
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
    
    /**
     * Get line that the characters object added when it read the file
     * @param position
     * @return
     */
    private static Integer getLine(Integer position) {
		return characters.get(position).line;
	}
    
    private static boolean isIdentifier(String id) {
    	boolean isIdentifier = false;
    	if (isLetter(id.charAt(0))) {
    		isIdentifier = true;
    		for (int i = 1; i < id.length(); i++) {
        		if (isLetter(id.charAt(i)) || isNumber(id.charAt(i)) || isCapLetter(id.charAt(i))) {
        			isIdentifier = true;
        		} else {
        			isIdentifier = false;
        		} 
        	}
    	} else {
    		isIdentifier = false;
    	}
    	return isIdentifier;
    }
   
    private static boolean isNumber(String number) {
    	boolean isNumber = false;
    	for (int i = 0; i < number.length(); i++) {
    		if (isNumber(number.charAt(i))) {
    			isNumber = true;
    		} else {
    			isNumber = false;
    		}
    	}
    	return isNumber;
    }
    
    private static String checkId(String string) {
    	switch (string) {
	        case "int": return "int";
	        case "void": return "void";
	        case "main": return "main";
	        case "if": return "if";
	        case "else": return "else";
	        case "while": return "while";
	        case "return": return "return";
	        case ",": return "coma";
	        case ";": return "semicolon";
	        case "(": return "opening-parenthesis";
	        case ")": return "closing-parenthesis";
	        case "{": return "opening-keys";
	        case "}": return "closing-keys";
	        case "[": return "opening-bracket";
	        case "]": return "closing-bracket";
	        case "+": return "add";
	        case "-": return "add";
	        case "*": return "mul";
	        case "/": return "mul";
	        case "=": return "assignation";
	        case "<": return "relational";
	        case ">": return "relational";
	        case "==": return "relational";
	        case "!=": return "relational";
	        case ">=": return "relational";
	        case "<=": return "relational";
	            
	        default:
	        	if (isIdentifier(string)) {
	        		return "identifier";
	        	} else if (isNumber(string)) {
	        		return "integer";
	        	} else {
	        		return "null";
	        	}
    	}
    }
    
    private static void lexiconAnalysis(String str) {
    	LexiconStates lexiconState = LexiconStates.Q0;
    	Integer position = 0;
    	boolean isError = false;
        for (int i = 0; i < str.length(); i++) {
            lexiconState = lexiconState.transition(str.charAt(i));
            position++;
            if (str.charAt(i) == ' ') { position--; }    
            /**
             * The initial state is reached when a space is found and 
             * the automaton is checked again
             */
            if (lexiconState == LexiconStates.Q0) {
            	/**
            	* The error state arrives here when finding a space and 
            	* checks if it's true add error to table since an error has been found
            	*/
            	if (isError) { 
            		Error errorObject = new Error(error, errorType.get(0), position, getLine(position));
            		errors.add(errorObject);
                	isError = false;
                	error = "";
                	errorType.clear();
            	} else if (str.charAt(i) == '=') {
            		if (item.size() == 2) {
                		toSyntax();
                	}
                	item.add(str.charAt(i));
            	} else {
            		/**
                	 * When is reached again to the initial state 
			    	 * The item is converted to a string and the item list is reset to 0 and 
			    	 * the string is sent to the id's list
                	 */
            		toSyntax();
            		if (str.charAt(i) != ' ') {
                		String id = String.valueOf(str.charAt(i));
                		ids.add(id);
                	}
            	}
            } 
            /** You get to a Q5 state that is the symbols */
            else if (lexiconState == LexiconStates.Q5) {
            	/**
            	 * When a symbol is found it is sent 
            	 * The item is converted to a string and the item list is reset to 0 and 
            	 * the string is sent to the id's list
            	 */
            	toSyntax();
            	/**
            	 * If another symbol is found followed by another, 
            	 * it is sent to the id's stack
            	 */
            	toSyntax(str.charAt(i));
            } else if (lexiconState == LexiconStates.Q6 || lexiconState == LexiconStates.Q8) { 
            	if (item.size() == 2) {
            		toSyntax();
            	}
            	item.add(str.charAt(i));
            } else if (lexiconState == LexiconStates.Q7 || lexiconState == LexiconStates.Q9) { 
            	if (item.size() == 2) {
            		toSyntax();
            	} 
            	item.add(str.charAt(i));
            } else if(lexiconState == LexiconStates.ERROR){
            	isError = true;
            	/**
            	 * If there is something in the stack to add to the id, 
            	 * take it out and put it inside the error 
            	 * so that it is printed since everything that is together is an error
            	 */
            	while (!item.isEmpty()) { error += item.remove(0); }
            	/** Add each error until it send to the initial state of the automata */
            	errorItem.add(str.charAt(i));
            	/** Add to the error type to know why exactly it failed */
            	errorType.add(str.charAt(i));
            	/** Add to the error string */
            	while (!errorItem.isEmpty()) { error += errorItem.remove(0); }
            	/**
            	 * If the error is in the last part of the document 
            	 * add the last error to the error list
            	 */
            	if (i == str.length()-1) {
            		/**
                	 * Get the first error added because this was the one that failed 
                	 * errorType.get(0)
                	 * Add error string to the list to be printed
                	 * errors.add(errorObject);
                	 */
                	Error errorObject = new Error(error, errorType.get(0), position-1, getLine(position-1));
            		errors.add(errorObject);
            		/** Set all the checkers to original to get more errors (if there are more) */
                	isError = false;
                	error = "";
                	errorType.clear();
            	}
            } else {
            	/** Add character to the item (new character) this is to sent to the syntax list */
            	item.add(str.charAt(i));
            }
        }
        /**
    	 * When there are no more transitions
    	 * The item is converted to a string and the item list is reset to 0 and 
    	 * Is sent to the id's list
    	 */
        toSyntax();
	}
    
	/**
	 * Print Error table where is the 
	 * error, error type, position and line
	 */
	private static void printErrorTable() {
		System.out.println("Start Error table");
		for (int i = 0; i < errors.size(); i++) {
			System.out.println("--------------------------------------------------------");
			System.out.println("Id:         " + errors.get(i).id);
        	if (isCapLetter(errors.get(i).error)) {
        		System.out.println("Error type: variables or functions can't initiate with capital Letter");
        	} else if (errors.get(i).error == '.') {
        		System.out.println("Error type: That period shouldn't be there");
        	} else {
        		System.out.println("Error type: That symbol '"+ errors.get(i).error + "' shouldn't be there or doesn't exist");
        	}
            System.out.println("Position:   " + (errors.get(i).position - errors.get(i).id.length()+1));
            System.out.println("Line:       " + errors.get(i).line);
		}
		System.out.println("--------------------------------------------------------");
		System.out.println("End Error table");
	}
	
	/** Get id's list and print token table and add to the tokens list */
	private static void printTokenTable() {
    	System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
		System.out.println("Start Tokens table");
		int positionToken = 1;
		for (int i = 0; i < ids.size(); i++) {
			positionToken += ids.get(i).length();
			System.out.println("--------------------------------------------------------");
			System.out.println("Id:       "+ ids.get(i));
			System.out.println("Type:     "+ checkId(ids.get(i)));
			System.out.println("Position: "+ (positionToken - ids.get(i).length()));
			System.out.println("Line:     "+ getLine(positionToken-1 - ids.get(i).length()));
			/** Saves token */
			Token token = new Token(ids.get(i), 
					checkId(ids.get(i)),
					(positionToken - ids.get(i).length()),
					getLine(positionToken-1 - ids.get(i).length()));
			tokens.add(token);
		}
		System.out.println("--------------------------------------------------------");
		System.out.println("End Tokens table");
	}
	/** Get tokens list and print identifiers table */
	private static void printLexicSymbolsTable() {
    	System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
		System.out.println("Start Lexic Symbols table");
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).type == "identifier" && tokens.get(i-1).type == "data-type") {
				System.out.println("--------------------------------------------------------");
    			System.out.println("Id:       "+ tokens.get(i).id);
    			System.out.println("Data type:"+ tokens.get(i-1).id);
    			System.out.println("Type:     "+ tokens.get(i).type);
    			System.out.println("Position: "+ tokens.get(i).initCharacter);
    			System.out.println("Line:     "+ tokens.get(i).line);
			}
		}
		System.out.println("--------------------------------------------------------");
		System.out.println("End Lexic Symbols table");
	}
    
    private enum SyntacticStates {
    	ERROR,
    	PROGRAM,
    	
    	/** D = Declaration */
    	/** F = Function */
    	/** V = Variable */
    	/** A = Array */
    	/** N = Number */
    	/** º = OR */
    	/** ª = Inside */
    	/** _ = Separation */
    	/** O = Open */
    	/** C = Close */
    	/** T = Type */
    	/** 5 = Multiple */
    	/** P = Parameter */
    	/** ST = Statement */
    	/** C = Compound */
    	/** L = Local */
    	/** LT = List */
    	/** E = Expression */
    	/** S = Selection */
    	/** I = Iteration */
    	/** R = Return */
    	/** ID = Id */
    	/** VD = Void */
    	/** SC = Semicolon */
    	/** REL = Relational */
    	/** MA = Multiplication and Add */
    	/** FT = Term and Factor */
    	/** AD = Additive */
    	/** AG = Arguments */
    	/** AT = Assign */
    	/** OP = Options */
    	
    	D_L, D, F_D, V_DºF_D, 
    	V_D, V_A_O, V_A_N, V_A_C,
    	T, P5, P5_LT, P, P5_ID, P5_A_ID, P5_A_C, P5_VD, 
    	C_ST, L_D, ST_LT, ST,
    	
    	FªV_ST_D, FªV_D, FªV_SCºA, FªV_A_O, FªV_A_N, FªV_A_C,
    	
    	E_ST,  	 E_ST_E, 	E_OP, 	S_ST,  	 S_ST_E, S_OP,	I_ST,	R_ST,
    	E_REL, 	 E_AD, 				S_REL, 	 S_AD,			
    	E_R_AG5, E_R_AG_LT,			S_R_AG5, S_R_AG_LT,
    	E_MA,  	 E_FT, 				S_MA,  	 S_FT,			
    	E_AG5, 	 E_AG_LT,			S_AG5, 	 S_AG_LT,		
    	E_E,						S_E,
    	
    	END;
    	
    	
    	SyntacticStates _int;
    	SyntacticStates _void;
    	SyntacticStates main;
    	SyntacticStates _if;
    	SyntacticStates _else;
    	SyntacticStates _while;
    	SyntacticStates _return;
        SyntacticStates coma;
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
        
        
    	static {
    		// TYPE
    		PROGRAM._void = D; PROGRAM._int = D;
    		D_L._void = D; D_L._int = D;
    		
    		// VARIABLE OR FUNCTION
    		D.id = V_DºF_D; 
    		
    		V_DºF_D.semicolon = D_L; // TO DECLARATION LIST
    		V_DºF_D.openingBrackets = V_A_O; // TO ARRAY OPENING
    		// TO PARAMS
    		V_DºF_D.openingParenthesis = P;
    		
    		V_A_O.integer = V_A_N;
    		V_A_N.closingBrackets = V_A_C;
    		V_A_C.semicolon = D_L;
    		
    		P._void = P5_VD;
    		P._int = P5_ID; 
    		 
    		P5._int = P5_ID; 
    		
    		P5_ID.id = P5_A_ID; 
    		P5_A_ID.openingBrackets = P5_A_C; 
    		P5_A_ID.closingParenthesis = ST; 
    		P5_A_ID.coma = P5;
    		
    		P5_A_C.closingBrackets = P5_LT;
    		P5_LT.coma = P5; 
    		P5_LT.closingParenthesis = ST;
    		P5_VD.closingParenthesis = ST;
    		
    		ST.closingParenthesis = ST; 
    		ST.openingKeys = FªV_ST_D;  
    		ST._void = FªV_D; 
    		ST._int = FªV_D;
    		ST.integer = E_ST;
    		ST.id = E_ST; 
    		ST.openingParenthesis = E_FT;
    		ST.closingKeys = FªV_ST_D;
    		ST._if = S_ST; 
    		ST._while = S_ST;
    		ST._return = R_ST; 
    		
    		FªV_ST_D._void = FªV_D; 
    		FªV_ST_D._int = FªV_D;
    		FªV_ST_D.integer = E_ST;
    		FªV_ST_D.id = E_ST; 
    		FªV_ST_D.openingParenthesis = E_FT;
    		FªV_ST_D.closingKeys = FªV_ST_D;
    		FªV_ST_D._if = S_ST; 
    		FªV_ST_D._while = S_ST;
    		FªV_ST_D._else = ST;  
    		FªV_ST_D._return = R_ST;  
    		
    		// For Expression statement
    		E_ST.assignation = E_ST_E; 
    		E_ST.mul = E_FT; 
    		E_ST.add = E_FT; 
    		E_ST.relational = E_AD;
    		E_ST.semicolon = FªV_ST_D; 
    		E_ST.openingParenthesis = E_AG_LT;
    		E_ST.openingBrackets = E_AG_LT;
    		
    		E_ST_E.id = E_OP; 
    		E_ST_E.integer = E_MA;
    		E_ST_E.openingParenthesis = E_AG_LT;
    		E_ST_E.openingBrackets = E_AG_LT;
    		
    		E_OP.semicolon = FªV_ST_D;
    		E_OP.mul = E_FT;						 
    		E_OP.add = E_FT; 																
    		E_OP.relational = E_AD;
    		E_OP.assignation = E_ST_E; 
    		E_OP.openingParenthesis = E_AG_LT;
    		E_OP.openingBrackets = E_AG_LT;
    		
    		E_FT.id = E_MA; 						E_AD.id = E_REL;
    		E_FT.integer = E_MA;					E_AD.integer = E_REL; 
    		E_FT._int = P5_ID;
    		E_FT.openingParenthesis = E_E;			E_AD.openingParenthesis = E_E;
    		E_FT._void = ST;
    		
    		E_MA.mul = E_FT;						E_REL.mul = E_AD;  
    		E_MA.add = E_FT; 						E_REL.add = E_AD;										
    		E_MA.relational = E_AD; 		 	
    		E_MA.openingParenthesis = E_AG_LT;		E_REL.openingParenthesis = E_R_AG_LT;
    		E_MA.closingParenthesis = E_OP;			E_REL.closingParenthesis = E_OP;
    		E_MA.openingBrackets = E_AG_LT;			E_REL.openingBrackets = E_R_AG_LT;
    		E_MA.closingBrackets = E_AG5;			E_REL.closingBrackets = E_R_AG5;
    		E_MA.semicolon = FªV_ST_D;				E_REL.semicolon = FªV_ST_D;
    		
    		E_AG_LT.id = E_AG5; 					E_R_AG_LT.id = E_R_AG5;
    		E_AG_LT.integer = E_AG5; 				E_R_AG_LT.integer = E_R_AG5; 
    		E_AG_LT.openingParenthesis = E_E;		E_R_AG_LT.openingParenthesis = E_E;
    		E_AG_LT.closingParenthesis = E_MA; 		E_R_AG_LT.closingParenthesis = E_REL;
    		
    		E_AG5.coma = E_AG_LT; 					E_R_AG5.coma = E_R_AG_LT; 
    		E_AG5.assignation = E_AG_LT; 			E_R_AG5.assignation = E_R_AG_LT;
    		E_AG5.mul = E_AG_LT; 					E_R_AG5.mul = E_R_AG_LT;
    		E_AG5.add = E_AG_LT; 					E_R_AG5.add = E_R_AG_LT;										
    		E_AG5.relational = E_R_AG_LT; 
    		E_AG5.openingParenthesis = E_AG_LT;		E_R_AG5.openingParenthesis = E_AG_LT;
    		E_AG5.closingParenthesis = E_AG5;		E_R_AG5.closingParenthesis = E_AG5;
    		E_AG5.openingBrackets = E_AG_LT;		E_R_AG5.openingBrackets = E_AG_LT;
    		E_AG5.closingBrackets = E_AG5;			E_R_AG5.closingBrackets = E_AG5;
    		E_AG5.semicolon = FªV_ST_D;				E_R_AG5.semicolon = FªV_ST_D;
    		
    		E_E.id = E_ST; E_E.integer = E_ST;
    		
    		// For Selection statement
    		S_ST.openingParenthesis = S_ST_E; 
    		
    		S_ST_E.id = S_OP; 
    		S_ST_E.openingParenthesis = S_AG_LT;
    		
    		S_OP.assignation = S_ST_E; 
    		S_OP.add = S_FT;
    		S_OP.mul = S_FT;
    		S_OP.relational = S_AD;
    		S_OP.openingParenthesis = S_AG_LT;
    		S_OP.closingParenthesis = ST;
    		S_OP.openingBrackets = S_AG_LT;
    		
    		S_FT.id = S_MA; 						S_AD.id = S_REL;
    		S_FT.integer = S_MA; 					S_AD.integer = S_REL; 
    		S_FT.openingParenthesis = S_E;			S_AD.openingParenthesis = S_E;
    		S_FT._void = ST;
    		
    		S_MA.mul = S_FT;						S_REL.mul = S_AD;  
    		S_MA.add = S_FT; 						S_REL.add = S_AD;										
    		S_MA.relational = S_AD; 		 	
    		S_MA.openingParenthesis = S_AG_LT;		S_REL.openingParenthesis = S_R_AG_LT;
    		S_MA.closingParenthesis = ST;			S_REL.closingParenthesis = ST;
    		S_MA.openingBrackets = S_AG_LT;			S_REL.openingBrackets = S_R_AG_LT;
    		S_MA.closingBrackets = S_AG5;			S_REL.closingBrackets = S_R_AG5;
    		
    		S_AG_LT.id = S_AG5; 					S_R_AG_LT.id = S_R_AG5;
    		S_AG_LT.integer = S_AG5; 				S_R_AG_LT.integer = S_R_AG5; 
    		S_AG_LT.openingParenthesis = S_E;		S_R_AG_LT.openingParenthesis = S_E;
    		S_AG_LT.closingParenthesis = S_MA; 		S_R_AG_LT.closingParenthesis = S_REL;
    		
    		S_AG5.coma = S_AG_LT; 					S_R_AG5.coma = S_R_AG_LT; 
    		S_AG5.assignation = S_AG_LT; 			S_R_AG5.assignation = S_R_AG_LT;
    		S_AG5.mul = S_AG_LT; 					S_R_AG5.mul = S_R_AG_LT;
    		S_AG5.add = S_AG_LT; 					S_R_AG5.add = S_R_AG_LT;										
    		S_AG5.relational = S_R_AG_LT; 
    		S_AG5.openingParenthesis = S_AG_LT;		S_R_AG5.openingParenthesis = S_AG_LT;
    		S_AG5.closingParenthesis = S_AG5;		S_R_AG5.closingParenthesis = S_AG5;
    		S_AG5.openingBrackets = S_AG_LT;		S_R_AG5.openingBrackets = S_AG_LT;
    		S_AG5.closingBrackets = S_AG5;			S_R_AG5.closingBrackets = S_AG5;
    		
    		S_E.id = S_ST; S_E.integer = S_ST;
    		
    		// For Return statement
    		R_ST.id = E_ST; 
    		R_ST.integer = E_ST;
    				
    		// Variable declarations inside function
    		FªV_D.id = FªV_SCºA; 
    		FªV_D.main = FªV_SCºA;
    		FªV_SCºA.semicolon = FªV_ST_D; 
    		FªV_SCºA.openingParenthesis = E_FT;
    		FªV_SCºA.openingBrackets = FªV_A_O;
    		
    		FªV_A_O.integer = FªV_A_N;
    		FªV_A_N.closingBrackets = FªV_A_C;
    		FªV_A_C.semicolon = FªV_ST_D;
    		
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
                case "int": 				return this._int == null? ERROR : this._int;
                case "void": 				return this._void == null? ERROR : this._void;
                case "main":      			return this.main == null? ERROR: this.main;
                case "if":       			return this._if == null? ERROR: this._if;
                case "else":       			return this._else == null? ERROR: this._else;
                case "while":     			return this._while == null? ERROR: this._while;
                case "return":    			return this._return == null? ERROR: this._return;
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
    
    private static void printUnbalancedParenthesisError(int i) {
		System.out.println("--------------------------------------------------------");
		System.out.println("Syntactic Error");
		System.out.println("--------------------------------------------------------");
		System.out.println("Id:         "+ tokens.get(i).id);
		System.out.println("Type:       "+ tokens.get(i).type);
		System.out.println("Error type: Unbalanced parenthesis  ");
		System.out.println("Position:   "+ tokens.get(i).initCharacter);
		System.out.println("Line:       "+ tokens.get(i).line);
		System.out.println("--------------------------------------------------------");
	}
    
    private static void printNoMain() {
    	System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
		System.out.println("Syntactic Error");
		System.out.println("--------------------------------------------------------");
		System.out.println("ErrorType: Code doesn't has main");
		System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");
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
			if ((tokens.get(i).type == "variable" || tokens.get(i).type == "function") && tokens.get(i-1).type == "data-type") {
				System.out.println("--------------------------------------------------------");
    			System.out.println("Id:       "+ tokens.get(i).id);
    			System.out.println("Data type:"+ tokens.get(i-1).id);
    			System.out.println("Type:     "+ tokens.get(i).type);
    			System.out.println("Position: "+ tokens.get(i).initCharacter);
    			System.out.println("Line:     "+ tokens.get(i).line);
			}
		}
		System.out.println("--------------------------------------------------------");
		System.out.println("End Syntatic symbol table");
    }
    
    private static void syntacticAnalysis() {
    	boolean isSyntaxError = false;
    	boolean balance = true;
    	boolean hasMain = false;
    	int knowMain = 0;
    	SyntacticStates syntacticState = SyntacticStates.PROGRAM;
    	int positionState = 0;
    	for (int i = 0; i < tokens.size(); i++) {
    		knowMain++;
    		syntacticState = syntacticState.transition(tokens.get(i).type);
    		System.out.println(syntacticState + " " + tokens.get(i).id + " " + tokens.get(i).type);
			if (tokens.get(i).type == "opening-parenthesis" && tokens.get(i-1).type == "identifier") {
				tokens.get(i-1).type = "function";
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
    		/** 
    		 * If there is an error it stops and doesn't reaches to the end so maybe it wont find if it has main 
    		 * this is checked after all the analysis is over
    		 */
    		if (tokens.get(i).type == "main") { hasMain = true; }
    	}	
    	for (int i = 0; i < tokens.size(); i++) {
        	if (tokens.get(i).type == "function") {
        		System.out.println(tokens.get(i).id + " " + tokens.get(i).type);
        	}
        }
    	if (!hasMain && knowMain == tokens.size()) { printNoMain(); }
    	if (!tokens.isEmpty() && !isSyntaxError && balance && hasMain) { 
    		printSyntacticSymbolsTable();
	    	ThreeDirectionsParser parser = new ThreeDirectionsParser();
	    	parser.tokens = tokens;
	    	parser.doThreeDirectionsParser();
    	}
    }

   
    /**
     * Main program 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
		String str = "";
    	str = readFile("Archivo_fuente.txt");
    	/** Make lexicon analysis */
    	lexiconAnalysis(str);
        /**
         * Print errors if it there are 
         * Also prints the type of error if it recognizes it
         * Prints line and initial character of the error
         */
        if (!errors.isEmpty()) { printErrorTable(); } 
        /**
         * Shows the tokens table, symbol and
         * Go the syntactic analysis
         */
        else {
        	if (!ids.isEmpty()) { printTokenTable(); }
        	if (!tokens.isEmpty()) { printLexicSymbolsTable(); }
        	syntacticAnalysis();
        	
        }   	
        
    }	
}