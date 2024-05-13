package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.FileNotFoundException;

// NumberleModel.java
public class NumberleModel extends java.util.Observable implements INumberleModel{

    private String TargetWord;
    public ArrayList<String> NumberList=new ArrayList<>();
    public int remainAttempts=6  ;
    public boolean gamewon;
    public boolean Flag1=false;
    public boolean Flag2=false ;
    public boolean Flag3;
//    private ArrayList<String> equations = new ArrayList<>();
    public String DefaultNumberal="5*1+2=7";

    private StringBuilder CurrentGuess;



    public void initialize(){

        this.LoadList();
        Random random=new Random();
        int index =random.nextInt(NumberList.size());
        if (Flag1==true){
            System.out.println("this is not ");
        }
        if (Flag2==true){
            System.out.println(getTargetWord());
        }
        if (Flag3==true){
            TargetWord= DefaultNumberal;
        }else {
            TargetWord=NumberList.get(index);
        }
        CurrentGuess=new StringBuilder(TargetWord.length());
        remainAttempts=MAX_ATTEMPTS;
        gamewon=false;
        setChanged();
        notifyObservers();

    }
    public boolean ProcessInput(String input){


        if (input.length() < 7){
            return false;
        }
        CurrentGuess = new StringBuilder(input);

        if (CurrentGuess.toString().equals(TargetWord)) {
            gamewon = true;
        }
        setChanged();
        notifyObservers();
        return gamewon;
    }

    public void remove(){
        if (CurrentGuess.length()>0){
            CurrentGuess.deleteCharAt(CurrentGuess.length()-1);
            setChanged();
            notifyObservers();
        }
    }


    public  void LoadList(){
        try {
            File file=new File("D:/IDEA/ww/src/main/java/org/example/equations.txt");
            assert file.exists():"File should be exists";
            Scanner scanner =new Scanner(file);

            while(scanner.hasNextLine()){
                this.NumberList.add(scanner.nextLine());

            }
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("This File is can't read ");
        }

    }


    @Override
    public boolean processInput(String input) {
        return ProcessInput(input);
    }

    @Override
    public boolean isGameOver() {
        assert remainAttempts >= 0 : "remainAttempts should be non-negative";
        return remainAttempts <= 0 || gamewon ;
    }

    @Override
    public boolean isGameWon() {
        return gamewon;
    }



    @Override
    public void setFlag(boolean flag2) {
        this.Flag2=flag2;
    }
    @Override
    public void setFlag3(boolean flag3) {
        this.Flag3=flag3;
    }




    @Override
    public void setTargetword(String targetword) {
        assert targetword != null : "targetword should not be null";
        assert targetword.length() >= 7 : "targetword length should be >= 7";
        this.TargetWord=targetword;
    }

    @Override
    public String getTargetWord() {
        return TargetWord;
    }

    @Override
    public StringBuilder getCurrentGuess() {
        return CurrentGuess;
    }

    @Override
    public int getRemainingAttempts() {
        assert remainAttempts >= 0 : "remainAttempts should be non-negative";
        return remainAttempts;
    }

    @Override
    public void setRemainingAttempts(int attempt) {
        assert attempt>= 0 : "val should be non-negative";
        remainAttempts = attempt;

    }

    @Override
    public boolean Pr(String input) {
        assert input.length() <=7 : "Input length should be <= 7";
        if (input.length() < 7){
            System.out.println("input length must =7");
        }
        CurrentGuess = new StringBuilder(input);

        if (CurrentGuess.toString().equals(TargetWord)) {
            gamewon = true;
        }else {
            remainAttempts--;

        }
        return  gamewon;

    }

    @Override
    public boolean startNewGame() {
        initialize();
        return false;
    }

    @Override
    public String DefaultWord() {
        return null;
    }

}
