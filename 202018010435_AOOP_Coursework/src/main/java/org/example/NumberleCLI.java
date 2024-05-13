package org.example;

import java.awt.*;
import java.util.Scanner;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;



public class NumberleCLI {


    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GRAY = "\u001B[90m";

    private static final INumberleModel model = new NumberleModel();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        model.startNewGame();
        GAME();
    }

    private static boolean correct(String input) {
        String[] EQUAL = input.split("=");
        if (EQUAL.length != 2) {
            return false;
        }
        Expression expLeft = new ExpressionBuilder(EQUAL[0].trim()).build();
        double leftResult = expLeft.evaluate();
        Expression expRight = new ExpressionBuilder(EQUAL[1].trim()).build();
        double rightResult = expRight.evaluate();
        if (leftResult!=rightResult){
            System.out.println("the left side is not equal right side ");
        }
        return Double.compare(leftResult, rightResult) == 0;
    }
    private static void GAME() {
        while (!model.isGameOver()) {
            KeyBoard();
            Operator();
            String input = Input();
            if(input.length()<7){
                System.out.println("your input length is must equal 7");
            }
            if (input.length()==7){
                if (correct(input)) {
                    boolean correct = model.Pr(input);

                    COLOR(input, model.getTargetWord());
                    if (correct) {
                        break;
                    }
                } else {
                    System.out.println("");
                }
            }
        }

        if (model.isGameWon()) {
            System.out.println("you won the game!");

        } else {
            System.out.println(" sorry you lose the game !");
            System.out.println("The correct answer is " + model.getTargetWord());

        }
    }

    private static void Operator() {
        System.out.println("operator: + - * /");
        System.out.println("number: 0 1 2 3 4 5 6 7 8 9");
        System.out.println("other symbol: =");
    }

    private static String Input() {
        System.out.print("please enter your guess: ");
        return scanner.nextLine();
    }
    private static void KeyBoard() {
        System.out.println("you have  : " + model.getRemainingAttempts()+" "+"chances to try ");
        System.out.println("you have : " + model.getTargetWord());
        System.out.println("your input guess is : " + model.getCurrentGuess());

    }
    private static void COLOR(String input, String Tarts) {
        StringBuilder inputs = new StringBuilder();
        inputs.setLength(0);
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (i < Tarts.length() && c == Tarts.charAt(i)) {
                inputs.append(ANSI_GREEN).append(c);
            } else if (Tarts.contains(String.valueOf(c))) {
                inputs.append(ANSI_YELLOW).append(c);
            } else {
                inputs.append(ANSI_GRAY).append(c);
            }
        }
        inputs.append(ANSI_RESET);
        System.out.println(inputs.toString());
    }
}
