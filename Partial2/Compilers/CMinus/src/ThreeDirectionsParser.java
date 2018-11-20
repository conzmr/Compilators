import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

class ThreeDirectionsParser {
    public List<Token> tokens = new ArrayList<Token>();
    private Token currentToken = null;
    private int currentIndex = 0;
    private StringBuilder stringBuilder = new StringBuilder();
    private int currentTermCounter = 1;
    private int currentLabelCounter = 1;


    public void doThreeDirectionsParser() {
        System.out.println("\n\n----------------------------\nInitiating Three Directions Parser\n----------------------------");

        for (Token token : tokens) {
            System.out.println("Token ID: " + token.id + "\nType: " + token.type + "\n");
        }

        currentToken = tokens.get(currentIndex);

        while (true) {
            if (currentToken == null) {
                break;
            }

            if (currentToken.type.equals("function")) {
                // goto function declaration
                functionDeclaration();

                //updateCurrentToken(2);
                updateCurrentToken(1);
                continue;
            }
            else if (currentToken.type.equals("main")) {
                mainDeclaration();

                break;
            }
            updateCurrentToken(1);
        }

        System.out.println("\n\nThree Directions Parse Result\n---------------------------------------------\n\n" + stringBuilder.toString());
    }

    private String getNewTerm() {
        String result = "T" + currentTermCounter;
        currentTermCounter++;

        return result;
    }

    private String getNewLabel() {
        String result = "L" + currentLabelCounter;
        currentLabelCounter++;

        return result;
    }

    private void updateCurrentToken(int jumpStep) {
        currentIndex += jumpStep;
        if (currentIndex >= tokens.size())
            currentToken = null;
        else
            currentToken = tokens.get(currentIndex);
    }

    private Token peekNextToken(int nextStep) {
        return tokens.get(currentIndex+nextStep);
    }

    private void mainDeclaration() {
        stringBuilder.append("entry main\n");

        updateCurrentToken(2);

        do {
            if (!currentToken.type.equals("closing-parenthesis")) {
                updateCurrentToken(1);
            }
            else {
                break;
            }
        } while (true);

        updateCurrentToken(2);


        if (currentToken.type.equals("int")) {
            declarationsBlock();
        }

        if (currentToken.type.equals("identifier")) {
            assignationsBlock();
        }

        if (currentToken.type.equals("if")) {
            // if statement, check it in new function
            ifStatement();
        }

        stringBuilder.append("return");
    }

    private void functionDeclaration() {
        stringBuilder.append("entry " + currentToken.id + "\n");

        // jumping opening parenthesis
        updateCurrentToken(2);

        do {
            if (currentToken.type != "closing-parenthesis") {
                updateCurrentToken(1);
            }
            else {
                break;
            }
        } while (true);

        // jumping opening brackets
        updateCurrentToken(2);

        if (currentToken.type.equals("int")) {
            declarationsBlock();
        }

        if (currentToken.type.equals("identifier")) {
            assignationsBlock();
        }

        if (currentToken.type.equals("while")) {
            whileStatement();
        }

        if (currentToken.type.equals("if")) {
            ifStatement();
        }

        if (currentToken.type.equals("return"))
            returnStatement();
    }

    private void declarationsBlock() {
        while (true) {
            updateCurrentToken(3);
            if (!currentToken.type.equals("int"))
                return;
        }
    }

    private void assignationsBlock() {
        do {
            if (peekNextToken(2).id.equals("read")) {
                stringBuilder.append("read ").append(currentToken.id).append("\n");

                updateCurrentToken(6);

                if (!currentToken.type.equals("identifier")) {
                    break;
                }
            }

            if (currentToken.id.equals("write")) {

                updateCurrentToken(2);
                String lastTerm = expression();

                stringBuilder.append("write ").append(lastTerm).append("\n");

                if (!currentToken.type.equals("identifier")) {
                    break;
                }
            }

            if (peekNextToken(1).type.equals("assignation")) {
                String idLeft = currentToken.id;

                updateCurrentToken(2);

                String lastTerm = multipleExpression();

                //String lastTerm = expression();

                stringBuilder.append(idLeft).append(" = ").append(lastTerm).append("\n");

                updateCurrentToken(1);

                if (!currentToken.type.equals("identifier")) {
                    break;
                }
            }

        } while (true);
    }

