/*
note:
    Console input method might not support in some machine.
*/

package texasholdem;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Author: Yiyao Zhang
 * Version: 4.1f
 */

enum Domain {   // input domain
    hasMaximum, hasMinimum, unlimited, limited
}

enum ReaderType {
    jOptionPane, scanner, console, bufferedReader
}

class InputHandleSystem {   // main function

    private class ReturnIfValid<A, B> {
        final A ISVALID;
        final B VALUE;

        ReturnIfValid(A bool, B d) {
            ISVALID = bool;
            VALUE = d;
        }
    }

    private class InputDomain { // store input domain and out-of-range message
        Domain domain;
        double max, min;
        String outOfRangeMessage;

        InputDomain(Domain inputDomain, double inputMin, double inputMax, String errorMessage) {
            domain = inputDomain;
            min = inputMin;
            max = inputMax;
            outOfRangeMessage = errorMessage;
        }

        void set(double inputMin, double inputMax) {    // set domain (have both max and min)
            domain = Domain.limited;
            min = inputMin;
            max = inputMax;
        }

        void set(Domain inputDomain, double limiter) {  // set domain (has max or min)
            switch (inputDomain) {
                case hasMaximum:
                    domain = inputDomain;
                    max = limiter;
                    break;
                case hasMinimum:
                    domain = inputDomain;
                    min = limiter;
                    break;
            }
        }
    }

    private class InputTriesLimiter {   // limited the tries
        int userApproach, userMaxTries;
        String errorMessage;

        InputTriesLimiter(int maxTries, String inputTooManyTimesMessage) {
            userApproach = 0;
            userMaxTries = maxTries;
            errorMessage = inputTooManyTimesMessage;
        }
    }

    private BuildInReader buildInReader;
    private ReaderType readerType;
    private InputDomain inputDomain;
    private InputTriesLimiter inputTriesLimiter;
    private String inputTypeErrorMessage;
    private char[] allowedChars;

    public InputHandleSystem() {    // init
        inputTriesLimiter = new InputTriesLimiter(3, "Input tries exceed!");
        inputDomain = new InputDomain(Domain.unlimited, 0, 0, "Your input must in range.");
        readerType = ReaderType.bufferedReader;
        inputTypeErrorMessage = "Your input must be a real number(int or double).";
    }

    void closeReader() throws Exception {   // close the reader
        buildInReader.close();
    }

    void setUserMaxTries(int maxTries) {    // set maximum number of user can try, default 3.
        inputTriesLimiter.userMaxTries = maxTries;
    }

    void setInputReader(ReaderType setReaderTypeTo) {   // set prefer reader
        readerType = setReaderTypeTo;
    }

    void setErrorMessage(String wrongInputTypeErrorMessage, String outOfRangeErrorMessage, String inputTriesExceededErrorMessage) { // set all three type error message
        inputTypeErrorMessage = wrongInputTypeErrorMessage;
        inputDomain.outOfRangeMessage = outOfRangeErrorMessage;
        inputTriesLimiter.errorMessage = inputTriesExceededErrorMessage;
    }

    void setInputDomain(double minimum, double maximum) {   // set input domain (have min and max) with default error message
        inputDomain.set(minimum, maximum);
    }

    void setInputDomain(Domain domain, double limiter) {    // set input domain (has only min or max) with default error message
        inputDomain.set(domain, limiter);
    }

    void setInputDomain(double minimum, double maximum, String errorMessage) {  // set input domain (have min and max) and error message
        setInputDomain(minimum, maximum);
        inputDomain.outOfRangeMessage = errorMessage;
    }

    void setInputDomain(Domain domain, double limiter, String errorMessage) {   // set input domain (has only min or max) and error message
        setInputDomain(domain, limiter);
        inputDomain.outOfRangeMessage = errorMessage;
    }

    double[] getDoubleArray(String message) throws Exception {  // get double array from input string, with a message to tell the user what to input.
        return getDoubleArrayWithSeparator(" ", message);
    }

