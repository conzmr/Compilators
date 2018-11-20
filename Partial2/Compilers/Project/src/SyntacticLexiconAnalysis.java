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

        Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, ERROR;
    	LexiconStates letter;
        LexiconStates number;
        LexiconStates capital;
        LexiconStates space;
        LexiconStates period;
        LexiconStates symbol;
        LexiconStates equal;
        
    	static {
            // Initial state of the automata
            Q0.space = Q0; Q0.letter = Q1; Q0.number = Q2; Q0.symbol = Q5; Q0.equal = Q6;  
            
            // Word
            Q1.space = Q0; Q1.letter = Q1; Q1.capital = Q1; Q1.number = Q1; Q1.symbol = Q5;
            
            // Number or Float
            Q2.space = Q0; Q2.number = Q2; Q2.period = Q3;  Q2.symbol = Q5;
            Q3.number = Q4;
            Q4.space = Q0; Q4.number = Q4; Q4.symbol = Q5;
            
            // Symbol
            Q5.space = Q0; Q5.letter = Q1; Q5.symbol = Q5; Q5.number = Q2;
            
            // Equal
            Q6.space = Q0; Q6.equal = Q7;
            Q7.space = Q0; Q7.equal = Q0;
            
            ERROR.letter = ERROR; ERROR.equal = ERROR; ERROR.space = Q0;  ERROR.symbol = Q5;
        }
    	
    	/**
    	 * The transition for lexicon gets a char and according to the state 
    	 * that it is moves to the reference or goes to the default that is error 
    	 * if it doesn't identifies the type. 
    	 * @param st
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
        	}
            switch (type) {
                case "letter":    return this.letter == null? ERROR : this.letter;
                case "number":    return this.number == null? ERROR: this.number;
                case "capLetter": return this.capital == null? ERROR: this.capital;
                case "space":     return this.space == null? ERROR: this.space;
                case "period":    return this.period == null? ERROR: this.period;
                case "symbol":    return this.symbol == null? ERROR: this.symbol;
                case "equal":     return this.equal == null? ERROR: this.equal;
                    
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
    	if (ch == 'a' || ch == 'b' || ch == 'c' || ch == 'd' || ch == 'e' || ch == 'f' || ch == 'g' || ch == 'h' || ch == 'i' || ch == 'j' ||
    			ch == 'k' || ch == 'l' || ch == 'm' || ch == 'n' || ch == 'o' || ch == 'p' || ch == 'q' || ch == 'r'|| ch == 's' || 
    			ch == 't' || ch == 'u' || ch == 'v' || ch == 'w' || ch == 'x' || ch == 'y' || ch == 'z') {
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
    	if (ch == 'A' || ch == 'B' || ch == 'C' || ch == 'D' || ch == 'E' || ch == 'F' || ch == 'G' || ch == 'H' || ch == 'I' || ch == 'J' ||
    			ch == 'K' || ch == 'L' || ch == 'M' || ch == 'N' || ch == 'O' || ch == 'P' || ch == 'Q' || ch == 'R' || ch == 'S' || 
    			ch == 'T' || ch == 'U' || ch == 'V' || ch == 'W' || ch == 'X' || ch == 'Y' || ch == 'Z') {
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
    	if (ch == ';' || ch == ',' || ch == '(' || ch == ')' || ch == '{' || ch == '}' || ch == '!' || ch == '|' || ch == '&' || ch == '+' || 
    			ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '<' || ch == '>') {
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
	        case "entero": return "data-type";
	        case "logico": return "data-type";
	        case "real": return "data-type";
	        case "principal": return "main";
	        case "si": return "if";
	        case "mientras": return "while";
	        case "regresa": return "return";
	        case "verdadero": return "boolean";
	        case "falso": return "boolean";
	        case ",": return "coma";
	        case ";": return "semicolon";
	        case "(": return "opening-parenthesis";
	        case ")": return "closing-parenthesis";
	        case "{": return "opening-keys";
	        case "}": return "closing-keys";
	        case "+": return "arithmetic";
	        case "-": return "arithmetic";
	        case "*": return "arithmetic";
	        case "/": return "arithmetic";
	        case "^": return "arithmetic";
	        case "=": return "assignation";
	        case "&": return "logical";
	        case "|": return "logical";
	        case "!": return "denial";
	        case "<": return "relational";
	        case ">": return "relational";
	        case "==": return "relational";
	            
	        default:
	        	if (isIdentifier(string)) {
	        		return "identifier";
	        	} else if (isNumber(string)) {
	        		return "integer";
	        	} else {
	        		return "float";
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
            } else if (lexiconState == LexiconStates.Q6) { 
            	if (item.size() == 2) {
            		toSyntax();
            	}
            	item.add(str.charAt(i));
            } else if (lexiconState == LexiconStates.Q7) { 
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
        FUNC_DATA, FUNC, FUNC_OPEN_P, FUNC_OPT_P_D, FUNC_OPEN_DATA, FUNC_VAR, FUNC_OPT_C_P, FUNC_OPEN_K, FUNC_OPT_K_D, 
        FUNC_CLOSE_K,
        
    	DECLARATIONS_ID, DECLARATIONS_SEMICOLON, DECLARATIONS_OPT_D_O,
    	
    	OPERATIONS_OPT_I_OP, OPERATIONS_ASSIGNMENT, OPERATIONS, OPERATIONS_OPT_S_A_L, OPERATIONS_OPT_I_R, OPERATIONS_CP, 
    	OPERATIONS_OPT_A_L_P, OPERATIONS_OPT_S_CP, OPERATIONS_FUNC_PC, OPERATIONS_R, OPERATIONS_S, OPERATIONS_ID,
    	
    	OPERATIONS_OPT_I_OP_A, OPERATIONS_OPT_A_P, OPERATIONS_I_A_CP, OPERATIONS_A_CP, OPERATIONS_OPT_S_CP_A, OPERATIONS_CP_A, 
    	OPERATIONS_FUNC_PC_A,
    	
    	OPERATIONS_OPT_I_OP_L, OPERATIONS_OPT_L_P, OPERATIONS_I_L_CP, OPERATIONS_L_CP, OPERATIONS_OPT_S_CP_L, OPERATIONS_CP_L, 
    	OPERATIONS_FUNC_PC_L, OPERATIONS_B,
    	
    	IF_OP, IF_ID, IF_CP, IF_OK, IF_OPT_OP_CK,
    	WHILE_OP, WHILE_ID, WHILE_CP, WHILE_OK, WHILE_OPT_OP_CK,
    	WHILE_IF_CLOSE_K,
    	
    	DENIAL_ID, DENIAL_SEMICOLON,
    	
    	RETURN_ID, RETURN_SEMICOLON,
    	
    	MAIN_OP, MAIN_CP, MAIN_OK, MAIN_OPT_K_D,
    	
    	M_DECLARATIONS_ID, M_DECLARATIONS_SEMICOLON, M_DECLARATIONS_OPT_D_O,
    	
    	M_OPERATIONS_OPT_I_OP, M_OPERATIONS_ASSIGNMENT, M_OPERATIONS, M_OPERATIONS_OPT_S_A_L, M_OPERATIONS_OPT_I_R, M_OPERATIONS_CP, 
    	M_OPERATIONS_OPT_A_L_P, M_OPERATIONS_OPT_S_CP, M_OPERATIONS_FUNC_PC, M_OPERATIONS_R, M_OPERATIONS_S, M_OPERATIONS_ID,
    	
    	M_OPERATIONS_OPT_I_OP_A, M_OPERATIONS_OPT_A_P, M_OPERATIONS_I_A_CP, M_OPERATIONS_A_CP, M_OPERATIONS_OPT_S_CP_A, M_OPERATIONS_CP_A,
    	M_OPERATIONS_FUNC_PC_A,
    	
    	M_OPERATIONS_OPT_I_OP_L, M_OPERATIONS_OPT_L_P, M_OPERATIONS_I_L_CP, M_OPERATIONS_L_CP, M_OPERATIONS_OPT_S_CP_L, M_OPERATIONS_CP_L,
    	M_OPERATIONS_FUNC_PC_L, M_OPERATIONS_B,
    	
    	M_IF_OP, M_IF_ID, M_IF_CP, M_IF_OK, M_IF_OPT_OP_CK,
    	M_WHILE_OP, M_WHILE_ID, M_WHILE_CP, M_WHILE_OK, M_WHILE_OPT_OP_CK,
    	M_WHILE_IF_CLOSE_K,
    	
    	M_DENIAL_ID, M_DENIAL_SEMICOLON, 
    	
    	END;
    	
    	SyntacticStates dataType;
    	SyntacticStates main;
    	SyntacticStates _if;
    	SyntacticStates _while;
    	SyntacticStates _return;
        SyntacticStates _boolean;
        SyntacticStates coma;
        SyntacticStates semicolon;
        SyntacticStates openingParenthesis;
        SyntacticStates closingParenthesis;
        SyntacticStates openingKeys;
        SyntacticStates closingKeys;
        SyntacticStates arithmetic;
        SyntacticStates logical;
        SyntacticStates relational;
        SyntacticStates assignation;
        SyntacticStates denial;
        SyntacticStates integer;
        SyntacticStates _float;
        SyntacticStates id;
        
        
    	static {
    		/** Read main or initiation of function */
    		FUNC_DATA.dataType = FUNC; FUNC_DATA.main = MAIN_OP;
    		/** Read id of function */
    		FUNC.id = FUNC_OPEN_P;
    		/** Read '(' of function */
    		FUNC_OPEN_P.openingParenthesis = FUNC_OPT_P_D; 
    		/** Read ')' or new parameter data-type function */
    		FUNC_OPT_P_D.dataType = FUNC_VAR; FUNC_OPT_P_D.closingParenthesis = FUNC_OPEN_K;
    		/** Read new parameter data-type after ',' */
    		FUNC_OPEN_DATA.dataType = FUNC_VAR;
    		/** Read id of new parameter data-type function */
    		FUNC_VAR.id = FUNC_OPT_C_P; 
    		/** Read ',' or ')' function */
    		FUNC_OPT_C_P.closingParenthesis = FUNC_OPEN_K; FUNC_OPT_C_P.coma = FUNC_OPEN_DATA;
    		/** Read '{' function */
    		FUNC_OPEN_K.openingKeys = FUNC_OPT_K_D;
    		/** Read '}' to close function, data-type for new variable, return or id for new assignment */
    		FUNC_OPT_K_D.dataType = DECLARATIONS_ID; FUNC_OPT_K_D._return = RETURN_ID; FUNC_OPT_K_D.id = OPERATIONS_ASSIGNMENT;
    		
    		// DECLARATIONS
    		/** Read id for new variable */
    		DECLARATIONS_ID.id = DECLARATIONS_SEMICOLON;
    		/** Read ';' for new variable */
    		DECLARATIONS_SEMICOLON.semicolon = DECLARATIONS_OPT_D_O;
    		/** Read data-type for new variable, id for new assignment, if, while or return */
    		DECLARATIONS_OPT_D_O.dataType = DECLARATIONS_ID; DECLARATIONS_OPT_D_O.id = OPERATIONS_ASSIGNMENT;
    		DECLARATIONS_OPT_D_O._if = IF_OP; DECLARATIONS_OPT_D_O._while = WHILE_OP; DECLARATIONS_OPT_D_O._return = RETURN_ID;
    		
    		// OPERATIONS
    		/** Read '=' for new variable */
    		OPERATIONS_ASSIGNMENT.assignation = OPERATIONS;
    		/** Read id, '(', '!', integer, float, boolean */
    		OPERATIONS.id = OPERATIONS_ID; 
    		OPERATIONS.openingParenthesis = OPERATIONS_OPT_I_OP; 
    		OPERATIONS.denial = DENIAL_ID;
    		OPERATIONS.integer = OPERATIONS_OPT_S_A_L; 
    		OPERATIONS._float = OPERATIONS_OPT_S_A_L;
    		OPERATIONS._boolean = OPERATIONS_B;
    		/** Read ';' to end assignment, relational or arithmetic symbol */
    		OPERATIONS_OPT_S_A_L.semicolon = OPERATIONS_OPT_I_R; 
    		OPERATIONS_OPT_S_A_L.relational = OPERATIONS_R;
    		OPERATIONS_OPT_S_A_L.arithmetic = OPERATIONS_OPT_I_OP_A;
    		/** Read ';', '(', logical, relational or arithmetic symbol */
    		OPERATIONS_ID.semicolon = OPERATIONS_OPT_I_R; 
    		OPERATIONS_ID.arithmetic = OPERATIONS_OPT_I_OP_A;
    		OPERATIONS_ID.logical = OPERATIONS_OPT_I_OP_L; 
    		OPERATIONS_ID.openingParenthesis = OPERATIONS_FUNC_PC;
    		OPERATIONS_ID.relational = OPERATIONS_R;
    		/** Read ')' for ending functions */
    		OPERATIONS_FUNC_PC.closingParenthesis = OPERATIONS_OPT_A_L_P;
    		/** Read after '(' an id, '(', integer, float or boolean */
    		OPERATIONS_OPT_I_OP.id = OPERATIONS_CP; 
    		OPERATIONS_OPT_I_OP.openingParenthesis = OPERATIONS_OPT_I_OP;
    		OPERATIONS_OPT_I_OP.integer = OPERATIONS_OPT_A_L_P; 
    		OPERATIONS_OPT_I_OP._float = OPERATIONS_OPT_A_L_P;
    		OPERATIONS_OPT_I_OP._boolean = OPERATIONS_CP_L;
    		/** Read after ')' an ')', ';' or arithmetic symbol */
    		OPERATIONS_OPT_A_L_P.arithmetic = OPERATIONS_I_A_CP; 
    		OPERATIONS_OPT_A_L_P.closingParenthesis = OPERATIONS_OPT_S_CP;
    		OPERATIONS_OPT_A_L_P.semicolon = OPERATIONS_OPT_I_R; 
    		/** Read after ')' an ')', ';' or arithmetic symbol */
    		OPERATIONS_OPT_S_CP.closingParenthesis = OPERATIONS_OPT_S_CP; 
    		OPERATIONS_OPT_S_CP.semicolon = OPERATIONS_OPT_I_R; 
    		OPERATIONS_OPT_S_CP.arithmetic = OPERATIONS_OPT_I_OP_A;
    		/** Read after id an ')', ';', logical or arithmetic symbol */
    		OPERATIONS_CP.closingParenthesis = OPERATIONS_OPT_S_CP; 
    		OPERATIONS_CP.semicolon = OPERATIONS_OPT_I_R; 
    		OPERATIONS_CP.openingParenthesis = OPERATIONS_FUNC_PC; 
    		OPERATIONS_CP.arithmetic = OPERATIONS_I_A_CP;
    		OPERATIONS_CP.logical = OPERATIONS_I_L_CP;
    		
    		// Relational
    		
    		OPERATIONS_R.integer = OPERATIONS_S; OPERATIONS_R._float = OPERATIONS_S;  OPERATIONS_R.id = OPERATIONS_S; 
    		
    		OPERATIONS_S.semicolon = OPERATIONS_OPT_I_R;
    		
    		// Arithmetic
    		
    		OPERATIONS_OPT_I_OP_A.id = OPERATIONS_CP_A; 
    		OPERATIONS_OPT_I_OP_A.openingParenthesis = OPERATIONS_OPT_I_OP_A;
    		OPERATIONS_OPT_I_OP_A.integer = OPERATIONS_OPT_A_P; 
    		OPERATIONS_OPT_I_OP_A._float = OPERATIONS_OPT_A_P;
    		
    		OPERATIONS_OPT_A_P.arithmetic = OPERATIONS_I_A_CP; 
    		OPERATIONS_OPT_A_P.closingParenthesis = OPERATIONS_OPT_S_CP_A;
    		OPERATIONS_OPT_A_P.semicolon = OPERATIONS_OPT_I_R; 
    		
    		OPERATIONS_I_A_CP.id = OPERATIONS_CP_A; OPERATIONS_I_A_CP.integer = OPERATIONS_A_CP; OPERATIONS_I_A_CP._float = OPERATIONS_A_CP;

    		OPERATIONS_A_CP.closingParenthesis = OPERATIONS_OPT_S_CP_A; 
    		OPERATIONS_A_CP.semicolon = OPERATIONS_OPT_I_R; 
    		OPERATIONS_A_CP.arithmetic = OPERATIONS_I_A_CP;
    		
    		OPERATIONS_OPT_S_CP_A.closingParenthesis = OPERATIONS_OPT_S_CP_A; 
    		OPERATIONS_OPT_S_CP_A.semicolon = OPERATIONS_OPT_I_R; 
    		OPERATIONS_OPT_S_CP_A.arithmetic = OPERATIONS_OPT_I_OP_A;
    		
    		OPERATIONS_CP_A.closingParenthesis = OPERATIONS_OPT_S_CP_A; 
    		OPERATIONS_CP_A.semicolon = OPERATIONS_OPT_I_R; 
    		OPERATIONS_CP_A.openingParenthesis = OPERATIONS_FUNC_PC_A; 
    		OPERATIONS_CP_A.arithmetic = OPERATIONS_I_A_CP;

    		OPERATIONS_FUNC_PC_A.closingParenthesis = OPERATIONS_OPT_A_P;
    		
    		// Logical 
    		
    		OPERATIONS_B.logical = OPERATIONS_I_L_CP;
    		OPERATIONS_B.semicolon = OPERATIONS_OPT_I_R;
    		
    		OPERATIONS_OPT_I_OP_L.id = OPERATIONS_CP_L; 
    		OPERATIONS_OPT_I_OP_L.openingParenthesis = OPERATIONS_OPT_I_OP_L;
    		OPERATIONS_OPT_I_OP_L._boolean = OPERATIONS_OPT_L_P; 
    		
    		OPERATIONS_OPT_L_P.closingParenthesis = OPERATIONS_OPT_S_CP_L;
    		OPERATIONS_OPT_L_P.logical = OPERATIONS_I_L_CP; 
    		OPERATIONS_OPT_L_P.semicolon = OPERATIONS_OPT_I_R; 
    		
    		OPERATIONS_I_L_CP.id = OPERATIONS_CP_L; OPERATIONS_I_L_CP._boolean = OPERATIONS_L_CP;
    		
    		OPERATIONS_L_CP.closingParenthesis = OPERATIONS_OPT_S_CP_L; 
    		OPERATIONS_L_CP.semicolon = OPERATIONS_OPT_I_R; 
    		OPERATIONS_L_CP.logical = OPERATIONS_I_L_CP;
	
    		OPERATIONS_OPT_S_CP_L.closingParenthesis = OPERATIONS_OPT_S_CP_L; 
    		OPERATIONS_OPT_S_CP_L.semicolon = OPERATIONS_OPT_I_R; 
    		OPERATIONS_OPT_S_CP_L.logical = OPERATIONS_OPT_I_OP_L;
    		
    		OPERATIONS_CP_L.closingParenthesis = OPERATIONS_OPT_S_CP_L; 
    		OPERATIONS_CP_L.semicolon = OPERATIONS_OPT_I_R; 
    		OPERATIONS_CP_L.openingParenthesis = OPERATIONS_FUNC_PC_L;
    		OPERATIONS_CP_L.logical = OPERATIONS_I_L_CP;

    		OPERATIONS_FUNC_PC_L.closingParenthesis = OPERATIONS_OPT_L_P;
    		
    		IF_OP.openingParenthesis = IF_ID;
    		IF_ID.id = IF_CP;
    		IF_CP.closingParenthesis = IF_OK;
    		IF_OK.openingKeys = IF_OPT_OP_CK;
    		IF_OPT_OP_CK.closingKeys = OPERATIONS_OPT_I_R; IF_OPT_OP_CK.id = OPERATIONS_ASSIGNMENT; 
    		IF_OPT_OP_CK._if = IF_OP; IF_OPT_OP_CK._while = WHILE_OP;
    		
    		WHILE_OP.openingParenthesis = WHILE_ID;
    		WHILE_ID.id = WHILE_CP;
    		WHILE_CP.closingParenthesis = WHILE_OK;
    		WHILE_OK.openingKeys = WHILE_OPT_OP_CK;
    		WHILE_OPT_OP_CK.closingKeys = OPERATIONS_OPT_I_R; WHILE_OPT_OP_CK.id = OPERATIONS_ASSIGNMENT; 
    		WHILE_OPT_OP_CK._if = IF_OP; WHILE_OPT_OP_CK._while = WHILE_OP;
    		
    		DENIAL_ID.id = DENIAL_SEMICOLON;
    		DENIAL_SEMICOLON.semicolon = OPERATIONS_OPT_I_R;
    		
    		OPERATIONS_OPT_I_R.id = OPERATIONS_ASSIGNMENT; OPERATIONS_OPT_I_R._if = IF_OP; OPERATIONS_OPT_I_R._while = WHILE_OP; 
    		OPERATIONS_OPT_I_R._return = RETURN_ID; OPERATIONS_OPT_I_R.closingKeys = WHILE_IF_CLOSE_K;
    		
    		WHILE_IF_CLOSE_K.id = OPERATIONS_ASSIGNMENT; WHILE_IF_CLOSE_K._if = IF_OP; WHILE_IF_CLOSE_K._while = WHILE_OP; 
    		WHILE_IF_CLOSE_K._return = RETURN_ID; WHILE_IF_CLOSE_K.closingKeys = OPERATIONS_OPT_I_R;
    		
    		// RETURN
    		
    		RETURN_ID.id = RETURN_SEMICOLON; RETURN_ID._boolean = RETURN_SEMICOLON; RETURN_ID.integer = RETURN_SEMICOLON; 
    		RETURN_ID._float = RETURN_SEMICOLON;
    		
    		RETURN_SEMICOLON.semicolon = FUNC_CLOSE_K;
    		
    		FUNC_CLOSE_K.closingKeys = FUNC_DATA;
    		
    		// MAIN
    		
    		MAIN_OP.openingParenthesis = MAIN_CP;
    		
    		MAIN_CP.closingParenthesis = MAIN_OK;
    		
    		MAIN_OK.openingKeys = MAIN_OPT_K_D;
    		
    		MAIN_OPT_K_D.closingKeys = END; MAIN_OPT_K_D.dataType = M_DECLARATIONS_ID;
    		
    		
    		// DECLARATIONS MAIN
    		
    		M_DECLARATIONS_ID.id = M_DECLARATIONS_SEMICOLON;
    		
    		M_DECLARATIONS_SEMICOLON.semicolon = M_DECLARATIONS_OPT_D_O;
    		
    		M_DECLARATIONS_OPT_D_O.dataType = M_DECLARATIONS_ID; M_DECLARATIONS_OPT_D_O.id = M_OPERATIONS_ASSIGNMENT;
    		M_DECLARATIONS_OPT_D_O._if = M_IF_OP; M_DECLARATIONS_OPT_D_O._while = M_WHILE_OP; 
    		
    		// OPERATIONS MAIN
    		
    		M_OPERATIONS_ASSIGNMENT.assignation = M_OPERATIONS;
    		
    		M_OPERATIONS.id = M_OPERATIONS_ID; 
    		M_OPERATIONS.openingParenthesis = M_OPERATIONS_OPT_I_OP; 
    		M_OPERATIONS.denial = M_DENIAL_ID;
    		M_OPERATIONS.integer = M_OPERATIONS_OPT_S_A_L; 
    		M_OPERATIONS._float = M_OPERATIONS_OPT_S_A_L;
    		M_OPERATIONS._boolean = M_OPERATIONS_B;
    		
    		M_OPERATIONS_OPT_S_A_L.semicolon = M_OPERATIONS_OPT_I_R; 
    		M_OPERATIONS_OPT_S_A_L.relational = M_OPERATIONS_R;
    		M_OPERATIONS_OPT_S_A_L.arithmetic = M_OPERATIONS_OPT_I_OP_A;
    		
    		M_OPERATIONS_ID.semicolon = M_OPERATIONS_OPT_I_R; 
    		M_OPERATIONS_ID.arithmetic = M_OPERATIONS_OPT_I_OP_A;
    		M_OPERATIONS_ID.logical = M_OPERATIONS_OPT_I_OP_L; 
    		M_OPERATIONS_ID.openingParenthesis = M_OPERATIONS_FUNC_PC;
    		M_OPERATIONS_ID.relational = M_OPERATIONS_R;
    		
    		M_OPERATIONS_FUNC_PC.closingParenthesis = M_OPERATIONS_OPT_A_L_P;
    		
    		M_OPERATIONS_OPT_I_OP.id = M_OPERATIONS_CP; 
    		M_OPERATIONS_OPT_I_OP.openingParenthesis = M_OPERATIONS_OPT_I_OP;
    		M_OPERATIONS_OPT_I_OP.integer = M_OPERATIONS_OPT_A_L_P; 
    		M_OPERATIONS_OPT_I_OP._float = M_OPERATIONS_OPT_A_L_P;
    		M_OPERATIONS_OPT_I_OP._boolean = M_OPERATIONS_CP_L;
    		
    		M_OPERATIONS_OPT_A_L_P.arithmetic = M_OPERATIONS_I_A_CP; 
    		M_OPERATIONS_OPT_A_L_P.closingParenthesis = M_OPERATIONS_OPT_S_CP;
    		M_OPERATIONS_OPT_A_L_P.semicolon = M_OPERATIONS_OPT_I_R; 
    		    		
    		M_OPERATIONS_OPT_S_CP.closingParenthesis = M_OPERATIONS_OPT_S_CP; 
    		M_OPERATIONS_OPT_S_CP.semicolon = M_OPERATIONS_OPT_I_R; 
    		M_OPERATIONS_OPT_S_CP.arithmetic = M_OPERATIONS_OPT_I_OP_A;
    		
    		M_OPERATIONS_CP.closingParenthesis = M_OPERATIONS_OPT_S_CP; 
    		M_OPERATIONS_CP.semicolon = M_OPERATIONS_OPT_I_R; 
    		M_OPERATIONS_CP.openingParenthesis = M_OPERATIONS_FUNC_PC; 
    		M_OPERATIONS_CP.arithmetic = M_OPERATIONS_I_A_CP;
    		M_OPERATIONS_CP.logical = M_OPERATIONS_I_L_CP;
    		
    		// Relational
    		
    		M_OPERATIONS_R.integer = M_OPERATIONS_S; M_OPERATIONS_R._float = M_OPERATIONS_S;  M_OPERATIONS_R.id = M_OPERATIONS_S; 
    		
    		M_OPERATIONS_S.semicolon = M_OPERATIONS_OPT_I_R;
    		
    		// Arithmetic
    		
    		M_OPERATIONS_OPT_I_OP_A.id = M_OPERATIONS_CP_A; 
    		M_OPERATIONS_OPT_I_OP_A.openingParenthesis = M_OPERATIONS_OPT_I_OP_A;
    		M_OPERATIONS_OPT_I_OP_A.integer = M_OPERATIONS_OPT_A_P; 
    		M_OPERATIONS_OPT_I_OP_A._float = M_OPERATIONS_OPT_A_P;
    		
    		M_OPERATIONS_OPT_A_P.arithmetic = M_OPERATIONS_I_A_CP; 
    		M_OPERATIONS_OPT_A_P.closingParenthesis = M_OPERATIONS_OPT_S_CP_A;
    		M_OPERATIONS_OPT_A_P.semicolon = M_OPERATIONS_OPT_I_R; 
    		
    		M_OPERATIONS_I_A_CP.id = M_OPERATIONS_CP_A; M_OPERATIONS_I_A_CP.integer = M_OPERATIONS_A_CP; 
    		M_OPERATIONS_I_A_CP._float = M_OPERATIONS_A_CP;

    		M_OPERATIONS_A_CP.closingParenthesis = M_OPERATIONS_OPT_S_CP_A; 
    		M_OPERATIONS_A_CP.semicolon = M_OPERATIONS_OPT_I_R; 
    		M_OPERATIONS_A_CP.arithmetic = M_OPERATIONS_I_A_CP;
    		
    		M_OPERATIONS_OPT_S_CP_A.closingParenthesis = M_OPERATIONS_OPT_S_CP_A; 
    		M_OPERATIONS_OPT_S_CP_A.semicolon = M_OPERATIONS_OPT_I_R; 
    		M_OPERATIONS_OPT_S_CP_A.arithmetic = M_OPERATIONS_OPT_I_OP_A;
    		
    		M_OPERATIONS_CP_A.closingParenthesis = M_OPERATIONS_OPT_S_CP_A; 
    		M_OPERATIONS_CP_A.semicolon = M_OPERATIONS_OPT_I_R; 
    		M_OPERATIONS_CP_A.openingParenthesis = M_OPERATIONS_FUNC_PC_A; 
    		M_OPERATIONS_CP_A.arithmetic = M_OPERATIONS_I_A_CP;

    		M_OPERATIONS_FUNC_PC_A.closingParenthesis = M_OPERATIONS_OPT_A_P;
    		
    		// Logical 
    		
    		M_OPERATIONS_B.logical = M_OPERATIONS_I_L_CP;
    		M_OPERATIONS_B.semicolon = M_OPERATIONS_OPT_I_R;
    		
    		M_OPERATIONS_OPT_I_OP_L.id = M_OPERATIONS_CP_L; 
    		M_OPERATIONS_OPT_I_OP_L.openingParenthesis = M_OPERATIONS_OPT_I_OP_L;
    		M_OPERATIONS_OPT_I_OP_L._boolean = M_OPERATIONS_OPT_L_P;
    		
    		M_OPERATIONS_OPT_L_P.closingParenthesis = M_OPERATIONS_OPT_S_CP_L;
    		M_OPERATIONS_OPT_L_P.logical = M_OPERATIONS_I_L_CP; 
    		M_OPERATIONS_OPT_L_P.semicolon = M_OPERATIONS_OPT_I_R; 
    		
    		M_OPERATIONS_I_L_CP.id = M_OPERATIONS_CP_L; M_OPERATIONS_I_L_CP._boolean = M_OPERATIONS_L_CP; 
    		
    		M_OPERATIONS_L_CP.closingParenthesis = M_OPERATIONS_OPT_S_CP_L; 
    		M_OPERATIONS_L_CP.semicolon = M_OPERATIONS_OPT_I_R; 
    		M_OPERATIONS_L_CP.logical = M_OPERATIONS_I_L_CP;
	
    		M_OPERATIONS_OPT_S_CP_L.closingParenthesis = M_OPERATIONS_OPT_S_CP_L; 
    		M_OPERATIONS_OPT_S_CP_L.semicolon = M_OPERATIONS_OPT_I_R; 
    		M_OPERATIONS_OPT_S_CP_L.logical = M_OPERATIONS_OPT_I_OP_L;
    		
    		M_OPERATIONS_CP_L.closingParenthesis = M_OPERATIONS_OPT_S_CP_L; 
    		M_OPERATIONS_CP_L.semicolon = M_OPERATIONS_OPT_I_R; 
    		M_OPERATIONS_CP_L.openingParenthesis = M_OPERATIONS_FUNC_PC_L;
    		M_OPERATIONS_CP_L.logical = M_OPERATIONS_I_L_CP;
    		
    		M_OPERATIONS_FUNC_PC_L.closingParenthesis = M_OPERATIONS_OPT_L_P;
    		
    		M_IF_OP.openingParenthesis = M_IF_ID;
    		M_IF_ID.id = M_IF_CP;
    		M_IF_CP.closingParenthesis = M_IF_OK;
    		M_IF_OK.openingKeys = M_IF_OPT_OP_CK;
    		M_IF_OPT_OP_CK.closingKeys = M_OPERATIONS_OPT_I_R; M_IF_OPT_OP_CK.id = M_OPERATIONS_ASSIGNMENT; 
    		M_IF_OPT_OP_CK._if = M_IF_OP; M_IF_OPT_OP_CK._while = M_WHILE_OP;
    		
    		M_WHILE_OP.openingParenthesis = M_WHILE_ID;
    		M_WHILE_ID.id = M_WHILE_CP;
    		M_WHILE_CP.closingParenthesis = M_WHILE_OK;
    		M_WHILE_OK.openingKeys = M_WHILE_OPT_OP_CK;
    		M_WHILE_OPT_OP_CK.closingKeys = M_OPERATIONS_OPT_I_R; M_WHILE_OPT_OP_CK.id = M_OPERATIONS_ASSIGNMENT; 
    		M_WHILE_OPT_OP_CK._if = M_IF_OP; M_WHILE_OPT_OP_CK._while = M_WHILE_OP;
    		
    		M_DENIAL_ID.id = M_DENIAL_SEMICOLON;
    		M_DENIAL_SEMICOLON.semicolon = M_OPERATIONS_OPT_I_R;
    		
    		M_OPERATIONS_OPT_I_R.id = M_OPERATIONS_ASSIGNMENT; M_OPERATIONS_OPT_I_R._if = M_IF_OP; M_OPERATIONS_OPT_I_R._while = M_WHILE_OP;
    		M_OPERATIONS_OPT_I_R.closingKeys = END; M_OPERATIONS_OPT_I_R.closingKeys = M_WHILE_IF_CLOSE_K;
    		
    		M_WHILE_IF_CLOSE_K.id = M_OPERATIONS_ASSIGNMENT; M_WHILE_IF_CLOSE_K._if = IF_OP; M_WHILE_IF_CLOSE_K._while = M_WHILE_OP; 
    		M_WHILE_IF_CLOSE_K.closingKeys = M_OPERATIONS_OPT_I_R;
    		
    		
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
                case "data-type": 			return this.dataType == null? ERROR : this.dataType;
                case "main":      			return this.main == null? ERROR: this.main;
                case "if":       			return this._if == null? ERROR: this._if;
                case "while":     			return this._while == null? ERROR: this._while;
                case "return":    			return this._return == null? ERROR: this._return;
                case "boolean":   			return this._boolean == null? ERROR: this._boolean;
                case "coma":      			return this.coma == null? ERROR: this.coma;
                case "semicolon": 			return this.semicolon == null? ERROR: this.semicolon;
                case "opening-parenthesis": return this.openingParenthesis == null? ERROR: this.openingParenthesis;
                case "closing-parenthesis": return this.closingParenthesis == null? ERROR: this.closingParenthesis;
                case "opening-keys": 		return this.openingKeys == null? ERROR: this.openingKeys;
                case "closing-keys":		return this.closingKeys == null? ERROR: this.closingKeys;
                case "arithmetic":			return this.arithmetic == null? ERROR: this.arithmetic;
                case "logical":				return this.logical == null? ERROR: this.logical;
                case "relational":				return this.relational == null? ERROR: this.relational;
                case "assignation":			return this.assignation == null? ERROR: this.assignation;
                case "integer":				return this.integer == null? ERROR: this.integer;
                case "float":				return this._float == null? ERROR: this._float;
                case "denial":				return this.denial == null? ERROR: this.denial;
                case "identifier":					return this.id == null? ERROR: this.id;
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
			case FUNC_DATA: 				return 1;
			case FUNC:						return 2;
			case FUNC_OPEN_P:				return 3;
			case FUNC_OPT_P_D:				return 4;
			case FUNC_OPEN_DATA:			return 5;
			case FUNC_VAR:					return 6;
			case FUNC_OPT_C_P:				return 7;
			case FUNC_OPEN_K:				return 8;
			case FUNC_CLOSE_K:				return 9;
			case FUNC_OPT_K_D:				return 10;
			
			case DECLARATIONS_ID:			return 11;
			case DECLARATIONS_SEMICOLON:	return 12;
			case DECLARATIONS_OPT_D_O:		return 13;
			
			case OPERATIONS_OPT_I_OP:		return 14;
			case OPERATIONS_ASSIGNMENT:		return 15;
			case OPERATIONS:				return 16;
			case OPERATIONS_OPT_S_A_L:		return 17;
			case OPERATIONS_CP:				return 18;
			case OPERATIONS_OPT_A_L_P:		return 19;
			case OPERATIONS_OPT_S_CP:		return 20;
			case OPERATIONS_FUNC_PC:		return 21;
			case OPERATIONS_OPT_I_R:		return 22;
			
			case OPERATIONS_R:				return 23;
			case OPERATIONS_S:				return 24;
			case OPERATIONS_ID:				return 25;
			
			case OPERATIONS_OPT_I_OP_A:		return 26;
			case OPERATIONS_OPT_A_P:		return 27;
			case OPERATIONS_I_A_CP:			return 28;
			case OPERATIONS_A_CP:			return 29;
			case OPERATIONS_OPT_S_CP_A:		return 30;
			case OPERATIONS_CP_A:			return 31;
			case OPERATIONS_FUNC_PC_A:		return 32;
			
			case OPERATIONS_B:				return 33;
			case OPERATIONS_OPT_I_OP_L:		return 34;
			case OPERATIONS_OPT_L_P:		return 35;
			case OPERATIONS_I_L_CP:			return 36;
			case OPERATIONS_L_CP:			return 37;
			case OPERATIONS_OPT_S_CP_L:		return 38;
			case OPERATIONS_CP_L:			return 39;
			case OPERATIONS_FUNC_PC_L:		return 40;
			
			case IF_OP:						return 41;
			case IF_ID:						return 42;
			case IF_CP:						return 43;
			case IF_OK:						return 44;
			case IF_OPT_OP_CK:				return 45;
			
			case WHILE_OP:					return 46;
			case WHILE_ID:					return 47;
			case WHILE_CP:					return 48;
			case WHILE_OK:					return 49;
			case WHILE_OPT_OP_CK:			return 50;
			case WHILE_IF_CLOSE_K:			return 51;
			
			case DENIAL_ID:					return 52;
			case DENIAL_SEMICOLON:			return 53;
		
			case RETURN_ID:					return 54;
			case RETURN_SEMICOLON:			return 55;
		
			case MAIN_OP:					return 56;
			case MAIN_CP:					return 57;
			case MAIN_OK:					return 58;
			case MAIN_OPT_K_D:				return 59;
		
			case M_DECLARATIONS_ID:			return 60;
			case M_DECLARATIONS_SEMICOLON:	return 61;
			case M_DECLARATIONS_OPT_D_O:	return 62;
			
			case M_OPERATIONS_OPT_I_OP:		return 63;
			case M_OPERATIONS_ASSIGNMENT:	return 64;
			case M_OPERATIONS:				return 65;
			case M_OPERATIONS_OPT_S_A_L:	return 66;
			case M_OPERATIONS_CP:			return 67;
			case M_OPERATIONS_OPT_A_L_P:	return 68;
			case M_OPERATIONS_OPT_S_CP:		return 69;
			case M_OPERATIONS_FUNC_PC:		return 70;
			case M_OPERATIONS_OPT_I_R:		return 71;
			
			case M_OPERATIONS_R:			return 72;
			case M_OPERATIONS_S:			return 73;
			case M_OPERATIONS_ID:			return 74;
			
			case M_OPERATIONS_OPT_I_OP_A:	return 75;
			case M_OPERATIONS_OPT_A_P:		return 76;
			case M_OPERATIONS_I_A_CP:		return 77;
			case M_OPERATIONS_A_CP:			return 78;
			case M_OPERATIONS_FUNC_PC_A:	return 79;
			case M_OPERATIONS_OPT_S_CP_A:	return 80;
			case M_OPERATIONS_CP_A:			return 81;
		
			case M_OPERATIONS_B:			return 82;
			case M_OPERATIONS_OPT_I_OP_L:	return 83;
			case M_OPERATIONS_OPT_L_P:		return 84;
			case M_OPERATIONS_I_L_CP:		return 85;
			case M_OPERATIONS_L_CP:			return 86;
			case M_OPERATIONS_OPT_S_CP_L:	return 87;
			case M_OPERATIONS_CP_L:			return 88;
			case M_OPERATIONS_FUNC_PC_L:	return 89;
			
			case M_IF_OP:					return 90;
			case M_IF_ID:					return 91;
			case M_IF_CP:					return 92;
			case M_IF_OK:					return 93;
			case M_IF_OPT_OP_CK:			return 94;
			
			case M_WHILE_OP:				return 94;
			case M_WHILE_ID:				return 95;
			case M_WHILE_CP:				return 96;
			case M_WHILE_OK:				return 97;
			case M_WHILE_OPT_OP_CK:			return 98;
			case M_WHILE_IF_CLOSE_K:		return 99;
			
			case M_DENIAL_ID:				return 100;
			case M_DENIAL_SEMICOLON:		return 101;
			
			case ERROR:						return 102;
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
			case 1:// FUNC_DATA
				System.out.println("Error type: It was expected a data-type");
				break;
			case 2: // FUNC
				System.out.println("Error type: It was expected a function");
				break;
			case 3: // FUNC_OPEN_P
				System.out.println("Error type: It was expected a '('");
				break;
			case 4: // FUNC_OPT_P_D
				System.out.println("Error type: It was expected a data-type or ')'");
				break;
			case 5: // FUNC_OPEN_DATA
				System.out.println("Error type: It was expected a data-type");
				break;
			case 6: // FUNC_VAR
				System.out.println("Error type: It was expected a variable");
				break;
			case 7: // FUNC_OPT_C_P
				System.out.println("Error type: It was expected a ',' or ')'");
				break;
			case 8: // FUNC_OPEN_K
				System.out.println("Error type: It was expected a '{'");
				break;
			case 9: // FUNC_CLOSE_K
				System.out.println("Error Type: It's the end of the function it should be expected '{'");
				break;
			case 10: // FUNC_OPT_K_D
				System.out.println("Error Type: It's expected a variable, if or while, data-type for a new variable or return");
				break;
			case 11: // DECLARATIONS_ID
				System.out.println("Error type: It was expected an id for a new variable");
				break;
			case 12: // DECLARATIONS_SEMICOLON
				System.out.println("Error type: It was expected a ';' for a new variable");
				break;
			case 13: // DECLARATIONS_OPT_D_O
				System.out.println("Error type: It was expected a variable, if or while, data-type for a new variable or return");
				break;
			case 14: // OPERATIONS_OPT_I_OP
				System.out.println("Error type: It was expected a variable, integer or float");
				break;
			case 15: // OPERATIONS_ASSIGNMENT
				System.out.println("Error type: It was expected a '='");
				break;
			case 16: // OPERATIONS
				System.out.println("Error type: It was expected a '!', '(', variable, function, integer or float, boolean");
				break;
			case 17: // OPERATIONS_OPT_S_A_L
				System.out.println("Error type: It was expected a ';', relational symbol, logical symbol or arithmetic symbol");
				break;
			case 18: // OPERATIONS_CP
				System.out.println("Error type: It was expected a ';', '(', ')', arithmetic symbol or logical symbol");
				break;
			case 19: // OPERATIONS_OPT_A_L_P
				System.out.println("Error type: It was expected a ';', ')', arithmetic symbol or logical symbol");
				break;
			case 20: // OPERATIONS_OPT_S_CP
				System.out.println("Error type: It was expected a ';', ')', arithmetic symbol or logical symbol");
				break;
			case 21: // OPERATIONS_FUNC_PC
				System.out.println("Error type: It was expected a ')'");
				break;
			case 22: // OPERATIONS_OPT_I_R
				System.out.println("Error Type: It was expected a variable, if, while, return");
				break;
			case 23: // OPERATIONS_R
				System.out.println("Error Type: It was expected a variable, float or integer");
				break;
			case 24: // OPERATIONS_S
				System.out.println("Error Type: It was expected a ';'");
				break;
			case 25: // OPERATIONS_ID
				System.out.println("Error type: It was expected a ';', ')', relational symbol, arithmetic symbol or logical symbol");
				break;
			case 26: // OPERATIONS_OPT_I_OP_A
				System.out.println("Error type: It was expected a '(', variable, function, integer or float");
				break;
			case 27: // OPERATIONS_OPT_A_P
				System.out.println("Error type: It was expected a ';', ')', arithmetic symbol");
				break;
			case 28: // OPERATIONS_I_A_CP
				System.out.println("Error type: It was expected a variable, function, integer or float");
				break;
			case 29: // OPERATIONS_A_CP
				System.out.println("Error type: It was expected a ')', ';' or arithmetic symbol");
				break;
			case 30: // OPERATIONS_OPT_S_CP_A
				System.out.println("Error type: It was expected a ')', ';' or arithmetic symbol");
				break;
			case 31: // OPERATIONS_CP_A
				System.out.println("Error type: It was expected a '(', ')', ';' or arithmetic symbol");
				break;
			case 32: // OPERATIONS_FUNC_PC_A
				System.out.println("Error type: It was expected a ')'");
				break;
			case 33: // OPERATIONS_B
				System.out.println("Error type: It was expected a logical symbol or ';'");
				break;
			case 34: // OPERATIONS_OPT_I_OP_L
				System.out.println("Error type: It was expected a '(', variable, boolean");
				break;
			case 35: // OPERATIONS_OPT_L_P
				System.out.println("Error type: It was expected a ';', ')', logical symbol");
				break;
			case 36: // OPERATIONS_I_L_CP
				System.out.println("Error type: It was expected a variable, function, boolean");
				break;
			case 37: // OPERATIONS_L_CP
				System.out.println("Error type: It was expected a ')', ';' or logical symbol");
				break;
			case 38: // OPERATIONS_OPT_S_CP_L
				System.out.println("Error type: It was expected a ')', ';' or logical symbol");
				break;
			case 39: // OPERATIONS_CP_L
				System.out.println("Error type: It was expected a '(', ')', ';' or logical symbol");
				break;
			case 40: // OPERATIONS_FUNC_PC_L
				System.out.println("Error type: It was expected a ')'");
				break;
			case 41: // IF_OP
				System.out.println("Error type: It was expected a '('");
				break;
			case 42: // IF_ID
				System.out.println("Error type: It was expected a variable");
				break;
			case 43: // IF_CP
				System.out.println("Error type: It was expected a ')'");
				break;
			case 44: // IF_OK
				System.out.println("Error type: It was expected a '{'");
				break;
			case 45: // IF_OPT_OP_CK
				System.out.println("Error type: It was expected a '}', variable, if or while");
				break;
			case 46: // WHILE_OP
				System.out.println("Error type: It was expected a '('");
				break;
			case 47: // WHILE_ID
				System.out.println("Error type: It was expected a variable");
				break;
			case 48: // WHILE_CP
				System.out.println("Error type: It was expected a ')'");
				break;
			case 49: // WHILE_OK
				System.out.println("Error type: It was expected a '{'");
				break;
			case 50: // WHILE_OPT_OP_CK
				System.out.println("Error type: It was expected a '}', variable, if, while or return");
				break;
			case 51: // WHILE_IF_CLOSE_K
				System.out.println("Error type: It was expected a '}', variable, if, while or return");
				break;
			case 52: // DENIAL_ID
				System.out.println("Error type: It was expected variable");
				break;
			case 53: // DENIAL_SEMICOLON
				System.out.println("Error type: It was expected ';'");
				break;
			case 54: // RETURN_ID
				System.out.println("Error type: It was expected variable");
				break;
			case 55: // RETURN_SEMICOLON
				System.out.println("Error type: It was expected ';'");
				break;
			case 56: // MAIN_OP
				System.out.println("Error type: It was expected a '('");
				break;
			case 57: // MAIN_CP
				System.out.println("Error type: It was expected a ')'");
				break;
			case 58: // MAIN_OK
				System.out.println("Error type: It was expected a '{'");
				break;
			case 59: // MAIN_OPT_K_D
				System.out.println("Error type: It was expected a '}' or data-type");
				break;
			case 60: // DECLARATIONS_ID
				System.out.println("Error type: It was expected an id for a new variable");
				break;
			case 61: // DECLARATIONS_SEMICOLON
				System.out.println("Error type: It was expected a ';' for a new variable");
				break;
			case 62: // DECLARATIONS_OPT_D_O
				System.out.println("Error type: It was expected a variable, if or while, data-type for a new variable or return");
				break;
			case 63: // OPERATIONS_OPT_I_OP
				System.out.println("Error type: It was expected a variable, integer or float");
				break;
			case 64: // OPERATIONS_ASSIGNMENT
				System.out.println("Error type: It was expected a '='");
				break;
			case 65: // OPERATIONS
				System.out.println("Error type: It was expected a '!', '(', variable, function, integer or float, boolean");
				break;
			case 66: // OPERATIONS_OPT_S_A_L
				System.out.println("Error type: It was expected a ';', relational symbol, logical symbol or arithmetic symbol");
				break;
			case 67: // OPERATIONS_CP
				System.out.println("Error type: It was expected a ';', '(', ')', arithmetic symbol or logical symbol");
				break;
			case 68: // OPERATIONS_OPT_A_L_P
				System.out.println("Error type: It was expected a ';', ')', arithmetic symbol");
				break;
			case 69: // OPERATIONS_OPT_S_CP
				System.out.println("Error type: It was expected a ';', ')', arithmetic symbol");
				break;
			case 70: // OPERATIONS_FUNC_PC
				System.out.println("Error type: It was expected a ')'");
				break;
			case 71: // OPERATIONS_OPT_I_R
				System.out.println("Error Type: It was expected a variable, if, while, return");
				break;
			case 72: // OPERATIONS_R
				System.out.println("Error Type: It was expected a variable, float or integer");
				break;
			case 73: // OPERATIONS_S
				System.out.println("Error Type: It was expected a ';'");
				break;
			case 74: // OPERATIONS_ID
				System.out.println("Error type: It was expected a ';', ')', relational symbol, arithmetic symbol or logical symbol");
				break;
			case 75: // OPERATIONS_OPT_I_OP_A
				System.out.println("Error type: It was expected a '(', variable, function, integer or float");
				break;
			case 76: // OPERATIONS_OPT_A_P
				System.out.println("Error type: It was expected a ';', ')', arithmetic symbol");
				break;
			case 77: // OPERATIONS_I_A_CP
				System.out.println("Error type: It was expected a variable, function, integer or float");
				break;
			case 78: // OPERATIONS_A_CP
				System.out.println("Error type: It was expected a ')', ';' or arithmetic symbol");
				break;
			case 79: // OPERATIONS_OPT_S_CP_A
				System.out.println("Error type: It was expected a ')', ';' or arithmetic symbol");
				break;
			case 80: // OPERATIONS_CP_A
				System.out.println("Error type: It was expected a '(', ')', ';' or arithmetic symbol");
				break;
			case 81: // OPERATIONS_FUNC_PC_A
				System.out.println("Error type: It was expected a ')'");
				break;
			case 82: // OPERATIONS_B
				System.out.println("Error type: It was expected a logical symbol or ';'");
				break;
			case 83: // OPERATIONS_OPT_I_OP_L
				System.out.println("Error type: It was expected a '(', variable, function, integer or float");
				break;
			case 84: // OPERATIONS_OPT_L_P
				System.out.println("Error type: It was expected a ';', ')', logical symbol");
				break;
			case 85: // OPERATIONS_I_L_CP
				System.out.println("Error type: It was expected a variable, function, integer or float");
				break;
			case 86: // OPERATIONS_L_CP
				System.out.println("Error type: It was expected a ')', ';' or logical symbol");
				break;
			case 87: // OPERATIONS_OPT_S_CP_L
				System.out.println("Error type: It was expected a ')', ';' or logical symbol");
				break;
			case 88: // OPERATIONS_CP_L
				System.out.println("Error type: It was expected a '(', ')', ';' or logical symbol");
				break;
			case 89: // OPERATIONS_FUNC_PC_L
				System.out.println("Error type: It was expected a ')'");
				break;
			case 90: // IF_OP
				System.out.println("Error type: It was expected a '('");
				break;
			case 91: // IF_ID
				System.out.println("Error type: It was expected a variable");
				break;
			case 92: // IF_CP
				System.out.println("Error type: It was expected a ')'");
				break;
			case 93: // IF_OK
				System.out.println("Error type: It was expected a '{'");
				break;
			case 94: // IF_OPT_OP_CK
				System.out.println("Error type: It was expected a '}', variable, if or while");
				break;
			case 95: // WHILE_OP
				System.out.println("Error type: It was expected a '('");
				break;
			case 96: // WHILE_ID
				System.out.println("Error type: It was expected a variable");
				break;
			case 97: // WHILE_CP
				System.out.println("Error type: It was expected a ')'");
				break;
			case 98: // WHILE_OK
				System.out.println("Error type: It was expected a '{'");
				break;
			case 99: // WHILE_OPT_OP_CK
				System.out.println("Error type: It was expected a '}', variable, if or while");
				break;
			case 100: // DENIAL_ID
				System.out.println("Error type: It was expected variable");
				break;
			case 101: // DENIAL_SEMICOLON
				System.out.println("Error type: It was expected ';'");
				break;
			default:
				System.out.println("Error Type: They shouldn't be items outside functions");
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
    	SyntacticStates syntacticState = SyntacticStates.FUNC_DATA;
    	int positionState = 0;
    	for (int i = 0; i < tokens.size(); i++) {
    		knowMain++;
    		syntacticState = syntacticState.transition(tokens.get(i).type);
    		if ((syntacticState == SyntacticStates.OPERATIONS_OPT_I_OP || syntacticState == SyntacticStates.M_OPERATIONS_OPT_I_OP
    				|| syntacticState == SyntacticStates.OPERATIONS_OPT_I_OP_A || syntacticState == SyntacticStates.OPERATIONS_OPT_I_OP_L) 
    				&& tokens.get(i).type == "opening-parenthesis") {
    			parenthesis.add(tokens.get(i).id);
    		} else if ((syntacticState == SyntacticStates.OPERATIONS_OPT_S_CP 
    				|| syntacticState == SyntacticStates.M_OPERATIONS_OPT_S_CP
    				|| syntacticState == SyntacticStates.OPERATIONS_OPT_S_CP_A
    				|| syntacticState == SyntacticStates.OPERATIONS_OPT_S_CP_L) && tokens.get(i).type == "closing-parenthesis") {
    			if (!parenthesis.isEmpty()){
					parenthesis.remove((parenthesis.size() - 1));
				} else {
    				printUnbalancedParenthesisError(i);
					balance = false;
        			break;
				}
    		} else if (syntacticState == SyntacticStates.OPERATIONS_OPT_I_R && tokens.get(i).type == "semicolon"
    				|| syntacticState == SyntacticStates.M_OPERATIONS_OPT_I_R && tokens.get(i).type == "semicolon") {
    			if (parenthesis.size() > 0) {
    				printUnbalancedParenthesisError(i);
    				balance = false;
        			break;
                } 
    		} else if (syntacticState == SyntacticStates.FUNC_OPEN_P) {
    			tokens.get(i).type = "function";
    		} else if (syntacticState == SyntacticStates.DECLARATIONS_SEMICOLON || syntacticState == SyntacticStates.FUNC_OPT_C_P ||
    				syntacticState == SyntacticStates.OPERATIONS_ASSIGNMENT ||  syntacticState == SyntacticStates.M_OPERATIONS_ASSIGNMENT ||
    				syntacticState == SyntacticStates.M_DECLARATIONS_SEMICOLON ) {
    			tokens.get(i).type = "variable";
    		} else if ((syntacticState == SyntacticStates.OPERATIONS_FUNC_PC || syntacticState == SyntacticStates.M_OPERATIONS_FUNC_PC
    				|| syntacticState == SyntacticStates.OPERATIONS_FUNC_PC_A || syntacticState == SyntacticStates.M_OPERATIONS_FUNC_PC_A
    				|| syntacticState == SyntacticStates.OPERATIONS_FUNC_PC_L || syntacticState == SyntacticStates.M_OPERATIONS_FUNC_PC_L)
    				&& (tokens.get(i-1).type == "identifier" || tokens.get(i-1).type == "variable") ) {
    			tokens.get(i-1).type = "function";
    		} else if ((syntacticState == SyntacticStates.OPERATIONS_ID || syntacticState == SyntacticStates.OPERATIONS_OPT_A_L_P
    				|| syntacticState == SyntacticStates.OPERATIONS_CP || syntacticState == SyntacticStates.IF_CP 
    				|| syntacticState == SyntacticStates.WHILE_CP || syntacticState == SyntacticStates.DENIAL_SEMICOLON 
    				|| syntacticState == SyntacticStates.RETURN_SEMICOLON || syntacticState == SyntacticStates.M_OPERATIONS_ID 
    				|| syntacticState == SyntacticStates.M_OPERATIONS_OPT_A_L_P || syntacticState == SyntacticStates.M_OPERATIONS_CP 
    				|| syntacticState == SyntacticStates.M_IF_CP || syntacticState == SyntacticStates.M_WHILE_CP 
    				|| syntacticState == SyntacticStates.M_DENIAL_SEMICOLON || syntacticState == SyntacticStates.OPERATIONS_CP_A
    				|| syntacticState == SyntacticStates.OPERATIONS_CP_L || syntacticState == SyntacticStates.M_OPERATIONS_CP_A
    				|| syntacticState == SyntacticStates.M_OPERATIONS_CP_L || syntacticState == SyntacticStates.OPERATIONS_S
    				|| syntacticState == SyntacticStates.M_OPERATIONS_S) && tokens.get(i).type == "identifier") {
    			tokens.get(i).type = "variable";
    		} else if (syntacticState == SyntacticStates.ERROR && !isSyntaxError) {
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
    	if (!hasMain && knowMain == tokens.size()) { printNoMain(); }
    	if (!tokens.isEmpty() && !isSyntaxError && balance && hasMain) { printSyntacticSymbolsTable(); }
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