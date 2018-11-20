
import java.io.*;
import java.util.*;

public class InfixPosfix {

    public static boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?");
    }


    public static boolean checkPriority(String x, String y, String operation) {
        int xInt = getPriority(x);
        int yInt = getPriority(y);

		if(operation.equals(">")){
		    return xInt > yInt;
		}
		else if(operation.equals("<")){
		    return xInt < yInt;
		}
		else if(operation.equals("==")){
		    return xInt == yInt;
		}
		else if(operation.equals(">=")){
		    return xInt >= yInt;
		}else{
		    return false;
		}
	}

	public static int getPriority(String x){
	    if(x.equals("+") || x.equals("-") ){
	        return 0;
	    }
	    else if(x.equals("/") || x.equals("*") ){
	        return 1;
	    }else{
	        return 2;
	    }
	}


	public static void printList(String label, ArrayList<String> list){
	    String string = "";
	    for(int i = 0; i < list.size(); i++){
	        string += list.get(i);
	    }
	    System.out.println(label +" " + string);
	}





    public static void main(String args[]) {
        String infixString = "2 + 3 * 4 + 6";
        String[] infixArray = infixString.split("\\s+");
        Stack<String> stack = new Stack<String>();
        ArrayList<String> infix = new ArrayList<String>();
        ArrayList<String> posfix = new ArrayList<String>();

        for(int i = 0; i<infixArray.length; i++) {
            infix.add(infixArray[i]);
        }

        while(!infix.isEmpty() || (infix.isEmpty() && !stack.empty())){
            if(!infix.isEmpty()){
                String current = infix.remove(0);
                if(isNumeric(current)){
                    posfix.add(current);
                }else{
                    if(stack.empty()){
                        stack.push(current);
                    }
                    else if(checkPriority(current, stack.peek(), "==")){
                        posfix.add(stack.pop());
                        stack.push(current);
                    }
                    else if(checkPriority(current, stack.peek(), ">")){
                        stack.push(current);
                    }else{
                        while(!stack.empty() && (checkPriority(stack.peek(), current, ">="))){
                            posfix.add(stack.pop());
                        }
                        stack.push(current);
                    }
                }
            }else{
                while(!stack.empty()){
                    posfix.add(stack.pop());
                }
            }
            printList("Infix:",infix);
            printList("Posfix:",posfix);

        }



    }
}