    double[] getDoubleArrayWithSeparator(String regEx,String message) throws Exception {
        reset();                                        // reset current tries
        ReturnIfValid<Boolean, Double> goodInput;      // set return variable for checkInputIsValid method. method returns a boolean "isValid" and double "value".
        buildInReader = new BuildInReader(readerType);  // set reader with prefer reader type
        double[] fixedInputArr;                         // set output double array
        boolean isValidInput = true;                    // set exit condition
        do {
            int arrayIndex = 0;                                                     // set array index variable
            System.out.print(message);                                              // out print input message
            String[] myArr;
            if (regEx.equals(" ")) {
                myArr = buildInReader.getLine(message).split("\\s+");   // separate each string in the incoming string and store as a string array
            } else {
                myArr = buildInReader.getLine(message).split(regEx);
            }

            fixedInputArr = new double[myArr.length];                               // create output array
            for (String myStr : myArr) {                                            // for each string in array, check if good double
                goodInput = checkInputIsValid(myStr);                                  // check if is a double
                if (inputIsInvalid(goodInput)) {              // if is not a good double or not in range
                    isValidInput = false;                                                   // exit condition == false
                    continue;
                }
                isValidInput = true;                                                    // exit condition == true
                fixedInputArr[arrayIndex++] = goodInput.VALUE;                          // assign good value to output array
            }
        } while (!isValidInput);
        return fixedInputArr;                           // return output array
    }

    double getDouble(String message) throws Exception { // get double from input string, with a message to tell the user what to input.
        reset();                                        // reset user current attempt
        ReturnIfValid<Boolean, Double> goodInput;      // set return variable of checkInputIsValid method, which returns a boolean "isValid" and a double "value".
        buildInReader = new BuildInReader(readerType);  // set reader with prefer reader type
        do {                                            // loop start
            System.out.print(message);                                      // out print input message
            goodInput = checkInputIsValid(buildInReader.getLine(message)); // check incoming string is a good double, "goodInput" variable include a boolean "isValid" and a double "value".
        } while (inputIsInvalid(goodInput));      // continue if is not a good double or not in range
        return goodInput.VALUE;                         // return good value.
    }

    double getDouble(String message, char ignoreChar) throws Exception {    // get double from input string with a character needs to ignore, with a message to tell the user what to input.
        reset();
        ReturnIfValid<Boolean, Double> goodInput;
        buildInReader = new BuildInReader(readerType);
        do {
            System.out.print(message);
            goodInput = checkInputIsValid(removeChar(buildInReader.getLine(message), ignoreChar)); // call removeChar method to remove given unneeded char. (such as %)
        } while (inputIsInvalid(goodInput));
        return goodInput.VALUE;
    }

    private String removeChar(String s, char c) {   // remove a char from string
        StringBuilder stringBuilder = new StringBuilder();
        char[] charArray = s.toCharArray();
        for (char eachChar : charArray) {
            if (!(eachChar == c)) {
                stringBuilder.append(eachChar);
            }
        }
        return stringBuilder.toString();
    }

    double getDouble(String message, Domain domain, double... values) throws Exception {    // set input domain and get double from input string, with a message to tell the user what to input.
        if (values.length == 1) {
            inputDomain.set(domain, values[0]);
        } else {
            if (values[1] > values[0])
                inputDomain.set(values[0], values[1]);
            else if (values[1] < values[0])
                inputDomain.set(values[1], values[0]);
        }
        return getDouble(message);
    }

    String getLine(String message) throws Exception {   // get string from input, with a message to tell the user what to input.
        buildInReader = new BuildInReader(readerType);
        System.out.print(message);
        return buildInReader.getLine(message);
    }

    String[] getLineWithSeparator(String regEx, String message) throws Exception {
        String str = getLine(message);
        return str.split(regEx);
    }

    int getInt(String message) throws Exception {   // get int, with a message to tell the user what to input.
        return (int) getDouble(message);
    }

    int getInt(String message, Domain inputDomain, double... domain) throws Exception { // set domain then get int
        return (int) getDouble(message, inputDomain, domain);
    }

    int getInt(String message, char ignoreChar) throws Exception {  // get int with a unnecessary char
        return (int) getDouble(message, ignoreChar);
    }

    char getChar(String message) throws Exception { // get a character, with a message to tell the user what to input.
        reset();                                        // reset current user attempt
        char myChar;                                    // declare output character
        String input;                                   // declare variable that store input string
        buildInReader = new BuildInReader(readerType);  // set reader with prefer reader type
        boolean isValidInput;                           // declare exit condition

        do {
            System.out.print(message);                                      // print message
            input = buildInReader.getLine(message);                         // get input and store to "input" variable
            try {
                myChar = input.toCharArray()[0];                            // get first character from input string and store to "myChar" variable.
            } catch (ArrayIndexOutOfBoundsException e) {
                isValidInput = errorHandler(inputDomain.outOfRangeMessage); // if fail to get char (e.g. space), error
                continue;
            }
            if (allowedChars != null) {                                     // if given accepted character range
                for (char c : allowedChars) {                                   // compare if this character is allowed
                    if (c == myChar) {
                        return myChar;                                          // if finds allowed char, return
                    }
                }
                isValidInput = errorHandler(inputDomain.outOfRangeMessage);     // if not, error
            } else {                                                        // if not given accepted character range
                return myChar;                                                  // return
            }

        } while (!isValidInput);
        return 'e'; // unreachable
    }

