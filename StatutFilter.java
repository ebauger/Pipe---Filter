package ca.etsmtl.log430.lab1;

import java.io.PipedReader;
import java.io.PipedWriter;

/**************************************************************************************
 ** Class name: StatutFilter
 ** Original author: Simon Bégin & Etienne B. Auger
 ** Date: 05/26/13
 ** Version 1.0
 **
 **
 ***************************************************************************************/

public class StatutFilter extends Thread {
	
	boolean Done;
	int LengthLineOfText = 0;
	
	PipedReader inputPipe = new PipedReader();
	PipedWriter outputPipe = new PipedWriter();
	
	public StatutFilter(PipedWriter InputPipe1, PipedWriter OutputPipe1) {
		try {
			this.inputPipe.connect(InputPipe1);
			System.out.println("StatutFilter:: pipe connectée.");
		}
		catch (Exception Error){
			System.out.println("StatutFilter:: la connexion avec la pipe a échoué.");
		}
		
		try {
			this.outputPipe = OutputPipe1;
			System.out.println("StatutFilter:: pipe de retour connectée");
		}
		catch (Exception Error) {
			System.out.println("StatutFilter:: la connexion avec la pipe de retour a échoué.");
		}
	}
	
	public void run() {
		
		String ASS ="";
		String NOU ="";
		String RES ="";
		String ROU ="";
		
		char[] CharacterValue = new char[1];
		// char array is required to turn char into a string
		String LineOfText = "";
		// string is required to look for the keyword
		int IntegerCharacter; // the integer value read from the pipe

		try {
			Done = false;
			
			while (!Done) {

				IntegerCharacter = inputPipe.read();
				CharacterValue[0] = (char) IntegerCharacter;

				if (IntegerCharacter == -1) { // la pipe se ferme
					Done = true;
				} 
				else {
					if (IntegerCharacter == '\n') { // fin de la ligne

						LineOfText += new String(CharacterValue);
						
						String tempLineOFText = LineOfText.substring(LengthLineOfText + 0, LengthLineOfText + 16); // numero du billet
						
						if(tempLineOFText.indexOf("ASS") != -1){
							ASS += tempLineOFText +"\n";
						}
						
						if(tempLineOFText.indexOf("NOU") != -1){
							NOU += tempLineOFText +"\n";
						}
						
						if(tempLineOFText.indexOf("RES") != -1){
							RES += tempLineOFText +"\n";
						}
						
						if(tempLineOFText.indexOf("ROU") != -1){
							ROU += tempLineOFText +"\n";
						}
											
						LengthLineOfText = LineOfText.length();			
					} 
					
					else {
						LineOfText += new String(CharacterValue);
					}
				}
			}
			
			String newLineOfText = ASS + NOU + RES + ROU;
			
			System.out.println("StatutFilter:: sending: "
					+ newLineOfText + " to output pipe.");
			
			outputPipe.write(newLineOfText, 0, newLineOfText.length());
			outputPipe.flush(); 
			
		} 
		catch (Exception Error) {
			System.out.println("StatutFilter:: Interrupted." + Error);
		} 

		try {
			inputPipe.close();
			System.out.println("StatutFilter:: input pipe closed.");

			outputPipe.close();
			System.out.println("StatutFilter:: output pipe closed.");
		} 
		catch (Exception Error) {
			System.out.println("StatutFilter:: Error closing pipes.");
		}
	
		
	}
}
