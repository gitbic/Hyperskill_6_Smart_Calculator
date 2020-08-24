package calculator;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Utility {
    private Map<String, String> variable;

    Utility(Map<String, String> variable) {
        this.variable = variable;
    }

    BigInteger solveMathExpression(Deque<String> stackPostfixExpression) {
        Deque<BigInteger> stackResult = new ArrayDeque<>();

        while (stackPostfixExpression.size() != 0) {
            String element = stackPostfixExpression.poll();

            if (element.matches("-?\\w+")) {
                if (element.matches("-?[a-zA-Z+]")) {
                    element = variable.get(element);
                }
                stackResult.push(new BigInteger(element));
            } else {

                BigInteger b = stackResult.poll();
                BigInteger a = stackResult.poll();
                BigInteger c = BigInteger.ZERO;

                switch (element) {
                    case "+":
                        c = a.add(b);
                        break;
                    case "-":
                        c = a.subtract(b);
                        break;
                    case "*":
                        c = a.multiply(b);
                        break;
                    case "/":
                        c = a.divide(b);
                        break;
                    case "^":
                        int d = Integer.parseInt(String.valueOf(b));
                        c = a.pow(d);
                        break;
                    default:
                        System.out.println("Invalid operator: " + element);
                }
                stackResult.push(c);
            }
        }
        return stackResult.poll();
    }

    Deque<String> convertFromInfixToPostfix(String infixExpression) {
        Deque<String> stackPostfixExpression = new ArrayDeque<>();
        Deque<String> stackSigns = new ArrayDeque<>();

        String regexElement = "(^-)?\\w+|[\\+\\*-/^()]";
        Pattern p = Pattern.compile(regexElement);
        Matcher m = p.matcher(infixExpression);

        while (m.find()) {
            String element = m.group();

            if (element.matches("\\w+|^-\\w+")) {
                stackPostfixExpression.offer(element);

            } else if (stackSigns.isEmpty() || stackSigns.peek().equals("(") || element.equals("(")) {
                stackSigns.push(element);

            } else if (element.equals(")")) {
                while (!stackSigns.peek().equals("(")) {
                    stackPostfixExpression.offer(stackSigns.poll());
                }
                stackSigns.poll();

            } else {
                int stackOperatorPriority = getOperatorPriority(stackSigns.peek());
                int inputOperatorPriority = getOperatorPriority(element);

                while (stackOperatorPriority >= inputOperatorPriority && !stackSigns.peek().equals("(")) {
                    stackPostfixExpression.offer(stackSigns.poll());
                    if (stackSigns.isEmpty()) break;

                    stackOperatorPriority = getOperatorPriority(stackSigns.peek());
                    inputOperatorPriority = getOperatorPriority(element);
                }
                stackSigns.push(element);
            }
        }

        while (stackSigns.size() != 0) {
            stackPostfixExpression.offer(stackSigns.poll());
        }
        return stackPostfixExpression;
    }

    private int getOperatorPriority(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }

    void assignmentVariable(String mathExpression) {
        String regexAssignmentVariable = "([a-zA-Z]+)=(-?\\d+|-?[a-zA-Z]+)";

        Pattern patternAssignmentVariable = Pattern.compile(regexAssignmentVariable);
        Matcher matcher = patternAssignmentVariable.matcher(mathExpression);
        if (matcher.matches()) {
            String varName = matcher.group(1);
            String varValue = matcher.group(2);

            if (varValue.matches("-?\\d+")) {
                variable.put(varName, varValue);
            } else {
                variable.put(varName, variable.get(varValue));
            }
        }
    }

    String clearExpression(String expression) {
        return expression.
                replaceAll("\\s", "").
                replaceAll("--", "+").
                replaceAll("\\++", "+").
                replaceAll("\\+-|-\\+", "-");
    }

    void performCommand(String command) {
        switch (command) {
            case "/help":
                System.out.println("Supported operations:\n" +
                        "- addition +\n" +
                        "- subtraction -\n" +
                        "- multiplication \\*\n" +
                        "- integer division /\n" +
                        "- parentheses ()\n" +
                        "- power operator ^\n" +
                        "- variables");
                break;
            case "/exit":
                System.out.println("Bye!");
                System.exit(0);
                break;
            default:
                System.out.println("Unknown command");
        }
    }
}
