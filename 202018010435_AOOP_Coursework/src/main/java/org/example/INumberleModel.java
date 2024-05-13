package org.example;

import java.util.ArrayList;

public interface INumberleModel{
    int MAX_ATTEMPTS = 6;


    void initialize();
    boolean processInput(String input);
    boolean isGameOver();
    boolean isGameWon();

    void setFlag3(boolean flag3);


    void setFlag(boolean flag1);
    void setTargetword(String targetword);
    String getTargetWord();
    StringBuilder getCurrentGuess();
    int getRemainingAttempts();
    void setRemainingAttempts(int val);
    boolean Pr(String input);
    boolean startNewGame();
    String DefaultWord();

    void remove( );

}



