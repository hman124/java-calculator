import java.util.Scanner;
import java.util.regex.Pattern; 
  
class Calculator {

    
    boolean isLoggingDebug = false;

    private Calculator(boolean log) {
        isLoggingDebug = log;


        System.out.println(isLoggingDebug);
        
        // Define the calculations
        
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a new equation: ");
        
        String equation = input.nextLine();
        input.close();
        
        double result = parseEquation(equation);
    }
    
    public static void main(String[] args){
        new Calculator(true);
    }

    private double parseEquation(String equation) {
        // Parse the equation
        String operationsExp = "[\\+-\\/\\*\\(\\)]+";
        String numExp = "[0-9]+";

        String multiplicationGroup = numExp + "\\*" + numExp;
        String divisionGroup = numExp + "\\/" + numExp;
        String additionGroup = numExp + "\\+" + numExp;
        String subtractionGroup = numExp + "-" + numExp;
        String perenthesisGroup = "\\((" + numExp + "[\\*\\/\\+-]" + numExp +")+\\)";

        System.out.println(perenthesisGroup);
        
        // Remove all whitespace from the equation
        equation = equation.replaceAll("\\s+", "");

        logDebug("Whitespace cleaned: " + equation);

        // PEMDAS


        // Walk through the equation and calculate the input
        // for (int i = 0; i < equation.length(); i++) {
        //     String item = Character.toString(equation.charAt(i));
        //     if(item.matches(operationsExp)){
        //         // Update the working value and clear the queue
        //         workingValue = Double.parseDouble(queuedValue);
        //         queuedValue = "";

        //         operationMode = true;
        //         operationSymbol = item;
        //     } else if (item.matches(numbersExp)){
        //         queuedValue += item;
        //     } else {
        //         System.out.println("ERROR: found invalid character in equation: " + item);
        //         break;
        //     }

        // }

        return 0.0;
    }

    private void logDebug(String val){
        System.out.println(isLoggingDebug);
        if(!isLoggingDebug) {return;}
        System.out.println("DEBUG: " + val);
    }
}