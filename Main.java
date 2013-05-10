package ca.etsmtl.log430.lab1;

import java.io.PipedWriter;

/**************************************************************************************
 ** Class name: Main Original author: A.J. Lattanze, CMU Date: 12/3/99
 ** Version 1.2
 ** 
 ** Adapted by R. Champagne, Ecole de technologie superieure 2002-May-08,
 ** 2011-Jan-12, 2012-Jan-11.
 ** 
 *************************************************************************************** 
 ** Purpose: Assignment 1 for LOG430, Architecture logicielle. This assignment is
 ** designed to illustrate a pipe and filter architecture. For the instructions,
 ** refer to the assignment write-up.
 ** 
 ** Abstract: This class contains the main method for assignment 1. The
 ** assignment 1 program consists of these files:
 ** 
 ** 1) Main: instantiates all filters and pipes, starts all filters.
 ** 2) FileReaderFilter: reads input file and sends each line to its output pipe.
 ** 3) TypeFilter: separates the input stream in two languages (FRA, EN) and writes
 **    lines to the appropriate output pipe.
 ** 4) SeverityFilter: determines if an entry contains a particular severity specified
 **    at instantiation. If so, sends the whole line to its output pipe.
 ** 5) MergeFilter: accepts inputs from 2 input pipes and writes them to its output pipe.
 ** 6) FileWriterFilter: sends its input stream to a text file.
 ** 
 ** Pseudo Code:
 ** 
 ** instantiate all filters and pipes
 ** start FileReaderFilter
 ** start TypeFilter
 ** start SeverityFilter for CRI
 ** start SeverityFilter for MAJ
 ** start MergeFilter
 ** start FileWriterFilter
 ** 
 ** Running the program
 ** 
 ** java Main IputFile OutputFile > DebugFile
 ** 
 ** Main - Program name
 ** InputFile - Text input file (see comments below)
 ** OutputFile - Text output file with students
 ** DebugFile - Optional file to direct debug statements
 ** 
 ** Modification Log
 ************************************************************************************** 
 ** 
 **************************************************************************************/

public class Main {

	public static void main(String argv[]) {
		// Lets make sure that input and output files are provided on the
		// command line

		if (argv.length != 3) {

			System.out
					.println("\n\nNombre incorrect de parametres d'entree. Utilisation:");
			System.out
					.println("\njava Main <fichier d'entree> <fichier de sortie>");

		} else {
			// These are the declarations for the pipes, for the first file.
			PipedWriter pipe01 = new PipedWriter();
			PipedWriter pipe02 = new PipedWriter();
			PipedWriter pipe03 = new PipedWriter();
			PipedWriter pipe04 = new PipedWriter();
			PipedWriter pipe05 = new PipedWriter();
			PipedWriter pipe06 = new PipedWriter();
			PipedWriter pipe07 = new PipedWriter();
			PipedWriter pipe08 = new PipedWriter();
			
			
			// These are the declarations for the pipes, for the second file.


			// Instantiate the Program Filter Thread
			Thread FileReaderFilter1 = new FileReaderFilter(argv[0], pipe01);

			// Instantiate the TypeFilter Thread
			Thread LanguageFilter1 = new TypeFilter(pipe01, pipe02, pipe03);

			// Instantiate the Course Filter Threads
			Thread KeywordFilter1 = new SeverityFilter("CRI", pipe02, pipe04);
			Thread KeywordFilter2 = new SeverityFilter("MAJ", pipe03, pipe05);

			// Instantiate the Merge Filter Thread
			Thread MergeFilter1 = new MergeFilter(pipe04, pipe05, pipe06);
			
			// Initiate the Custom Filter Thread
			Thread CustomFilter1 = new CustomFilter(pipe06, pipe07);
			
			// Initiate the Sort Filter Thread
			Thread SortFilter1 = new SortFiler(pipe07, pipe06);

			// Instantiate the FileWriter Filter Thread
			Thread FileWriterFilter1 = new FileWriterFilter(argv[1], pipe08);

			// Start the threads (these are the filters)
			FileReaderFilter1.start();
			LanguageFilter1.start();
			KeywordFilter1.start();
			KeywordFilter2.start();
			MergeFilter1.start();
			FileWriterFilter1.start();
			
		}  // if
		
	} // main
	
} // Main