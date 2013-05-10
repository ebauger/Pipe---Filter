package ca.etsmtl.log430.lab1;

import java.io.PipedReader;
import java.io.PipedWriter;

/**************************************************************************************
 ** Class name: MergeFilter
 ** Original author: A.J. Lattanze, CMU
 ** Date: 12/3/99
 ** Version 1.0
 **
 ** Adapted by R. Champagne, Ecole de technologie superieure
 ** 2002-May-08, 2011-Jan-12
 **
 ***************************************************************************************
 ** Purpose: Assignment 1 for LOG430, Architecture logicielle.  This
 ** assignment is designed to illustrate a pipe and filter architecture.  For the 
 ** instructions, refer to the assignment write-up.
 **
 ** Abstract: This class is intended to be a filter that will collect the streams from 
 **	     two input pipes and merge them into one output pipe.
 ** 
 ** Pseudo Code:
 **
 ** connect to input pipe 1
 ** connect to input pipe 2
 ** connect to output pipe
 **
 **	while not done
 **		read char1 from input pipe 1
 **		read char2 from input pipe 2
 **		string1 = string1 + char1
 **		string2 = string2 + char2
 **		write string1 to output pipe
 **		write string2 to output pipe
 **	end while
 **
 **	close pipes
 **	close file
 **
 ** Running the program
 **
 ** 	See Main.java
 **
 ** Modification Log
 **************************************************************************************
 **
 **************************************************************************************/

public class MergeFilter extends Thread {
	// Declarations

	// Create local pipes to that will connect to upstream filters
	PipedReader InputPipe1 = new PipedReader();
	PipedReader InputPipe2 = new PipedReader();

	// Create local pipes to that will connect to downstream filter
	PipedWriter OutputPipe1 = new PipedWriter();

	public MergeFilter(PipedWriter InputPipe1, PipedWriter InputPipe2,
			PipedWriter OutputPipe1) {
		try {
			// Connect InputPipes to upstream filters
			this.InputPipe1.connect(InputPipe1);
			this.InputPipe2.connect(InputPipe2);
			System.out.println("MergeFilter:: connected to upstream filters.");
		} catch (Exception Error) {
			System.out.println("MergeFilter:: Error connecting input pipes.");
		} // try/catch

		try {
			// Connect outputPipe1 to downstream filter
			this.OutputPipe1 = OutputPipe1;
			System.out.println("MergeFilter:: connected to downstream filter.");
		} catch (Exception Error) {
			System.out.println("MergeFilter:: Error connecting output pipe.");
		} // catch

	} // Constructor

	// This is the method that is called when the thread is started in
	// Main

	public void run() {
		// Declarations
		boolean Done1, Done2; // Flags for reading from each pipe

		// Begin process data from the input pipes
		try {
			// Declarations

			// Need to be an array for easy conversion to string
			char[] CharacterValue1 = new char[1];
			char[] CharacterValue2 = new char[1];

			// Indicate when you are done reading on pipes #1 and #2
			Done1 = false;
			Done2 = false;

			// integer values read from the pipes
			int IntegerCharacter1;
			int IntegerCharacter2;

			// lines of text from input pipes #1 and #2
			String LineOfText1 = "";
			String LineOfText2 = "";

			// Indicate whether lines of text are ready to be output
			// to downstream filters
			boolean Write1 = false;
			boolean Write2 = false;

			// Main loop for reading data

			while (!Done1 || !Done2) {
				// Read pipe #1
				if (!Done1) {
					IntegerCharacter1 = InputPipe1.read();
					CharacterValue1[0] = (char) IntegerCharacter1;

					if (IntegerCharacter1 == -1) // pipe #1 is closed
					{
						Done1 = true;
						System.out
								.println("MergeFilter:: Input pipe 1 closed.");

						try {
							InputPipe1.close();
						} catch (Exception Error) {
							System.out
									.println("MergeFilter:: Error closing input pipe 1.");
						} // try/catch

					} else {

						if (IntegerCharacter1 == '\n') // end of line
						{
							System.out.println("MergeFilter:: Received: "
									+ LineOfText1 + " on input pipe 1.");
							Write1 = true;
						} else {
							LineOfText1 += new String(CharacterValue1);
						} // if

					} // if ( IntegerCharacter1 == -1 )

				} // if (!Done1)

				// Read pipe #2
				if (!Done2) {
					IntegerCharacter2 = InputPipe2.read();
					CharacterValue2[0] = (char) IntegerCharacter2;

					if (IntegerCharacter2 == -1) // pipe #2 is closed
					{
						Done2 = true;
						System.out
								.println("MergeFilter:: input pipe 2 closed.");

						try {
							InputPipe2.close();
						} catch (Exception Error) {
							System.out
									.println("MergeFilter:: Error closing input pipe 2.");
						} // try/catch
					} else {
						if (IntegerCharacter2 == '\n') // end of line
						{
							System.out.println("MergeFilter:: Received: "
									+ LineOfText2 + " on input pipe 2.");
							Write2 = true;
						} else {
							LineOfText2 += new String(CharacterValue2);
						} // if

					} // if ( IntegerCharacter2 == -1 )

				} // if (!Done2)

				if (Write1) {
					Write1 = false;

					try {
						System.out.println("MergeFilter:: Sending "
								+ LineOfText1 + " to output pipe.");
						LineOfText1 += "\n";
						OutputPipe1.write(LineOfText1, 0, LineOfText1.length());
						OutputPipe1.flush();
					} catch (Exception IOError) {
						System.out.println("MergeFilter:: Write Error.");
					} // try/catch

					LineOfText1 = "";

				} // if ( Write1 )

				if (Write2) {
					Write2 = false;

					try {
						System.out.println("MergeFilter:: Sending "
								+ LineOfText2 + " to output pipe.");
						LineOfText2 += "\n";
						OutputPipe1.write(LineOfText2, 0, LineOfText2.length());
						OutputPipe1.flush();
					} catch (Exception IOError) {
						System.out.println("MergeFilter:: Write Error.");
					} // try/catch

					LineOfText2 = "";

				} // if (Write2)

			} // while ( !Done1 || !Done2 )

		} // try

		catch (Exception Error) {
			System.out.println("MergeFilter:: Interrupted.");
		} // catch

		try {
			System.out.println("MergeFilter:: output pipe closed.");
			OutputPipe1.close();
		} catch (Exception Error) {
			System.out.println("MergeFilter:: Error closing pipes");
		} // try/catch

	} // run

} // class