package exercise2;

import acm.graphics.GObject;
import acm.program.*;
import java.awt.event.MouseEvent;

public class App extends GraphicsProgram {

    private static final double CANVAS_WIDTH = 540;    // Calculator width
    private static final double CANVAS_HEIGHT = 740;   // Calculator height
    private CalculatorLayout calculatorLayout;         // Instantiate the Layout Object

    private char opBuffer;                             // Stores the operator
    private double operand1;                           // Stores the operand digits
    private String result;                             // Stores the results
    private String memoryValue;                        // Variable to store the value in memory

    private boolean isFirstOp;                         // Checks if first operator
    private boolean isPriorEquals;                     // Checks if it is prior to equal sign
    private boolean isFirstPoint;                      // Checks if first decimal point
    private boolean isDeletable;                       // Checks if it is deletable

    private boolean isRadiansMode = false;             // Flag for radians (true) or degrees (false)
    private boolean isHyperbolicMode = false;          // Flag for hyperbolic mode (true) or normal mode (false)

    public void run() {
        setTitle("LBYCPEI Scientific Calculator");
        setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        calculatorLayout = new CalculatorLayout(getHeight());
        add(calculatorLayout);
        initBooleans();
        addMouseListeners();                           // Adds the program as both a MouseListener and MouseMotionListener to the canvas.
    }