    char getChar(String message, char... domain) throws Exception { // set char domain then get char
        allowedChars = domain;
        return getChar(message);
    }

    char getChar(String message, String errorMessage, char... domain) throws Exception {    // set char domain and out-of-range error message then get char, note that error message going to reset to previous error message after this method.
        String currentErrorMessage = inputDomain.outOfRangeMessage;
        inputDomain.outOfRangeMessage = errorMessage;
        char myChar = getChar(message, domain);
        inputDomain.outOfRangeMessage = currentErrorMessage;
        return myChar;
    }

    String tryParseDouble(String rawData) {
        ReturnIfValid<Boolean, Double> goodInput;
        goodInput = checkInputIsValid(rawData);
        if (inputIsInvalid(goodInput)) {
            return "Invalid";
        }
        return String.valueOf(goodInput.VALUE);
    }

    private boolean inputIsInvalid(ReturnIfValid<Boolean, Double> goodInput) {    // check if is double and is in range
        if (!goodInput.ISVALID)  // if not even a double return error
            return !errorHandler(inputTypeErrorMessage);
        if (!(checkInputIsInRange(goodInput.VALUE))) // if not in range return error
            return !errorHandler(inputDomain.outOfRangeMessage);
        return false;
    }

    private boolean checkInputIsInRange(double input) { // check if in range
        switch (inputDomain.domain) {
            case hasMaximum:
                return input <= inputDomain.max;
            case hasMinimum:
                return input >= inputDomain.min;
            case limited:
                return input <= inputDomain.max && input >= inputDomain.min;
            default:
                return true;
        }
    }

    private ReturnIfValid<Boolean, Double> checkInputIsValid(String input) {  // check if is double
        double goodInput = 0.0;
        boolean isGoodInput = true;
        try {
            goodInput = Double.parseDouble(input);  // try to parse string to double, fail -> error
        } catch (NumberFormatException e) {
            isGoodInput = false;
        }
        return new ReturnIfValid<>(isGoodInput, goodInput);    // returns boolean "isValid" and double "value"
    }

    private boolean errorHandler(String errorMessage) { // out print error message and always return false.
        if (inputTriesLimiter.userApproach == inputTriesLimiter.userMaxTries) { // if max tries exceed
            System.out.println(inputTriesLimiter.errorMessage); // print error and exit program
            System.exit(1);
        }
        switch (readerType) {   // print message, (for joptionpane reader) pop error message window.
            case jOptionPane:
                JOptionPane.showMessageDialog(null, errorMessage, "Input Error", JOptionPane.WARNING_MESSAGE);
            default:
                System.out.println(errorMessage + " (tries: " + ++inputTriesLimiter.userApproach + "/3)");
        }
        return false;
    }

    private void reset() {  // reset user current attempt
        inputTriesLimiter.userApproach = 0;
    }
}

class BuildInReader {   // build-in reader, have all four kinds of java reader
    private Scanner scanner;
    private Console console;
    private BufferedReader bufferedReader;
    private ReaderType reader;

    BuildInReader(ReaderType readerType) {  // init set prefer reader
        reader = readerType;
        switch (reader) {
            case bufferedReader:
                bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                break;
            case jOptionPane:
                break;
            case console:
                console = System.console();
                break;
            case scanner:
                scanner = new Scanner(System.in);
        }
    }

    String getLine(String message) throws Exception {   // get string with prefer reader
        String userInput = "";
        switch (reader) {
            case bufferedReader:
                userInput = bufferedReader.readLine();
                break;
            case jOptionPane:
                userInput = JOptionPane.showInputDialog(message);
                break;
            case console:
                userInput = console.readLine();
                break;
            case scanner:
                userInput = scanner.nextLine();
        }
        return userInput;
    }

    void close() throws Exception { // close reader
        switch (reader) {
            case bufferedReader:
                bufferedReader.close();
                break;
            case jOptionPane:
                break;
            case console:
                break;
            case scanner:
                scanner.close();
        }
    }
}