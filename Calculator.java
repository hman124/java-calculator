import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Calculator {
    private final String numExp = "[0-9]+(\\.[0-9]+)?";
    private final String operationExp = "[\\*\\/\\+-\\^]?";
    private final String multiplicationGroup = numExp + "\\*" + numExp;
    private final String divisionGroup = numExp + "\\/" + numExp;
    private final String additionGroup = numExp + "\\+" + numExp;
    private final String subtractionGroup = numExp + "-" + numExp;
    private final String exponentGroup = numExp + "\\^" + numExp;
    private final String perenthesisGroup = "\\((" + numExp + operationExp + ")+" + numExp +"\\)";

    boolean isLoggingDebug = false;

    private Calculator(boolean log) {
        isLoggingDebug = log;

        // Define the calculations
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a new equation: ");

        String equation = input.nextLine();
        input.close();

        double result = solveEquation(equation);

        System.out.println("RESULT: " + result);
    }

    public static void main(String[] args){
        boolean log = args.length > 0 && Objects.equals(args[0], "logDebug");
        new Calculator(log);
    }

    private double solveEquation(String equation) {
        // Remove all whitespace from the equation
        equation = equation.replaceAll("\\s+", "");
        logDebug("Whitespace cleaned: " + equation);

        return simplifyEquationR(equation);
    }

    private double simplifyEquationR(String equation){
        // PEMDAS

        // Parse parenthesis
        Pattern perenthesisPattern = Pattern.compile(perenthesisGroup);
        Matcher parenthesisMatcher = perenthesisPattern.matcher(equation);

        // Iterate through all matches
        while (parenthesisMatcher.find()) {
            int start = parenthesisMatcher.start();
            int end = parenthesisMatcher.end();

            // Remove the parenthesis
            String subEquation = parenthesisMatcher.group();
            subEquation = subEquation.substring(1, subEquation.length() - 1);

            // Get the simplified version of these parentheses (run recursive)
            String simplified = Double.toString(simplifyEquationR(subEquation));
            equation = substituteValue(equation, simplified, start, end);

            // Find all new matches
            parenthesisMatcher = perenthesisPattern.matcher(equation);
        }

        // Parse exponents
        equation = operationParser(equation, exponentGroup, Math::pow);

        // Parse multiplication
        equation = operationParser(equation, multiplicationGroup, (n1, n2) -> n1 * n2);

        // Parse division
        equation = operationParser(equation, divisionGroup, (n1, n2) -> n1 / n2);

        // Parse addition
        equation = operationParser(equation, additionGroup, Double::sum);

        // Parse subtraction
        equation = operationParser(equation, subtractionGroup, (n1, n2) -> n1 - n2);

        return Double.parseDouble(equation);
    }

    interface OperationFunction {
        double calculate(double n1, double n2);
    }

    private String operationParser(String equation, String operationGroup, OperationFunction cb) {
                // Parse subtraction
        Pattern operationPattern = Pattern.compile(operationGroup);
        Matcher operationMatcher = operationPattern.matcher(equation);

        // Iterate through all matches
        while (operationMatcher.find()) {
            int start = operationMatcher.start();
            int end = operationMatcher.end();

            String subEquation = operationMatcher.group();
            logDebug(subEquation);

            // Perform the multiplication
            double[] numbers = extractNumbers(subEquation);
            double number1 = numbers[0];
            double number2 = numbers[1];

            double calculatedResult = cb.calculate(number1, number2);
            String stringResult = Double.toString(calculatedResult);

            equation = substituteValue(equation, stringResult, start, end);
            operationMatcher = operationPattern.matcher(equation);
        }

        return equation;
    }

    private double[] extractNumbers(String subEquation) {
        Pattern numberPattern = Pattern.compile(numExp);
        // Perform the multiplication
        Matcher numberMatcher = numberPattern.matcher(subEquation);

        if (!numberMatcher.find()) {return new double[]{};}
        double firstNumber = Double.parseDouble(numberMatcher.group());

        if (!numberMatcher.find()) {return new double[]{};}
        double secondNumber = Double.parseDouble(numberMatcher.group());

        return new double[]{firstNumber, secondNumber};
    }

    private String substituteValue(String equation, String subEquation, int start, int end){
        return equation.substring(0, start) + subEquation + equation.substring(end);
    }

    private void logDebug(String val){
        if(!isLoggingDebug) {return;}
        System.out.println("DEBUG: " + val);
    }
}