    public void mouseClicked(MouseEvent e) {
        GObject element = calculatorLayout.getElementAt(e.getX(), e.getY());

        if (element instanceof MyButton) {
            String input = ((MyButton) element).getText();

            // Handle special cases: Clear Element, Clear All, and Delete
            if (input.equals("CE")) {
                calculatorLayout.clearMainDisplay();
                calculatorLayout.clearMemoElement(opBuffer);
                return;
            }
            if (input.equals("C")) {
                calculatorLayout.clearMainDisplay();
                calculatorLayout.clearMemoDisplay();
                initBooleans();
                return;
            }
            if (input.equals("⌫") && isDeletable) {
                calculatorLayout.deleteOneCharacter();
                return;
            }

            // Handle reciprocal function
            if (input.equals("1/x")) {
                double operand = Double.parseDouble(calculatorLayout.getMainDisplay());
                double result = 1.0 / operand;
                calculatorLayout.setMainDisplay(String.valueOf(result));
                calculatorLayout.clearMemoDisplay();
                initBooleans();
                return;
            }

            // Handle square root function
            if (input.equals("√")) {
                double operand = Double.parseDouble(calculatorLayout.getMainDisplay());
                double sqrtResult = Math.sqrt(operand);
                calculatorLayout.setMainDisplay(String.valueOf(sqrtResult));
                calculatorLayout.clearMemoDisplay();
                return;
            }

            // Handle exponentiation function
            if (input.equals("^")) {
                operand1 = Double.parseDouble(calculatorLayout.getMainDisplay());
                calculatorLayout.setMainDisplay("");
                calculatorLayout.clearMemoDisplay();
                calculatorLayout.setMemoDisplay(operand1 + " ^ ");

                opBuffer = '^';
                isFirstOp = false;
                isFirstPoint = true;
                return;
            }

            // Handle trigonometric and logarithmic functions
            if (input.equals("sin") || input.equals("cos") || input.equals("tan") || input.equals("sinh") || input.equals("cosh") || input.equals("tanh") || input.equals("log") || input.equals("ln")) {
                double operand = Double.parseDouble(calculatorLayout.getMainDisplay());

                String degreeSymbol = isRadiansMode ? "" : "°";
                String functionDisplay = input + "(" + calculatorLayout.getMainDisplay() + degreeSymbol + ")";
                calculatorLayout.setMainDisplay("");

                if (!isOperator(opBuffer) && !isFirstOp && !isPriorEquals) {
                    calculatorLayout.setMemoDisplay(calculatorLayout.getMemoDisplay() + " " + functionDisplay);
                } else {
                    calculatorLayout.clearMemoDisplay();
                    calculatorLayout.setMemoDisplay(functionDisplay);
                }

                double result = switch (input) {
                    case "sin" -> isRadiansMode ? Math.sin(operand) : Math.sin(Math.toRadians(operand));
                    case "cos" -> isRadiansMode ? Math.cos(operand) : Math.cos(Math.toRadians(operand));
                    case "tan" -> isRadiansMode ? Math.tan(operand) : Math.tan(Math.toRadians(operand));
                    case "sinh" -> isRadiansMode ? Math.sinh(operand) : Math.sinh(Math.toRadians(operand));
                    case "cosh" -> isRadiansMode ? Math.cosh(operand) : Math.cosh(Math.toRadians(operand));
                    case "tanh" -> isRadiansMode ? Math.tanh(operand) : Math.tanh(Math.toRadians(operand));
                    case "log" -> Math.log10(operand);
                    case "ln" -> Math.log(operand);
                    default -> 0.0;
                };

                calculatorLayout.setMainDisplay(String.valueOf(result));
                return;
            }

            // II. Handle arithmetic symbols and operations
            char symbol = input.charAt(0);

            if (symbol == '(' || symbol == ')') {
                if (calculatorLayout.getMainDisplay().equals("0")) {
                    calculatorLayout.setMainDisplay(String.valueOf(symbol));
                } else {
                    calculatorLayout.appendMainDisplay(String.valueOf(symbol));
                }
                return;
            }

            if (symbol == '±' && isDeletable) {
                calculatorLayout.negateElement(opBuffer);
                System.out.println("Negation Called");
                return;
            }

            if ((symbol >= '0' && symbol <= '9') || symbol == '.' || symbol == 'e' || symbol == 'π') {
                isDeletable = true;

                if (symbol == '.') {
                    if (!isFirstPoint) {
                        return;
                    } else {
                        isFirstPoint = false;
                    }
                }

                if (isPriorEquals) {
                    calculatorLayout.clearMainDisplay();
                    isPriorEquals = false;
                    System.out.println("Digit: Prior Equals");
                }

                if (symbol == 'π') {
                    double operand = Math.PI;
                    if (calculatorLayout.getMainDisplay().equals("0")) {
                        calculatorLayout.setMainDisplay(String.valueOf(operand));
                    } else {
                        calculatorLayout.appendMainDisplay(String.valueOf(operand));
                    }
                    return;
                }

                if (symbol == 'e') {
                    double operand = Math.E;
                    if (calculatorLayout.getMainDisplay().equals("0")) {
                        calculatorLayout.setMainDisplay(String.valueOf(operand));
                    } else {
                        calculatorLayout.appendMainDisplay(String.valueOf(operand));
                    }
                    return;
                }

                if (calculatorLayout.getMainDisplay().equals("0") && symbol != '.') {
                    calculatorLayout.setMainDisplay(String.valueOf(symbol));
                } else {
                    calculatorLayout.appendMainDisplay(String.valueOf(symbol));
                }

                calculatorLayout.setMemoDisplay(symbol);
                System.out.println("Digit: Prior Not Equals");
                return;
            }

            // Handle factorial symbol
            if (symbol == '!') {
                String currentDisplay = calculatorLayout.getMainDisplay();
                try {
                    int number = Integer.parseInt(currentDisplay);
                    if (number < 0) {
                        System.out.println("Error: Factorial of a negative number is not defined");
                        return;
                    }
                    long factorial = calculateFactorial(number);
                    calculatorLayout.setMainDisplay(String.valueOf(factorial));
                    calculatorLayout.setMemoDisplay("!");
                    System.out.println("Factorial Calculated: " + factorial);
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Invalid input for factorial");
                }
                return;
            }

            if (isOperator(symbol)) {
                if (isFirstOp && !isPriorEquals) {
                    operand1 = Double.parseDouble(calculatorLayout.getMainDisplay());
                    calculatorLayout.setMemoDisplay(String.valueOf(symbol));
                    opBuffer = symbol;
                    isFirstOp = false;
                    System.out.println("Operator: First Operation and Not prior equals");
                } else if (isPriorEquals) {
                    calculatorLayout.setMemoDisplay(result + symbol);
                    opBuffer = symbol;
                    isFirstOp = false;
                    System.out.println("Operator: Prior equals!");
                } else {
                    double operand2 = Double.parseDouble(calculatorLayout.getMainDisplay());
                    operand1 = MathHandler.evaluate(operand1, operand2, opBuffer);
                    result = String.valueOf(operand1);
                    result = result.contains(".") ? result.replaceAll("0*$", "").replaceAll("\\.$", "") : result;
                    calculatorLayout.setMainDisplay(result);
                    calculatorLayout.setMemoDisplay(String.valueOf(symbol));
                    isDeletable = false;
                    System.out.println("Operator: Second operator");
                }

                calculatorLayout.clearNumBuffer();
                isFirstPoint = true;
            }

            if (symbol == '=') {
                if (opBuffer == '^') {
                    double exponent = Double.parseDouble(calculatorLayout.getMainDisplay());
                    double result = Math.pow(operand1, exponent);
                    calculatorLayout.setMainDisplay(String.valueOf(result));
                    initBooleans();
                    return;
                }
                double operand2 = Double.parseDouble(calculatorLayout.getMainDisplay());
                operand1 = MathHandler.evaluate(operand1, operand2, opBuffer);
                result = String.valueOf(operand1);
                result = result.contains(".") ? result.replaceAll("0*$", "").replaceAll("\\.$", "") : result;
                calculatorLayout.setMainDisplay(result);
                calculatorLayout.clearMemoDisplay();
                initBooleans();
                System.out.println("Equals: evaluated");
                System.out.println("operand1 = " + operand1);
                System.out.println("operand2 = " + operand2);
            }

            if (input.equals("DEG") || input.equals("RAD")) {
                isRadiansMode = !isRadiansMode;
                ((MyButton) element).setText(isRadiansMode ? "RAD" : "DEG");
                return;
            }

            if (input.equals("hyp") || input.equals("HYP")) {
                isHyperbolicMode = !isHyperbolicMode;
                ((MyButton) element).setText(isHyperbolicMode ? "HYP" : "hyp");
                updateTrigFunctionLabels();
                return;
            }
        }
    }


