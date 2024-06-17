/*
 * File: MathHandler.java
 * ---------------------
 *  This class is a helper class for a sample calculator app implementation
 *  Author: Cobalt mkc
 *  Date created: July 22, 2019
 *  Last modified: Aug 3, 2022
 */

package exercise2;

import java.util.*;

public class MathHandler {

    public static int evaluate(int operand1, int operand2, char operator) {
        // No modifications needed here!!!
        int result;
        switch (operator) {
            case '+':
                result = add(operand1, operand2);
                break;
            case '-':
                result = subtract(operand1, operand2);
                break;
            case 'x':
                result = multiply(operand1, operand2);
                break;
            case '÷':
                result = divide(operand1, operand2);
                break;
            default:
                result = 0;
        }
        return result;
    }

    public static double evaluate(double operand1, double operand2, char operator) {
        // No modifications needed here!!!
        double result;
        switch (operator) {
            case '+':
                result = add(operand1, operand2);
                break;
            case '-':
                result = subtract(operand1, operand2);
                break;
            case 'x':
                result = multiply(operand1, operand2);
                break;
            case '÷':
                result = divide(operand1, operand2);
                break;
            default:
                result = 0;
        }
        return result;
    }

    public static int add(int operand1, int operand2) {
        return operand1 + operand2;
    }

    public static double add(double operand1, double operand2) {
        return operand1 + operand2;
    }

    public static int subtract(int operand1, int operand2) {
        return operand1 - operand2;
    }

    public static double subtract(double operand1, double operand2) {
        return operand1 - operand2;
    }

    public static int divide(int operand1, int operand2) {
        if (operand2 == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return operand1 / operand2;
    }

    public static double divide(double operand1, double operand2) {
        if (operand2 == 0.0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return operand1 / operand2;
    }

    public static int multiply(int operand1, int operand2) {
        return operand1 * operand2;
    }

    public static double multiply(double operand1, double operand2) {
        return operand1 * operand2;
    }

    public static double evaluate(String expression) {
        List<String> rpn = infixToRPN(expression);
        return evaluateRPN(rpn);
    }

    private static List<String> infixToRPN(String expression) {
        List<String> output = new ArrayList<>();
        Stack<Character> operators = new Stack<>();
        int i = 0;
        while (i < expression.length()) {
            char token = expression.charAt(i);
            if (Character.isDigit(token) || token == '.') {
                StringBuilder number = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i++));
                }
                output.add(number.toString());
            } else if (token == '(') {
                operators.push(token);
                i++;
            } else if (token == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.add(String.valueOf(operators.pop()));
                }
                operators.pop();  // pop '('
                i++;
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    output.add(String.valueOf(operators.pop()));
                }
                operators.push(token);
                i++;
            } else {
                i++;
            }
        }
        while (!operators.isEmpty()) {
            output.add(String.valueOf(operators.pop()));
        }
        return output;
    }

    private static boolean isOperator(char token) {
        return token == '+' || token == '-' || token == 'x' || token == '÷';
    }

    private static int precedence(char operator) {
        switch (operator) {
            case 'x':
            case '÷':
                return 2;
            case '+':
            case '-':
                return 1;
            default:
                return 0;
        }
    }

    private static double evaluateRPN(List<String> rpn) {
        Stack<Double> stack = new Stack<>();
        for (String token : rpn) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                double result = evaluate(operand1, operand2, token.charAt(0));
                stack.push(result);
            }
        }
        return stack.pop();
    }

    private static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
