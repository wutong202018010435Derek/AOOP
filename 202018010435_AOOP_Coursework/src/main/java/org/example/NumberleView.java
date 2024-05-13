package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class NumberleView extends JFrame implements Observer {
    private  JPanel Panel;

    private NumberleController controller;
    private final INumberleModel model;
    public JButton button[][]=new JButton[6][7];
    private JTextField TextField;
    private int currentRow=0;
    public static final int ROWS = 6;
    public static final int COLS = 7;
    public static final int MAX_INPUT_LENGTH = 7;
    private static final Color CORRECT_COLOR = Color.GREEN;
    //The entered text contains letters but in the wrong order
    private static final Color INCORRECT_COLOR = Color.YELLOW;
    private final Color defaultColor = UIManager.getColor("Button.background");
    //
    private JPanel row1;
    private JPanel row2;


    public NumberleView(INumberleModel model,NumberleController controller){
        this.controller=controller;
        this.model=model;
        controller.startNewGame();
        Initialize();
        showGameStartReminder();
        ((NumberleModel)this.model).addObserver(this);
        this.controller.setView(this);
        update((NumberleModel)this.model, null);

    }

    public void Initialize(){

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Numberle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        //set total page size width and height
        setSize(800, 800);
        setLayout(new BorderLayout());
        Panel = new JPanel(new GridLayout(6, 7)); // Use GridLayout for buttons


        setLocationRelativeTo(null);
        JPanel inputPanel=new JPanel();
        TextField=new JTextField(7);
        inputPanel.add(TextField);
        add(inputPanel,BorderLayout.NORTH);
        inputPanel.setVisible(false);
        String input=TextField.getText();

        assert (input)!=null;

        JPanel keyBoardPanel = new JPanel();
        keyBoardPanel.setLayout(new BoxLayout(keyBoardPanel, BoxLayout.Y_AXIS));
        row1=new JPanel();
        row1.setLayout(new FlowLayout());
        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setFont(new Font("Serif", Font.PLAIN, 30));
            button.addActionListener(new ButtonClickListener());
            row1.add(button);
        }
        JButton zeroButton = new JButton("0");
        zeroButton.setFont(new Font("Serif", Font.PLAIN, 30));
        zeroButton.addActionListener(new ButtonClickListener());
        row1.add(zeroButton);

        row2 = new JPanel();
        row2.setLayout(new FlowLayout());
        String[] operators = {"Delete", "+", "-", "*", "/", "=", "Enter"};
        for (String operator : operators) {
            JButton button = new JButton(operator);
            button.setFont(new Font("Serif", Font.PLAIN, 30));
            button.addActionListener(new ButtonClickListener());
            row2.add(button);
        }
        keyBoardPanel.add(row1);
        keyBoardPanel.add(row2);

        add(keyBoardPanel, BorderLayout.SOUTH);

        JPanel ButtonPanel=new JPanel();


        JButton yoursetting=new JButton("Constant_Word");

        JButton target=new JButton("Target");

        yoursetting.addActionListener(e -> {
            Fixed();
        });
        target.addActionListener(e -> {
            target();
        });



        ButtonPanel.add(target);
        ButtonPanel.add(yoursetting);
        add(ButtonPanel,BorderLayout.NORTH);


        for (int i=0;i<6;i++){
            for (int j=0;j<7;j++){
                button[i][j]=new JButton("");
                button[i][j].setPreferredSize(new Dimension(100,100));
                Panel.add(button[i][j]);
            }
            add(Panel, BorderLayout.CENTER); // 移到循环外部
            Panel.setVisible(true);

        }


        setVisible(true);
        change();

    }

    public void Fixed(){
        controller.SetFlag(true);
        System.out.println(controller.getTargetWord());
    }

    public void change() {
        String input = TextField.getText();
        updateButtonsFromInput(input);
    }


    public void target(){
        controller.Flag3(true);
    }


    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String buttonText = source.getText();
            switch (buttonText) {
                case "Delete":
                    clearDisplay();
                    break;
                case"Enter":
                    Enter();
                    break;
                default:
                    if (TextField.getText().length() < MAX_INPUT_LENGTH) {
                        updateDisplay(buttonText);
                        updateButtonsFromInput(TextField.getText());
                    }
                    controller.isGameOver();
            }
        }
    }

    private void TargetColor(char[] target, String input) {
        char[] userInput = input.toCharArray();

        for (int col = 0; col < 7; col++) {

            char targetChar = target[col];
            char inputChar = userInput[col];

            if (targetChar == inputChar) {
                button[currentRow][col].setBackground(Color.GREEN);
            } else if (new String(target).indexOf(inputChar) != -1) {
                button[currentRow][col].setBackground(Color.ORANGE);
            } else {
                button[currentRow][col].setBackground(Color.GRAY);
            }

            button[currentRow][col].setText(String.valueOf(inputChar));


            String buttonText = button[currentRow][col].getText();


            for (Component component : row1.getComponents()) {
                if (component instanceof JButton) {
                    JButton keyboardButton = (JButton) component;
                    if (keyboardButton.getText().equals(buttonText)) {
                        if (targetChar == inputChar) {
                            keyboardButton.setBackground(Color.GREEN);
                        } else if (new String(target).indexOf(inputChar) != -1) {
                            keyboardButton.setBackground(Color.ORANGE);
                        } else {
                            keyboardButton.setBackground(Color.GRAY);
                        }
                        break;
                    }
                }
            }


            for (Component component : row2.getComponents()) {
                if (component instanceof JButton) {
                    JButton keyboardButton = (JButton) component;
                    if (keyboardButton.getText().equals(buttonText)) {
                        if (targetChar == inputChar) {
                            keyboardButton.setBackground(Color.GREEN);
                        } else if (new String(target).indexOf(inputChar) != -1) {
                            keyboardButton.setBackground(Color.ORANGE);
                        } else {
                            keyboardButton.setBackground(Color.GRAY);
                        }
                        break;
                    }
                }
            }
        }
    }



    private void updateButtonsFromInput(String input) {
        int rowIndex = currentRow;

        int colIndex = 0;
        char[] chars = input.toCharArray();

        for (char c : chars) {
            if (Character.isDigit(c) || "+-*/=".indexOf(c) != -1) {
                if (colIndex < 7) {
                    button[rowIndex][colIndex].setText(String.valueOf(c));
                    colIndex++;
                }
            }
        }
        for (int clearIndex = colIndex; clearIndex < 7; clearIndex++) {
            button[rowIndex][clearIndex].setText("");
        }


    }

    public void Enter() {
        String input = TextField.getText();

        // Check if input length is less than 7
        if (input.length() < 7) {
            controller.Flag2(true);
            TooShort();

            return; // Return to avoid further processing
        }

        boolean containsOperator = false; // Flag to check if an operator is present

        for (char c : input.toCharArray()) {
            if ("+-*/".indexOf(c) != -1) {
                containsOperator = true; // Set flag if an operator is found
                break;
            }
        }

        if (input.length() == 7) {
            boolean containsEquals = input.contains("=");
            if (!containsEquals) {

                NoEqualSign();
            } else if (!containsOperator) {
                Symobol();
            } else {
                boolean isCorrect = validateEquation(input);
                if (isCorrect) {
                    char[]target=controller.getTargetWord().toCharArray();
                    validateEquation(input);

                    controller.setRemainingAttempts(controller.getRemainingAttempts()-1);

                    updateButtonsFromInput(input); // Update buttons based on the input4
                    TargetColor(target,input);
                    currentRow++;
                    if (currentRow>5){
                       currentRow=0;
                    }
                    controller.processInput(input);
                    TextField.setText(""); // Clear the input text field
                    if (controller.getRemainingAttempts()==0){
                        LOSEGame();
                    }
                }
                else{

                    NotEqual();
                }
            }
        }

    }






    private boolean validateEquation(String input) {
        String[] parts = input.split("=");
        if (parts.length != 2) {
            return false;
        }

        Expression expLeft = new ExpressionBuilder(parts[0].trim()).build();
        double leftResult = expLeft.evaluate();
        Expression expRight = new ExpressionBuilder(parts[1].trim()).build();
        double rightResult = expRight.evaluate();

        return Double.compare(leftResult, rightResult) == 0;
    }






    public void clearDisplay(){
        String currentText=TextField.getText();
        if (!currentText.isEmpty()){
            String newText=currentText.substring(0,currentText.length()-1);
            TextField.setText(newText);
            updateButtonsFromInput(newText);


        }


    }



    public void updateDisplay(String text){
        if (TextField.getText().length()+text.length()>7){
            text = text.substring(0, 7 - TextField.getText().length());
        }
        TextField.setText(TextField.getText()+text);

    }



    public void TooShort(){
        JFrame reminderFrame = new JFrame("reminder");
        reminderFrame.setBounds(625,300, 400, 200);
        reminderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel reminderLabel = new JLabel("Too Short !");
        reminderLabel.setFont(new Font("Serif", Font.BOLD, 25));
        reminderLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel reminderPanel = new JPanel(new BorderLayout());
        reminderPanel.add(reminderLabel, BorderLayout.CENTER);
        reminderFrame.getContentPane().add(reminderPanel);



        reminderFrame.setVisible(true);

        Timer timer = new Timer(2000, e -> {
            reminderFrame.dispose();
        });
        timer.setRepeats(false);
        timer.start();

    }

    public void Symobol(){
        JFrame reminderFrame = new JFrame("reminder");
        reminderFrame.setBounds(625,300, 400, 200);
        reminderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel reminderLabel = new JLabel("There must be at least one sign +-*/ !");
        reminderLabel.setFont(new Font("Serif", Font.BOLD, 25));
        reminderLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel reminderPanel = new JPanel(new BorderLayout());
        reminderPanel.add(reminderLabel, BorderLayout.CENTER);
        reminderFrame.getContentPane().add(reminderPanel);



        reminderFrame.setVisible(true);

        Timer timer = new Timer(2000, e -> {
            reminderFrame.dispose();
        });
        timer.setRepeats(false);
        timer.start();


    }


    public void NotEqual(){
        JFrame reminderFrame = new JFrame("reminder");
        reminderFrame.setBounds(625,300, 400, 200);
        reminderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel reminderLabel = new JLabel("the left side is not equal to the right");
        reminderLabel.setFont(new Font("Serif", Font.BOLD, 25));
        reminderLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel reminderPanel = new JPanel(new BorderLayout());
        reminderPanel.add(reminderLabel, BorderLayout.CENTER);
        reminderFrame.getContentPane().add(reminderPanel);



        reminderFrame.setVisible(true);

        Timer timer = new Timer(2000, e -> {
            reminderFrame.dispose();
        });
        timer.setRepeats(false);
        timer.start();

    }

    private void showGameStartReminder() {
        JFrame reminderFrame = new JFrame("reminder");
        reminderFrame.setBounds(625,300, 400, 200);
        reminderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel reminderLabel = new JLabel("Guess the first equations！");
        reminderLabel.setFont(new Font("Serif", Font.BOLD, 25));
        reminderLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel reminderPanel = new JPanel(new BorderLayout());
        reminderPanel.add(reminderLabel, BorderLayout.CENTER);
        reminderFrame.getContentPane().add(reminderPanel);



        reminderFrame.setVisible(true);

        Timer timer = new Timer(2000, e -> {
            reminderFrame.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void NoEqualSign(){
        JFrame reminderFrame = new JFrame("reminder");
        reminderFrame.setBounds(625,300, 400, 200);
        reminderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel reminderLabel = new JLabel("No equal = sign !");
        reminderLabel.setFont(new Font("Serif", Font.BOLD, 25));
        reminderLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel reminderPanel = new JPanel(new BorderLayout());
        reminderPanel.add(reminderLabel, BorderLayout.CENTER);
        reminderFrame.getContentPane().add(reminderPanel);



        reminderFrame.setVisible(true);

        Timer timer = new Timer(2000, e -> {
            reminderFrame.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void LOSEGame(){
        JFrame reminderFrame = new JFrame("reminder");
        reminderFrame.setBounds(625,300, 400, 200);
        reminderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Button Restart=new Button("Replay");
        Restart.addActionListener(e -> {

            controller.startNewGame();
            currentRow=0;



            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    button[row][col].setText("");
                    button[row][col].setBackground(defaultColor);
                }
            }

            for (Component component : row1.getComponents()) {
                if (component instanceof JButton) {
                    component.setEnabled(true);
                    component.setBackground(defaultColor);
                }
            }
            for (Component component : row2.getComponents()) {
                if (component instanceof JButton) {
                    component.setEnabled(true);
                    component.setBackground(defaultColor);
                }
            }
            reminderFrame.dispose();

        });
        Restart.setBounds(10,20,10,10);

        JLabel reminderLabel = new JLabel("You lost!");
        reminderLabel.setFont(new Font("Serif", Font.BOLD, 25));

        reminderLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel reminderPanel = new JPanel(new BorderLayout());
        reminderPanel.add(reminderLabel, BorderLayout.CENTER);
        reminderPanel.add(Restart,BorderLayout.NORTH);

        reminderFrame.getContentPane().add(reminderPanel);

        Restart.setVisible(true);
        reminderFrame.setVisible(true);

    }

    public void WonGame(){
        JFrame reminderFrame = new JFrame("reminder");
        reminderFrame.setBounds(625,300, 400, 200);
        reminderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Button Restart=new Button("Restart");
        Restart.addActionListener(e -> {
            currentRow=0;
            controller.startNewGame();


            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    button[row][col].setText("");
                    button[row][col].setBackground(defaultColor);
                }
            }

            for (Component component : row1.getComponents()) {
                if (component instanceof JButton) {
                    component.setEnabled(true
                    );
                    component.setBackground(defaultColor);
                }
            }
            for (Component component : row2.getComponents()) {
                if (component instanceof JButton) {
                    component.setEnabled(true);
                    component.setBackground(defaultColor);
                }
            }
            reminderFrame.dispose();

        });
        Restart.setBounds(10,20,10,10);

        JLabel reminderLabel = new JLabel("You won!");
        reminderLabel.setFont(new Font("Serif", Font.BOLD, 25));

        reminderLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel reminderPanel = new JPanel(new BorderLayout());
        reminderPanel.add(reminderLabel, BorderLayout.CENTER);
        reminderPanel.add(Restart,BorderLayout.NORTH);

        reminderFrame.getContentPane().add(reminderPanel);


        Restart.setVisible(true);
        reminderFrame.setVisible(true);

    }

    @Override
    public void update(Observable o, Object arg) {
        controller.getRemainingAttempts();

        if (controller.isGameOver()) {
            if (controller.isGameWon()) {

                WonGame();
            } else if (controller.getRemainingAttempts() == 0) {
                LOSEGame();
            }

            for (Component component : row1.getComponents()) {
                if (component instanceof JButton) {
                    component.setEnabled(false);
                }
            }
            for (Component component : row2.getComponents()) {
                if (component instanceof JButton) {
                    component.setEnabled(false);
                }
            }
        }
    }
}
