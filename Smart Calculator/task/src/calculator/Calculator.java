package calculator;

import java.math.BigInteger;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;

class Calculator {

    void run() {

        Scanner scanner = new Scanner(System.in);

        Matcher matcher = null;
        Map<String, String> variable = new HashMap<>();
        Checker inputChecker = new Checker(variable);
        Utility utility = new Utility(variable);
        Deque<String> stackPostfixExpression;

        while (true) {

            String mathExpression = scanner.nextLine();
            mathExpression = utility.clearExpression(mathExpression);
            inputChecker.setInputString(mathExpression);

            if (mathExpression.isEmpty()) {
                continue;
            }

            if (inputChecker.isCommand()) {
                utility.performCommand(mathExpression);
                continue;
            }

            if (inputChecker.isContainsError()) {
                System.out.println(inputChecker.getErrorMessage());
                continue;
            }

            if (inputChecker.isJustNumber()) {
                System.out.println(Integer.parseInt(mathExpression));
                continue;
            }

            if (inputChecker.isJustLetter()) {
                System.out.println(variable.getOrDefault(mathExpression, "Unknown variable"));
                continue;
            }

            if (inputChecker.isAssignmentVariable()) {
                utility.assignmentVariable(mathExpression);
                continue;
            }

            // convert from infixExpression to postfixExpression
            // change variable to digits
            // and execute expression
            stackPostfixExpression = utility.convertFromInfixToPostfix(mathExpression);
            BigInteger result = utility.solveMathExpression(stackPostfixExpression);
            System.out.println(result);
        }
    }
}
