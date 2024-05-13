package org.example;


public class Main {
    public static void main(String[] args) {

        INumberleModel model = new NumberleModel();
        NumberleController controller = new NumberleController(model);
        NumberleView view = new NumberleView(model, controller);


    }
}