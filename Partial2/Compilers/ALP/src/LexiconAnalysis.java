import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class LexiconAnalysis {
	
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
        LexiconStates period;
        LexiconStates symbol;
        LexiconStates equal;
        LexiconStates quotes;
        
    	static {
            // Initial state of the automata
            Q0.space = Q0; Q0.letter = Q1; Q0.capital = Q1; Q0.number = Q2; Q0.symbol = Q5; Q0.equal = Q6; Q0.quotes = Q8; Q0.period = Q0;
            
            // Word
            Q1.space = Q0; Q1.letter = Q1; Q1.capital = Q1; Q1.number = Q1; Q1.symbol = Q5; Q1.period = Q0; Q1.quotes = Q8; Q1.equal = Q0;
            
            // Number or Float
            Q2.space = Q0; Q2.number = Q2; Q2.period = Q3;  Q2.symbol = Q5;
            Q3.number = Q4; Q3.space = Q0;
            Q4.space = Q0; Q4.number = Q4; Q4.symbol = Q5;
            
            // Symbol
            Q5.space = Q0; Q5.letter = Q1; Q5.symbol = Q5; Q5.number = Q2; Q5.period = Q0; Q5.quotes = Q8;
            
            // Equal
            Q6.space = Q0; Q6.equal = Q7;
            Q7.space = Q0; Q7.equal = Q0; Q7.quotes = Q1;
            
            // Quotes
            Q8.space = Q8; Q8.letter = Q8; Q8.capital = Q8; Q8.number = Q8; Q8.symbol = Q8; Q8.period = Q8; Q8.quotes = Q9;
            Q9.space = Q0; Q9.period = Q0; Q9.symbol = Q0; Q9.letter = Q0;
            
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
        	} else if (ch == '\"') {
        		type = "quotes";
        	}
            switch (type) {
                case "letter":    return this.letter == null? ERROR : this.letter;
                case "number":    return this.number == null? ERROR: this.number;
                case "capLetter": return this.capital == null? ERROR: this.capital;
                case "space":     return this.space == null? ERROR: this.space;
                case "period":    return this.period == null? ERROR: this.period;
                case "symbol":    return this.symbol == null? ERROR: this.symbol;
                case "equal":     return this.equal == null? ERROR: this.equal;
                case "quotes":	  return this.quotes == null? ERROR: this.quotes;
                    
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
    			ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '<' || ch == '>' || ch == '[' || ch == ']' || ch == ':') {
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
    protected String readFile(String filename) {
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
    	if (position >= characters.size()) {
    		return characters.get(characters.size()-1).line;
    	} else {
    		return characters.get(position).line;
    	}
	}
    
    private static boolean isIdentifier(String id) {
    	boolean isIdentifier = false;
    	if (isLetter(id.charAt(0)) || isCapLetter(id.charAt(0))) {
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
    
    private static boolean isString(String string) {
    	boolean isString = false;
    	if (string.charAt(0) == '"' && string.charAt(string.length()-1) == '"') {
    		isString = true;
    	} else {
    		isString = false;
    	}
    	return isString;
    }
    
    private static boolean isPronoun(String string) {
    	boolean isPronoun = false;
    	if (string.equals("he") || string.equals("He") || string.equals("She") || string.equals("she") 
    			|| string.equals("it") || string.equals("It")) {
    		isPronoun = true;
    	}
    	return isPronoun;
    }
    
    private static boolean isPossessive(String string) {
    	boolean isPossessive = false;
    	if (string.equals("his") || string.equals("her") || string.equals("its")) {
    		isPossessive = true;
    	}
    	return isPossessive;
    }
    
    private static boolean isA(String string) {
    	boolean isA = false;
    	if (string.equals("a") || string.equals("A")) {
    		isA = true;
    	}
    	return isA;
    }
    
    private static String checkId(String string) {
    	switch (string) {
	        case "is": return "is";
	        case "Class": return "class";
	        case "has": return "has";
	        case "can": return "can";
	        case "To": return "to";
	        case "used": return "used";
	        case "needs": return "needs";
	        case "in": return "in";
	        case "increases": return "sum";
	        case "decreases": return "res";
	        case "print": return "print";
	        case "by": return "by";
	        case "and": return "and";
	        case "return": return "return";
	        case "true": return "boolean";
	        case "false": return "boolean";
	        case ",": return "coma";
	        case ";": return "semicolon";
	        case "(": return "opening-parenthesis";
	        case ")": return "closing-parenthesis";
	        case "{": return "opening-keys";
	        case "}": return "closing-keys";
	        case "[": return "opening-bracket";
	        case "]": return "closing-bracket";
	        case "+": return "add";
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
	        case ".": return "end";
	        case "\"": return "quotes";
	        case ":": return "colon";
	            
	        default:
	        	if (isNumber(string)) {
	        		return "integer";
	        	} else if (isString(string)) {
	        		return "string";
	        	} else if (isPronoun(string)) {
	        		return "pronoun";
	        	} else if (isPossessive(string)) {
	        		return "possesive";
	        	} else if (isA(string)) {
	        		return "init";
	        	} else if (isIdentifier(string)) {
	        		return "identifier";
	        	} else {
	        		return "float";
	        	} 
    	}
    }
    
    @SuppressWarnings("unlikely-arg-type")
	protected void lexiconAnalysis(String str) {
    	LexiconStates lexiconState = LexiconStates.Q0;
    	Integer position = 0;
    	boolean isError = false;
        for (int i = 0; i < str.length(); i++) {
            lexiconState = lexiconState.transition(str.charAt(i));
            System.out.print(lexiconState.toString() + " ");
            System.out.println(str.charAt(i));
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
            		if (item.size() == 2 || !item.contains("=")) {
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
            else if (lexiconState == LexiconStates.Q3) {
            	if (str.charAt(i) == '.' && str.length() == i + 1) {
            		toSyntax();
            		String id = String.valueOf(str.charAt(i));
            		ids.add(id);
            	} else if (str.charAt(i) == '.' && !isNumber(str.charAt(i + 1))) {
            		toSyntax();
            		String id = String.valueOf(str.charAt(i));
            		ids.add(id);
            	} else {
            		item.add(str.charAt(i));
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
    protected void printErrorTable() {
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
	protected void printTokenTable() {
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
}
