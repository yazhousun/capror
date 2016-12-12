package core;

public class PrintUsage {
	public static void print()
	{
		SimpleFormatPrinter sp = new SimpleFormatPrinter();
		
		sp.println("Usage: java -jar MostRecentGenomeVersion.java genomeListDirectory outputDirectory outputFileNamePrefix");
		sp.println();
		sp.println("Available commands:");
		sp.setFirstColumnWidth(20);
		sp.printfln("genomeListDirectory", "file path to a directory containing all genomes to study in individual files"); 
		sp.printfln("", "Example: Representatives or Neighbors directory");
		sp.println();
		sp.printfln("outputDirectory", "file path to a folder that will contain the file listing most recent versions.");
		sp.println();
		sp.printfln("outputFileNamePrefix", "custom prefix for the output file");
		sp.printfln("", "Example: Representatives or Neighbors");
		sp.println();
		sp.println("Author: Camille Menendez, Yazhou Sun, last update 2016-12-06");
		System.exit(0);
	}
}
