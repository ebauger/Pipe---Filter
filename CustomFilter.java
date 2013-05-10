package ca.etsmtl.log430.lab1;

import java.io.PipedReader;
import java.io.PipedWriter;

/**************************************************************************************
 ** Class name: CustomFiler
 ** Original author: Simon Bégin, Étienne B. Auger
 ** Date: 10/05/13
 ** Version 1.0
 **
 ***************************************************************************************
 ** Purpose: Assignment 1 for LOG430, Architecture logicielle.  This
 ** assignment is designed to illustrate a pipe and filter architecture.  For the 
 ** instructions, refer to the assignment write-up.
 **
 ** 
 **
 **
 ** 	See Main.java
 **
 ** Modification Log
 **************************************************************************************
 **
 **************************************************************************************/

public class CustomFilter extends Thread {
	boolean Done;
	PipedReader inputPipe = new PipedReader();
	PipedWriter outputPipe = new PipedWriter();

	public CustomFilter(PipedWriter inputPipe, PipedWriter outputPipe) {
		try {

			// Connect inputPipe to Main

			this.inputPipe.connect(inputPipe);
			System.out.println("CustomFilter :: connected to upstream filter.");

			// Connect outputPipe to Merge

			this.outputPipe = outputPipe;
			System.out.println("CustomFilter :: connected to downstream filter.");

		} catch (Exception Error) {
			System.out.println("CustomFilter :: " + Error);
		}
	}
	
	public void run() {
				char[] CharacterValue = new char[1];
				
				String LineOfText = "";
				
				int IntegerCharacter; 

				try {

					Done = false;

					while (!Done) {

						IntegerCharacter = inputPipe.read();
						CharacterValue[0] = (char) IntegerCharacter;

						if (IntegerCharacter == -1) { // pipe is closed

							Done = true;

						} else {

							if (IntegerCharacter == '\n') { // end of line

								System.out.println("SeverityFilter " + severity
										+ ":: received: " + LineOfText + ".");

								if (LineOfText.indexOf(severity) != -1) {

									System.out.println("SeverityFilter "
											+ severity + ":: sending: "
											+ LineOfText + " to output pipe.");
									LineOfText += new String(CharacterValue);
									outputPipe
											.write(LineOfText, 0, LineOfText.length());
									outputPipe.flush();

								} // if

								LineOfText = "";

							} else {

								LineOfText += new String(CharacterValue);

							} // if //

						} // if

					} // while

				} catch (Exception Error) {

					System.out.println("SeverityFilter::" + severity
							+ " Interrupted.");

				} // try/catch

				try {

					inputPipe.close();
					System.out.println("SeverityFilter " + severity
							+ ":: input pipe closed.");

					outputPipe.close();
					System.out.println("SeverityFilter " + severity
							+ ":: output pipe closed.");

				} catch (Exception Error) {

					System.out.println("SeverityFilter " + severity
							+ ":: Error closing pipes.");

				} 
	}
}