    private void whileStatement() {
        // jumping "while ("
        updateCurrentToken(2);

        String loopLabel = getNewLabel();
        String operation = "";

        do {
            if (!currentToken.type.equals("closing-parenthesis")) {
                operation += currentToken.id + " ";
                updateCurrentToken(1);
            }
            else {
                break;
            }
        } while (true);

        if (!operation.isEmpty() && operation.length() >= 3) {
            String term = getNewTerm();
            String label = getNewLabel();
            operation = term + " = " + operation;

            stringBuilder.append("Label ").append(loopLabel).append("\n");

            stringBuilder.append(operation).append("\n");

            stringBuilder.append("if false ").append(term).append(" goto ").append(label).append("\n");


            if (peekNextToken(1).type.equals("opening-keys"))
                updateCurrentToken(2);
            else
                updateCurrentToken(1);


            if (currentToken.type.equals("int"))
                declarationsBlock();

            if (currentToken.type.equals("identifier") || currentToken.type.equals("function"))
                assignationsBlock();

            if (currentToken.type.equals("while")) {
                whileStatement();
            }

            if (currentToken.type.equals("if")) {
                ifStatement();
            }

            if (currentToken.type.equals("return"))
                returnStatement();

            stringBuilder.append("goto ").append(loopLabel).append("\n");

            stringBuilder.append("Label ").append(label).append("\n");

            if (currentToken.type.equals("closing-keys"))
                updateCurrentToken(1);
        }
    }

    private void ifStatement() {
        // jumping "if ("
        updateCurrentToken(2);

        String operation = "";

        do {
            if (!currentToken.type.equals("closing-parenthesis")) {
                operation += currentToken.id + " ";
                updateCurrentToken(1);
            }
            else {
                break;
            }
        } while (true);

        if (!operation.isEmpty() && operation.length() >= 3) {
            String term = getNewTerm();
            String label = getNewLabel();
            operation = term + " = " + operation;

            stringBuilder.append(operation).append("\n");

            stringBuilder.append("if false ").append(term).append(" goto ").append(label).append("\n");


            if (peekNextToken(1).type.equals("opening-keys"))
                updateCurrentToken(2);
            else
                updateCurrentToken(1);


            if (currentToken.type.equals("int"))
                declarationsBlock();

            if (currentToken.type.equals("identifier") || currentToken.type.equals("function")) {
                assignationsBlock();
            }

            if (currentToken.type.equals("while")) {
                whileStatement();
            }

            if (currentToken.type.equals("if")) {
                ifStatement();
            }

            if (currentToken.type.equals("return"))
                returnStatement();

            stringBuilder.append("Label ").append(label).append("\n");


            if (currentToken.type.equals("closing-keys"))
                updateCurrentToken(1);


            if (currentToken.id.equals("else")) {
                boolean wasClosingBrackets = false;

                if (peekNextToken(1).type.equals("opening-keys")) {
                    wasClosingBrackets = true;
                    updateCurrentToken(2);
                }
                else
                    updateCurrentToken(1);

                if (currentToken.type.equals("int"))
                    declarationsBlock();

                if (currentToken.type.equals("identifier") || currentToken.type.equals("function"))
                    assignationsBlock();

                if (currentToken.type.equals("return"))
                    returnStatement();


                if (wasClosingBrackets && currentToken.type.equals("closing-keys"))
                    updateCurrentToken(1);
            }
        }
    }

    private void returnStatement() {
        if (peekNextToken(1).type.equals("semicolon")) {
            stringBuilder.append("return").append("\n");
            updateCurrentToken(2);
            return;
        }
        else if (peekNextToken(2).type.equals("semicolon")) {
            updateCurrentToken(1);
            if (currentToken.type == "function") {
                String functionTerm = functionCall();
                stringBuilder.append("return ").append(functionTerm).append("\n");
                updateCurrentToken(1);
                return;
            }
            else {
                stringBuilder.append("return ").append(currentToken.id).append("\n");
                updateCurrentToken(2);
                return;
            }
        }

        updateCurrentToken(1);
        String lastTerm = expression();

        stringBuilder.append("return ").append(lastTerm).append("\n");
    }

