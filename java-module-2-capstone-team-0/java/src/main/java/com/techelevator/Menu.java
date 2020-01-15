package com.techelevator;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Menu {

	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();//whatever they type
		try {
			int selectedOption = Integer.valueOf(userInput);//turn String into and int
			if(selectedOption > 0 && selectedOption <= options.length) {//if the int corresponds to a choice
				choice = options[selectedOption - 1];//choicce = options - 1 turning it into the location of the array
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n"); //lets user know entry was invalid
		}
		return choice;//object
	}

	private void displayMenuOptions(Object[] options) {//takes an array 
		out.println();
		for(int i = 0; i < options.length; i++) {//for each
			int optionNum = i+1;//the displayed number starts at 1 and the 1st location in the array/arraylist 0
			out.println(optionNum+") "+options[i]);//display
		}
		out.print("\nPlease choose an option >>> ");//prompt
		out.flush();//clears the buffer 
	}
}
