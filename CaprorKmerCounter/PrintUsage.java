package core;

public class PrintUsage {
	public static void print()
	{
		SimpleFormatPrinter sp = new SimpleFormatPrinter();
		
		sp.println("Usage: java -jar CaprorKmerCounter.java genomeListDirectory mostRecentVersionsFile outputDirectory kmerLength outputFileNamePrefix");
		sp.println();
		sp.println("Available commands:");
		sp.setFirstColumnWidth(20);
		sp.printfln("genomeListDirectory", "file path to a directory containing all genomes to study in individual files"); 
		sp.printfln("", "Example: Representatives or Neighbors directory");
		sp.println();
		sp.printfln("mostRecentVersionsFile", "file path to a file that contains a list of the most recent versions of each genome.");
		sp.println();
		sp.printfln("outputDirectory", "file path to a folder that will contain the kmer results files");
		sp.println();
		sp.printfln("kmerLength", "must be an integer between 1 and 15");
		sp.println();
		sp.printfln("outputFileNamePrefix", "custom prefix for the output file");
		sp.printfln("", "Example: Representatives or Neighbors");
		sp.println();
		sp.println("Author: Camille Menendez, Yazhou Sun, last update 2016-12-06");
		System.exit(0);
	}
}
