import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Calculator {


    private final String operationsExp = "[\\+-\\/\\*\\(\\)]+";
    private final String numExp = "[0-9]+(\\.[0-9]+)?";
    private final String multiplicationGroup = numExp + "\\*" + numExp;
    private final String divisionGroup = numExp + "\\/" + numExp;
    private final String additionGroup = numExp + "\\+" + numExp;
    private final String subtractionGroup = numExp + "-" + numExp;
    private final String perenthesisGroup = "\\((" + numExp + "[\\*\\/\\+-]" + numExp +")+\\)";


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
        new Calculator(true);
    }

    private double solveEquation(String equation) {
        // Remove all whitespace from the equation
        equation = equation.replaceAll("\\s+", "");
        logDebug("Whitespace cleaned: " + equation);

//        logDebug(perenthesisGroup);
//        logDebug(multiplicationGroup);
//        logDebug(divisionGroup);
//        logDebug(additionGroup);
//        logDebug(subtractionGroup);


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

            // Get the simplified version of this parentheses
            String simplified = Double.toString(simplifyEquationR(subEquation));
            equation = substituteValue(equation, simplified, start, end);
            parenthesisMatcher = perenthesisPattern.matcher(equation);
        }

        // Parse multiplication
        Pattern multPattern = Pattern.compile(multiplicationGroup);
        Matcher multMatcher = multPattern.matcher(equation);

        // Iterate through all matches
        while (multMatcher.find()) {
            int start = multMatcher.start();
            int end = multMatcher.end();

            String subEquation = multMatcher.group();
            logDebug(subEquation);

            // Perform the multiplication
            double[] ns = extractNumbers(subEquation);
            double n1 = ns[0];
            double n2 = ns[1];

            String result = Double.toString(n1 * n2);
            equation = substituteValue(equation, result, start, end);
            multMatcher = multPattern.matcher(equation);
        }

        // Parse division
        Pattern divPattern = Pattern.compile(divisionGroup);
        Matcher divMatcher = divPattern.matcher(equation);

        // Iterate through all matches
        while (divMatcher.find()) {
            int start = divMatcher.start();
            int end = divMatcher.end();

            String subEquation = divMatcher.group();
            logDebug(subEquation);

            // Perform the multiplication
            double[] ns = extractNumbers(subEquation);
            double n1 = ns[0];
            double n2 = ns[1];

            String result = Double.toString(n1 / n2);
            equation = substituteValue(equation, result, start, end);
            divMatcher = divPattern.matcher(equation);
        }

        // Parse addition
        Pattern addPattern = Pattern.compile(additionGroup);
        Matcher addMatcher = addPattern.matcher(equation);

        // Iterate through all matches
        while (addMatcher.find()) {
            int start = addMatcher.start();
            int end = addMatcher.end();

            String subEquation = addMatcher.group();
            logDebug(subEquation);

            // Perform the multiplication
            double[] ns = extractNumbers(subEquation);
            double n1 = ns[0];
            double n2 = ns[1];

            String result = Double.toString(n1 + n2);
            equation = substituteValue(equation, result, start, end);
            addMatcher = addPattern.matcher(equation);
        }

        // Parse subtraction
        Pattern subPattern = Pattern.compile(subtractionGroup);
        Matcher subMatcher = subPattern.matcher(equation);

        // Iterate through all matches
        while (subMatcher.find()) {
            int start = subMatcher.start();
            int end = subMatcher.end();

            String subEquation = subMatcher.group();
            logDebug(subEquation);

            // Perform the multiplication
            double[] ns = extractNumbers(subEquation);
            double n1 = ns[0];
            double n2 = ns[1];

            String result = Double.toString(n1 + n2);
            equation = substituteValue(equation, result, start, end);
            subMatcher = subPattern.matcher(equation);
        }

        return Double.parseDouble(equation);
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