package ca.etsmtl.log430.lab1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.File;

/**************************************************************************
 ** Class name: FileWriterFilter
 ** Author: R. Champagne
 **        (heavily modified version of MergeFilter, by A.J. Lattanze)
 ** Date: 2001-Feb-06, 2011-Jan-12
 ** Version 1.1
 ***************************************************************************
 ** Purpose: Assignment 1 for LOG430, Architecture logicielle. This
 ** assignment is designed to illustrate a pipe and filter architecture.
 ** For the instructions, refer to the assignment write-up.
 **
 ** Abstract: This class is intended to be a filter that will collect the
 **           input from its input pipe and write it to an output file.
 **
 ** Pseudo Code:
 **
 ** connect to upstream filter for input
 ** Open output file
 **	while not done
 **		read char from input pipe
 **		string = string + char
 **		write string to string array 
 **	end while
 **	close pipes
 **	write string array to file
 **
 ** Running the program
 **
 ** 	See Main.java
 **
 ** Modification Log
 ***************************************************************************
 **
 **************************************************************************/

public class FileWriterFilter extends Thread {
	// Declarations

	String OutputFileName;

	// Maximum number of lines of text to be sorted
	int MaxBufferSize = 100;

	// Create local pipe that will connect to upstream filter
	PipedReader InputPipe1 = new PipedReader();

	public FileWriterFilter(String OutputFileName, PipedWriter InputPipe1) {
		this.OutputFileName = OutputFileName;

		try {
			// Connect inputPipe to upstream filter
			this.InputPipe1.connect(InputPipe1);
			System.out.println("FileWriterFilter:: connected to upstream filter.");
		} catch (Exception Error) {
			System.out.println("FileWriterFilter:: Error connecting input pipe.");
		} // catch

	} // Constructor

	// This is the method that is called when the thread is started in
	// Main

	public void run() {
		// Declarations

		boolean Done1; // Flags for reading from pipe
		String Directory; // Part of output file name
		File FileObject; // Output file object
		File DirectoryObject; // Output directory object
		BufferedWriter bout = null; // Output file buffer writer object

		// Open and/or create the output file

		FileObject = new File(OutputFileName);
		OutputFileName = FileObject.getName();

		Directory = FileObject.getAbsolutePath();
		Directory = Directory.substring(0,
				(Directory.length() - OutputFileName.length()));
		DirectoryObject = new File(Directory);

		// Create directory if it doesn't exist
		if (!DirectoryObject.exists()) {
			try {
				DirectoryObject.mkdirs();
				System.out.println("FileWriterFilter:: Created directory: "
						+ Directory + ".");
			} catch (SecurityException Error) {
				System.out.println("FileWriterFilter:: Unable to create directory: "
						+ Directory + ".");
			} // try/catch
		} // if

		// Indicate that file exists and will be overwritten
		if (FileObject.exists()) {
			System.out.println("FileWriterFilter:: overwriting file "
					+ FileObject.getAbsolutePath() + ".");
		}

		// Create a buffered writer
		try {
			bout = new BufferedWriter(new FileWriter(FileObject));
		} catch (IOException IOError) {
			System.out.println("FileWriterFilter:: Buffered Writer Creation Error.");
		} // try/catch

		// Create a temporary String array of a big size (for sorting)
		String[] tmpArray = new String[MaxBufferSize];
		int count = 0;
		int i;

		// Begin process data from the input pipes
		try {
			// Declarations

			// Needs to be an array for easy conversion to string
			char[] CharacterValue1 = new char[1];

			Done1 = false; // Indicates when you are done
			// reading on pipe #1
			int IntegerCharacter1; // integer value read from pipe
			String LineOfText1 = ""; // line of text from inputpipe #1
			boolean Write1 = false; // line of text to write to output
			// pipe #1

			// Main loop for reading data

			while (!Done1) {
				// Read pipe #1
				if (!Done1) {
					IntegerCharacter1 = InputPipe1.read();
					CharacterValue1[0] = (char) IntegerCharacter1;

					if (IntegerCharacter1 == -1) // pipe #1 is closed
					{
						Done1 = true;
						System.out.println("FileWriterFilter:: input pipe closed.");
						try {
							InputPipe1.close();
						} catch (Exception Error) {
							System.out
									.println("FileWriterFilter:: Error closing input pipe.");
						} // try/catch
					} else {
						if (IntegerCharacter1 == '\n') // end of line
						{
							System.out.println("FileWriterFilter:: Received: "
									+ LineOfText1 + " on input pipe.");
							Write1 = true;
						} else {
							LineOfText1 += new String(CharacterValue1);
						} // if
					} // if ( IntegerCharacter1 == -1 )
				} // if (!Done1)

				if (Write1) {
					// Add LineOfText1 to temporary string array,
					// increment arrayindex and reset Write1 to false.
					Write1 = false;
					tmpArray[count] = LineOfText1;
					++count;
					LineOfText1 = "";
				} // if

			} // while (!Done1)

		} // try

		catch (Exception Error) {
			System.out.println("FileWriterFilter:: Interrupted.");
		} // catch

		// At this point, we have all lines of text in tmpArray.

		// Write the string array to output file
		try {
			for (i = 0; i < count; i++) {
				System.out.println("FileWriterFilter:: Writing: " + tmpArray[i]);
				bout.write(tmpArray[i]);

				// Add a platform independent newline
				bout.newLine();
			} // for
		} catch (Exception IOError) {
			System.out.println("FileWriterFilter:: Write Error.");
		} // try/catch

		// Close the output file
		try {
			System.out.println("FileWriterFilter:: Closing output file "
					+ FileObject.getAbsolutePath() + ".");
			bout.close();
		} catch (Exception Error) {
			System.out.println("FileWriterFilter:: Error closing output file.");
		} // try/catch

	} // run

} // class
