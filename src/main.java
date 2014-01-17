import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class main implements ActionListener {
	
JFrame frame = new JFrame("tylerBrothersHangMan");
JFrame fam = new JFrame("tylerBrothersHangMan");
JPanel statusPanel = new JPanel();
JPanel actionPanel = new JPanel();
JPanel inputPanel = new JPanel();
JButton startButton = new JButton("Start New Game");
JButton goButton = new JButton("Ready");
Integer numAttemptsLeft = 5;
Label labelAttemptsLeft = new Label("Attempts left:" + numAttemptsLeft);
String stringFromFile = "";
String fileLoc = "";
JTextField consoleTextField = new JTextField("Press Start Game at the top to begin playing.");
JTextField fileText = new JTextField("Type file location here.");
JTextArea consoleTextArea = new JTextArea(10,1);
ArrayList<JButton> arrayJButtonLetters = new ArrayList<JButton>();

	public static void main(String[] args) {
		new main().selectTextFile();
		//new main().getWordFromFile();
	}
	
	public void selectTextFile(){
		fam.setSize(new Dimension(400,150));
		fam.setLayout(new BorderLayout());
		
		fileText.setEditable(true);
		fileText.setPreferredSize(new Dimension(800,50));
		
		goButton.setPreferredSize(new Dimension(50,50));
		goButton.addActionListener(this);
		
		fam.add(fileText,BorderLayout.NORTH);
		fam.add(goButton,BorderLayout.SOUTH);
		fam.show();
	}
	
	public void startGame(){
		frame.setSize(new Dimension(800,200));
		frame.setBackground(Color.white);
		frame.setLayout(new BorderLayout());
		
		startButton.setPreferredSize(new Dimension(200,20));
		startButton.setName("startButton");
		startButton.addActionListener(this);
		
		statusPanel.setPreferredSize(new Dimension(800,30));
		statusPanel.add(startButton);
		statusPanel.add(labelAttemptsLeft);
		statusPanel.setBackground(Color.gray);
		
		actionPanel.setPreferredSize(new Dimension(800, 70));
		actionPanel.setLayout(new FlowLayout());

		inputPanel.setPreferredSize(new Dimension(800,100));
		inputPanel.setBackground(Color.blue);
		inputPanel.setLayout(new GridLayout(2,1));
		consoleTextArea.setEditable(false);
		consoleTextField.setEditable(true);
		consoleTextField.addActionListener(this);
		inputPanel.add(consoleTextArea);
		inputPanel.add(consoleTextField);
		
		frame.add(statusPanel,BorderLayout.NORTH);
		frame.add(actionPanel,BorderLayout.CENTER);
		frame.add(inputPanel,BorderLayout.SOUTH);
		frame.show();
	}
	
	public String getWordFromFile(){ //make all lower case
	    ArrayList<String> reservoir = new ArrayList<String>();
	    Random generator = new Random();
	    
	    try {
	    	Scanner s = new Scanner(new File(fileLoc));
	    	while(s.hasNext()){
	    		reservoir.add(s.next().toLowerCase());
	    	}

	    } catch (FileNotFoundException e) {
	      System.out.println("File not found!");
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    
	    int i = generator.nextInt(reservoir.size());
	    return reservoir.get(i);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(startButton)){
			actionPanel.removeAll();
			consoleTextArea.setText("Guessed letters: ");
			consoleTextField.setText("Type any lower case letter here and press ENTER to guess a letter.");
			consoleTextField.setEnabled(true);
			arrayJButtonLetters = new ArrayList<JButton>();
			numAttemptsLeft = 5;
			labelAttemptsLeft.setText("Attempts left:" + numAttemptsLeft.toString());
			
			stringFromFile = getWordFromFile();
			System.out.println("Psssst, the answer is: " + stringFromFile);
			
			for(int i = 0; i < stringFromFile.length(); i++){
				arrayJButtonLetters.add(new JButton(" - "));
			}
			
			for(int a = 0; a < arrayJButtonLetters.size();a++){
				actionPanel.add(arrayJButtonLetters.get(a));
			}
			frame.show();
		}
		
		else if(e.getSource().equals(consoleTextField)){
			boolean indicator = false;
			
			for(int b = 0; b < arrayJButtonLetters.size();b++){
				if(stringFromFile.substring(b,b+1).equals(consoleTextField.getText().toLowerCase())){
					arrayJButtonLetters.get(b).setText(consoleTextField.getText());
					indicator = true;
				}
			}
			
			if(indicator && arrayJButtonLetters.size() > 0){ // .size() > 0 so before Create Game is pressed there is no action
					consoleTextArea.append(consoleTextField.getText() + " (C), ");
					
			}else if (indicator == false && arrayJButtonLetters.size() > 0){
				consoleTextArea.append(consoleTextField.getText() + " (W), ");
				numAttemptsLeft--;
				labelAttemptsLeft.setText("Attempts left:" + numAttemptsLeft.toString());
				if(numAttemptsLeft == 0){
					consoleTextArea.setText("YOU LOSE");
					consoleTextField.setEnabled(false);
				}
			}
			
			indicator = true; //RE USING INDICATOR TO TELL WHETHER I HAVE WON
			for(int d = 0; d < arrayJButtonLetters.size();d++){
				if(arrayJButtonLetters.get(d).getText().equals(" - ")){
					indicator = false;
					break;
				}
			}
			if(indicator == true && numAttemptsLeft > 0){
				consoleTextArea.setText("YOU WIN");
			}
			
			consoleTextField.setText("");
			frame.show();
		}
		
		else if(e.getSource().equals(goButton)){
			fileLoc = fileText.getText();
			try {
				Scanner s = new Scanner(new File(fileLoc));
				fam.hide();
				startGame();
			} catch (FileNotFoundException e1) {
				fileText.setText("FILE NOT FOUND, TRY AGAIN!");
			}

		}
	}
}
