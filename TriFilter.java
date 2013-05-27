package ca.etsmtl.log430.lab1;

import java.io.PipedReader;
import java.io.PipedWriter;

/**************************************************************************************
 ** Class name: TriFilter
 ** Original author: Simon Bégin & Etienne B. Auger
 ** Date: 05/26/13
 ** Version 1.0
 **
 **
 ***************************************************************************************/

public class TriFilter extends Thread {
	
	boolean Done;
	int LengthLineOfText = 0;
	
	PipedReader inputPipe = new PipedReader();
	PipedWriter outputPipe = new PipedWriter();
	
	public TriFilter(PipedWriter InputPipe1, PipedWriter OutputPipe1) {
		try {
			this.inputPipe.connect(InputPipe1);
			System.out.println("TriFilter:: pipe connectée.");
		}
		catch (Exception Error){
			System.out.println("TriFilter:: la connexion avec la pipe a échoué.");
		}
		
		try {
			this.outputPipe = OutputPipe1;
			System.out.println("TriFilter:: pipe de retour connectée");
		}
		catch (Exception Error) {
			System.out.println("TriFilter:: la connexion avec la pipe de retour a échoué.");
		}
	}
	
	public void run() {
		
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
						
						String billet = LineOfText.substring(LengthLineOfText + 0, LengthLineOfText + 4); // numero du billet
						String type = LineOfText.substring(LengthLineOfText + 5, LengthLineOfText + 8); // Le type
						String severite = LineOfText.substring(LengthLineOfText + 22, LengthLineOfText + 25); // Sévérité
						String statut = LineOfText.substring(LengthLineOfText + 26, LengthLineOfText + 29); // Le Statut
						String newLineOfText = statut + " " + severite + " " + type + " " + billet+ "\n";
						
						LengthLineOfText = LineOfText.length();
						
						System.out.println("TriFilter:: sending: "
								+ newLineOfText + " to output pipe.");
						
						outputPipe.write(newLineOfText, 0, newLineOfText.length());
						outputPipe.flush(); 
					} 
					
					else {
						LineOfText += new String(CharacterValue);
					}
				}
			}
		} 
		catch (Exception Error) {
			System.out.println("TriFilter:: Interrupted." + Error);
		} 

		try {
			inputPipe.close();
			System.out.println("TriFilter:: input pipe closed.");

			outputPipe.close();
			System.out.println("TriFilter:: output pipe closed.");
		} 
		catch (Exception Error) {
			System.out.println("TriFilter:: Error closing pipes.");
		}
	
		
	}
}
