public class Main {

	/**
     * Main program 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
    	LexiconAnalysis lexicon = new LexiconAnalysis();
		String str = "";
    	str = lexicon.readFile("Archivo_Fuente.txt");
    	/** Make lexicon analysis */
    	lexicon.lexiconAnalysis(str);
        /**
         * Print errors if it there are 
         * Also prints the type of error if it recognizes it
         * Prints line and initial character of the error
         */
        if (!LexiconAnalysis.errors.isEmpty()) { lexicon.printErrorTable(); } 
        /**
         * Shows the tokens table, symbol and
         * Go the syntactic analysis
         */
        else {
        	if (!LexiconAnalysis.ids.isEmpty()) { lexicon.printTokenTable(); }
        	SyntacticAnalysis syntactic = new SyntacticAnalysis();
        	SyntacticAnalysis.tokens = LexiconAnalysis.tokens;
        	if (!syntactic.syntacticAnalysis()) {
        		// PARSE
				System.out.print("-------------------- Starting Parsing -----------------------------\n\n");
				KotlinParser parser = new KotlinParser(SyntacticAnalysis.tokens);
				parser.ParseToKotlin();
				
			}
			else {
				// TODO: DO NOT PARSE
			}
        	
        }   	
        
    }	
}