    private String expression() {
        String leftTerm = "";
        String rightTerm = "";

        if (!peekNextToken(1).type.equals("add") && !peekNextToken(1).type.equals("mul") && !peekNextToken(1).type.equals("relational")) {
            if (currentToken.type.equals("function") || peekNextToken(1).type.equals("opening-parenthesis")) {
                String lastTerm = functionCall();

                if (currentToken.type.equals("add") || currentToken.type.equals("mul") || currentToken.type.equals("relational")) {
                    String operator = currentToken.id;

                    updateCurrentToken(1);

                    if (currentToken.type.equals("function") || peekNextToken(1).type.equals("opening-parenthesis")) {
                        rightTerm = functionCall();
                        //updateCurrentToken(1);
                    }
                    else {
                        rightTerm = currentToken.id;
                        //updateCurrentToken(1);
                    }

                    String resultTerm = getNewTerm();

                    stringBuilder.append(resultTerm).append(" = ").append(lastTerm).append(" ").append(operator).append(" ").append(rightTerm).append("\n");

                    return resultTerm;
                }

                return lastTerm;
            }


            String id = currentToken.id;
            updateCurrentToken(1);
            return id;
        }

        String operator = peekNextToken(1).id;

        if (currentToken.type.equals("function") || peekNextToken(1).type.equals("opening-parenthesis")) {
            leftTerm = functionCall();
            updateCurrentToken(1);
        }
        else {
            leftTerm = currentToken.id;
            updateCurrentToken(2);
        }

        if (currentToken.type.equals("function") || peekNextToken(1).type.equals("opening-parenthesis")) {
            rightTerm = functionCall();
            updateCurrentToken(1);
        }
        else {
            rightTerm = currentToken.id;
            updateCurrentToken(1);
        }

        String resultTerm = getNewTerm();

        stringBuilder.append(resultTerm).append(" = ").append(leftTerm).append(" ").append(operator).append(" ").append(rightTerm).append("\n");

        return resultTerm;
    }

    private String functionCall() {
        stringBuilder.append("begin_args").append("\n");

        String functionName = currentToken.id;
        // jumping "fun ("
        updateCurrentToken(2);

        if (currentToken.type.equals("closing-parenthesis")) {
            String term = getNewTerm();

            stringBuilder.append(term).append(" call ").append(functionName).append(", 0").append("\n");

            return term;
        }

        int argumentsListCount = 0;
        do {
            if (currentToken.type.equals("closing-parenthesis")) {
                break;
            }

            argumentsListCount++;
            String termFromExpression = expression();

            stringBuilder.append("param ").append(termFromExpression).append("\n");

            if (currentToken.type.equals("closing-parenthesis")) {
                break;
            }

            updateCurrentToken(1);

        } while (true);

        String resultTerm = getNewTerm();

        stringBuilder.append(resultTerm).append(" = call ").append(functionName).append(", ").append(argumentsListCount).append("\n");

        updateCurrentToken(1);
        return resultTerm;
    }

    private String multipleExpression() {
        ArrayList<String> expressionArray = new ArrayList<>();
        Stack<String> parenthesisStack = new Stack<>();

        while (!currentToken.type.equals("semicolon")) {
            String term = "";
            boolean isFunctionCall = false;

            if (currentToken.type.equals("function") || (currentToken.type.equals("identifier") && peekNextToken(1).type.equals("opening-parenthesis")))
                isFunctionCall = true;

            while (true) {
                if (currentToken.type.equals("add") && !isFunctionCall)
                    break;

                if (currentToken.type.equals("mul") && !isFunctionCall)
                    break;

                if (currentToken.type.equals("semicolon") && !isFunctionCall)
                    break;

                if (currentToken.type.equals("opening-parenthesis") && !isFunctionCall)
                    break;

                if (currentToken.type.equals("opening-parenthesis") && isFunctionCall) {
                    parenthesisStack.push("(");
                }

                if (currentToken.type.equals("closing-parenthesis") && !isFunctionCall)
                    break;

                term += currentToken.id;

                if (currentToken.type.equals("closing-parenthesis") && isFunctionCall) {
                   parenthesisStack.pop();

                    if (parenthesisStack.isEmpty()) {
                        updateCurrentToken(1);
                        break;
                    }
                }

                updateCurrentToken(1);
            }

            if (!term.isEmpty())
                expressionArray.add(term);

            if (currentToken.type.equals("semicolon")) {
                break;
            }

            expressionArray.add(currentToken.id);

            updateCurrentToken(1);
            parenthesisStack = new Stack<>();
        }


        String lastResult = multipleExp(expressionArray);


        return lastResult;
    }

