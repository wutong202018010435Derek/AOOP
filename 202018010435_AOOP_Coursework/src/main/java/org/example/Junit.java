package org.example;

import org.junit.Test;

import org.junit.Before;



import static org.junit.Assert.*;


public class Junit {
    private NumberleModel model;
    @Before
    public void Setup(){
        model=new NumberleModel();
    }
    @Test
    public void TestNumber(){
      model.setTargetword("3+3=3+3");
      assertEquals("3+3=3+3",model.getTargetWord());


    }
    @Test
    public void ProcessInput(){
        model.initialize();
        assertFalse(model.processInput("1+2*3=7"));
        assertFalse(model.isGameWon());
    }
    @Test
    public void Remove() {
        model.initialize();
        model.remove();
        assertEquals(0, model.getCurrentGuess().length());
    }
    @Test
    public void StartNewGame() {
        model.initialize();
        String oldWord = model.getTargetWord();
        model.startNewGame();
        assertNotEquals(oldWord, model.getTargetWord());
    }
    @Test
    public void TestRemainingAttempts() {
        model.initialize();
        int initialAttempts = model.getRemainingAttempts();
        model.setRemainingAttempts(initialAttempts - 1);
        assertEquals(initialAttempts - 1, model.getRemainingAttempts());
    }

 @Test
    public void Testing(){
        model.initialize();
        model.setTargetword("7+1=4+4");
       assertTrue(model.processInput("7+1=4+4"));
       assertTrue(model.gamewon);


 }


}

