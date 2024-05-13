package org.example;

import java.util.ArrayList;
import java.util.Random;

public class NumberleController {

    private NumberleView view;
    private INumberleModel model;

    public NumberleController(INumberleModel model) {
        this.model = model;
    }
    public int getRemainingAttempts() {
        return model.getRemainingAttempts();
    }
    public void startNewGame() {
        model.startNewGame();
    }


    public void SetFlag(Boolean constant) {
        if (constant) {
            // list
            ArrayList<String> equations = new ArrayList<>();
            equations.add("5*1+2=7");
            equations.add("1+6=5+2");
            equations.add("2*4=3+5");
            equations.add("3-1=2*1");

            // random
            Random random = new Random();
            // choose one
            String selectedEquation = equations.get(random.nextInt(equations.size()));
            // set one
            model.setTargetword(selectedEquation);
        }
    }
//public void SetFlag(Boolean Constant) {
//    if (Constant) {
//        Random random = new Random();
//        if (model.getFlag3()) {
//            ArrayList<String> equations = model.getEquations();
//            int index = random.nextInt(equations.size());
//            model.setTargetword(equations.get(index));
//        } else {
//            model.setTargetword("5*1+2=7");
//        }
//    }
//}
    public void Flag2(Boolean flag){
        model.setFlag(flag);
        System.out.println("It's not correct answer,please try again and you have "+getRemainingAttempts()+" "+"chance ");

    }
    public void Flag3(Boolean Flag){
        model.setFlag(Flag);
        System.out.println(getTargetWord());
    }
    public void setView(NumberleView view) {
        this.view = view;
    }
    public String getTargetWord() {
        return model.getTargetWord();
    }
    public void setRemainingAttempts(int val) {
        model.setRemainingAttempts(val);
    }
    public void processInput(String input) {
       model.processInput(input);
    }
    public boolean isGameOver() {
        return model.isGameOver();
    }
    public boolean isGameWon() {
        return model.isGameWon();
    }

}