    private void initBooleans() {
        isFirstOp = true;
        isPriorEquals = true;
        isDeletable = false;
        isFirstPoint = true;
    }

    private boolean isOperator(char symbol) {
        return (symbol == '+' || symbol == '-' || symbol == 'x' || symbol == '÷');
    }

    private void updateTrigFunctionLabels() {
        for (GObject obj : calculatorLayout) {
            if (obj instanceof MyButton) {
                MyButton button = (MyButton) obj;
                String label = button.getText();
                switch (label) {
                    case "sin", "sinh" -> button.setText(isHyperbolicMode ? "sinh" : "sin");
                    case "cos", "cosh" -> button.setText(isHyperbolicMode ? "cosh" : "cos");
                    case "tan", "tanh" -> button.setText(isHyperbolicMode ? "tanh" : "tan");
                }
            }
        }
    }

    private double evaluateExpressionInsideParentheses(String expression) {
        if (expression.startsWith("(") && expression.endsWith(")")) {
            expression = expression.substring(1, expression.length() - 1);
        }

        String[] parts = expression.split("\\+");
        double sum = 0.0;
        for (String part : parts) {
            sum += Double.parseDouble(part);
        }
        return sum;
    }

    private String formatResult(double result) {
        String formattedResult = String.valueOf(result);
        return formattedResult.contains(".") ? formattedResult.replaceAll("0*$", "").replaceAll("\\.$", "") : formattedResult;
    }

    private long calculateFactorial(int number) {
        long result = 1;
        for (int i = 1; i <= number; i++) {
            result *= i;
        }
        return result;
    }

    public static void main(String[] args) {
        (new App()).start(args);
    }
}