    private String multipleExp(ArrayList<String> expressionArray) {
        int openParenthesisIndex = -1;
        int closeParenthesisIndex = -1;

        for (String term : expressionArray) {
            System.out.println("Term: " + term);
        }

        System.out.println();

        for (int i = 0; i < expressionArray.size(); i++) {
            String currentTerm = expressionArray.get(i);

            if (currentTerm.contains("(") && currentTerm.contains(")")) {
                String result = expressionForGivenFunction(currentTerm);

                expressionArray.remove(i);
                expressionArray.add(i, result);

                i = -1;
            }
        }

        for (int i = 0; i < expressionArray.size(); i++) {
            String currentTerm = expressionArray.get(i);

            if (currentTerm.equals("(")) {
                openParenthesisIndex = i;
            }
            else if (currentTerm.equals(")")) {
                closeParenthesisIndex = i;

                ArrayList<String> subExpressionArr = new ArrayList<>(expressionArray.subList(openParenthesisIndex+1, closeParenthesisIndex));

                String resultTerm = multipleExp(subExpressionArr);

                for (int j = closeParenthesisIndex; j >= openParenthesisIndex; j--) {
                    expressionArray.remove(j);
                }

                expressionArray.add(openParenthesisIndex, resultTerm);

                i = -1;
            }
        }

        for (int i = 0; i < expressionArray.size(); i++) {
            String currentTerm = expressionArray.get(i);

            if (currentTerm.equals("*") || currentTerm.equals("/")) {
                String leftTerm = expressionArray.get(i-1);
                String rightTerm = expressionArray.get(i+1);

                String resultTerm = expressionForGivenTerms(leftTerm, currentTerm, rightTerm);

                expressionArray.remove(i+1);
                expressionArray.remove(i);
                expressionArray.remove(i-1);


                expressionArray.add(i-1, resultTerm);

                i = -1;
            }
        }

        for (int i = 0; i < expressionArray.size(); i++) {
            String currentTerm = expressionArray.get(i);

            if (currentTerm.equals("+") || currentTerm.equals("-")) {
                String leftTerm = expressionArray.get(i-1);
                String rightTerm = expressionArray.get(i+1);

                String resultTerm = expressionForGivenTerms(leftTerm, currentTerm, rightTerm);

                expressionArray.remove(i+1);
                expressionArray.remove(i);
                expressionArray.remove(i-1);

                expressionArray.add(i-1, resultTerm);
                
                i = -1;
            }
        }

        return expressionArray.get(0);
    }

    private String expressionForGivenTerms(String leftTerm, String operator, String rightTerm) {
        String resultTerm = getNewTerm();

        stringBuilder.append(resultTerm).append(" = ").append(leftTerm).append(" ").append(operator).append(" ").append(rightTerm).append("\n");
        return resultTerm;
    }

    private String expressionForGivenFunction(String function) {
        StringBuilder args = new StringBuilder();

        String functionName = function.split("\\(")[0];

        Stack<String> parenthesisStack = new Stack<>();

        boolean parenthesisFlag = false;
        for (char currentChar : function.toCharArray()) {
            if (currentChar == ')') {
                parenthesisStack.pop();

                if (parenthesisStack.isEmpty())
                    break;
            }
            if (currentChar == '(') {
                parenthesisStack.push("(");
                parenthesisFlag = true;

                if (parenthesisStack.size() > 1)
                    args.append(currentChar);
            }
            else if (parenthesisFlag) {
                args.append(currentChar);
            }
        }

        System.out.println("arguments string: " + args);

        String[] argsArray = args.toString().split(",");

        stringBuilder.append("begin_args").append("\n");

        for (String arg : argsArray) {
            //String[] argAsArray = arg.split("");
            ArrayList<String> argAsList = splitStringIntoTermsArr(arg);

            String resultTerm = multipleExp(argAsList);

            String newTerm = getNewTerm();

            stringBuilder.append(newTerm).append(" = ").append(resultTerm).append("\n");
            stringBuilder.append("param ").append(newTerm).append("\n");
        }

        String resultTerm = getNewTerm();
        stringBuilder.append(resultTerm).append(" = call ").append(functionName).append(", ").append(argsArray.length).append("\n");

        return resultTerm;
    }

    private ArrayList<String> splitStringIntoTermsArr(String termStr) {
        System.out.println("term to split: " + termStr);
        ArrayList<String> termsArr = new ArrayList<>();

        String term = "";
        boolean isFunctionCall = false;

        for (int i = 0; i < termStr.length(); i++) {
            char currentChar = termStr.charAt(i);

            if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/' || currentChar == '(' || currentChar == ')') {

                if (!term.isEmpty()) {
                    termsArr.add(term);
                }
                termsArr.add(currentChar + "");

                term = "";
                isFunctionCall = false;
                continue;
            }


            term += currentChar;
        }

        if (!term.isEmpty())
            termsArr.add(term);

        return termsArr;
    }
}
