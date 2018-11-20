import java.util.List;
import java.util.ArrayList;

// Luis Eduardo Vargas Victoria
// A01630086
// Diseño de compiladores

public class InfixToPostfix {

    private static void infixToPostfix(String infix, char[] objects) {
    	
        System.out.println(infix);
        String postfix = "";
        List<Character> operators = new ArrayList<Character>();

        for (int i = 0; i < objects.length; i++) {

            System.out.println("Object: " + objects[i]);
        	if (objects[i] == '1' || 
        			objects[i] == '2' || 
        				objects[i] == '3' || 
        					objects[i] == '4' || 
        						objects[i] == '5' || 
        							objects[i] == '6' || 
        								objects[i] == '7' || 
        									objects[i] == '8' || 
        										objects[i] == '9' || 
        											objects[i] == '0') {
        		postfix += objects[i] + " ";
            } else if (objects[i] == '+' || 
							objects[i] == '-' || 
								objects[i] == '*' || 
									objects[i] == '/' || 
										objects[i] == '^') {
            	if (operators.isEmpty()){
					operators.add(objects[i]);
				} else {
					// Mientras el operador que llega sea menor o igual a la prioridad saca de la pila el operador de hasta arriba  
					// Hace esto hasta que la prioridad no se cumpla y la pila no este vacía
					// Me tarde 2 horas sacando esto
					while(!operators.isEmpty() && priority(objects[i], operators.get(operators.size() - 1))) {
            			postfix += operators.remove((operators.size()- 1)) + " ";
            		
            		}
            		operators.add(objects[i]);
				}
            }
			else {
				System.out.println("No acceptable");
			}
            System.out.println(postfix);
            System.out.println("Stack: " + operators);
        }
        // Me tarde 1 hora sacando esto
        while (!operators.isEmpty())
            postfix += operators.remove((operators.size() - 1));
        
        System.out.println(postfix);
    }
    
    private static boolean priority(char op1, char op2){
    	  if ((op2 == '+' || op2 == '-') && (op1 == '+' || op1 == '-'))
    		  return true;
    	  else if ((op2 == '*' || op2 == '/') && (op1 == '+' || op1 == '-' || op1 == '*' || op1 == '/'))
    		  return true;
    	  else if ((op2 == '^') && (op1 == '+' || op1 == '-' || op1 == '*' || op1 == '/'))
    		  return true;
    	  else if ((op2 == '^') && (op1 == '^' || op1 == '+' || op1 == '-' || op1 == '*' || op1 == '/'))
    		  return true;
    	  else
    		  return false;
    	 }
    
    public static void main(String args[]) {

    	String infix = "6 + 2 * 4 / 2 ^ 2 - 4";
		String[] splitInfix = infix.split("\\s+");
		String infixConcatenated = "";
	    for (String n:splitInfix) {
	    	infixConcatenated+= n;
	    }
	    char[] objects = infixConcatenated.toCharArray();
        infixToPostfix(infix, objects);
    }
    
}
