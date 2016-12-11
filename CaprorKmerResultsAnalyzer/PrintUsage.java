package core;

public class PrintUsage {
	public static void print()
	{
		SimpleFormatPrinter sp = new SimpleFormatPrinter();
		
		sp.println("Usage: java -jar CaprorKmerResultsAnalyzer.java kmerListFile outputDirectory kmerLength");
		sp.println();
		sp.println("Available commands:");
		sp.setFirstColumnWidth(20);
		sp.printfln("kmerListFile", "file path to a file containing all kmers to study."); 
		sp.println();
		sp.printfln("outputDirectory", "file path to a folder that will contain the kmer results files.");
		sp.println();
		sp.printfln("kmerLength", "must be an integer between 1 and 15 that matches the kmerListFile kmer value.");
		sp.println();
		sp.println("Author: Camille Menendez, Yazhou Sun, last update 2016-12-07");
		System.exit(0);
	}
}
