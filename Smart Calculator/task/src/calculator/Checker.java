package calculator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Checker {

    private String inputString;
    private String errorMessage;
    private Map<String, String> variable;
    private String regexCommand = "/.*";
    private String regexJustNumber = "-?\\d+";
    private String regexJustLetter = "[a-zA-Z]+";
    private String regexNotCorrectInput = ".*[\\+-]$|.*[^\\w\\+-=\\*/\\^\\(\\)].*|.*[\\*/]{2,}.*";
    private String regexNotCorrectIdentifier = ".*\\d.*=.*";
    private String regexNotCorrectAssignment = ".*=([a-zA-Z]+\\d+|\\d+\\D+).*|.*=.*=.*";
    private String regexAssignmentVariable = "([a-zA-Z]+)=(-?\\d+|-?[a-zA-Z]+)";
    private String regexUnknownVariable = "([a-zA-Z]+)([\\+-]|$)";
    private String regexAddSubtract = "([\\+-]?)(\\w+)([\\+-])(\\w+).*?";
    private String regexNotParentheses = "[^\\(\\)]";

    Checker(Map<String, String> variable) {
        this.variable = variable;
    }

    String getRegexAddSubtract() {
        return regexAddSubtract;
    }

    boolean isCommand() {
        return inputString.matches(regexCommand);
    }

    boolean isJustNumber() {
        return inputString.matches(regexJustNumber);
    }

    boolean isJustLetter() {
        return inputString.matches(regexJustLetter);
    }

    boolean isAssignmentVariable() {
        return inputString.matches(regexAssignmentVariable);
    }

    private boolean isNotCorrectInput() {
        return inputString.matches(regexNotCorrectInput);
    }

    private boolean isNotCorrectIdentifier() {
        return inputString.matches(regexNotCorrectIdentifier);
    }

    private boolean isNotCorrectAssignment() {
        return inputString.matches(regexNotCorrectAssignment);
    }

    private boolean isNotCorrectParentheses() {
        Deque<Character> braceDeque = new ArrayDeque<>();
        String parentheses = inputString.replaceAll(regexNotParentheses, "");
        boolean isBracePairs = true;

        for (char p : parentheses.toCharArray()) {
            if (p == '(') {
                braceDeque.push(p);
            } else {
                if (braceDeque.size() == 0) {
                    isBracePairs = false;
                    break;
                }
                braceDeque.poll();
            }
        }
        if (braceDeque.size() != 0) {
            isBracePairs = false;
        }
        return !isBracePairs;
    }

    private boolean isUnknownVariable() {
        Pattern pattern = Pattern.compile(regexUnknownVariable);
        Matcher matcher = pattern.matcher(inputString);
        while (matcher.find()) {
            if (!variable.containsKey(matcher.group(1))) {
                return true;
            }
        }
        return false;
    }

    void setInputString(String inputString) {
        this.inputString = inputString;
    }

    String getErrorMessage() {
        return errorMessage;
    }

    boolean isContainsError() {
        boolean error = false;
        if (isNotCorrectInput()) {
            errorMessage = "Invalid expression";
            error = true;
        } else if (isNotCorrectIdentifier()) {
            errorMessage = "Invalid identifier";
            error = true;
        } else if (isNotCorrectAssignment()) {
            errorMessage = "Invalid assignment";
            error = true;
        } else if (isUnknownVariable()) {
            errorMessage = "Unknown variable";
            error = true;
        } else if (isNotCorrectParentheses()) {
            errorMessage = "Invalid expression";
            error = true;
        }
        return error;
    }
}

