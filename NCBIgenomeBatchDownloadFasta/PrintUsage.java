package core;

public class PrintUsage {
	public static void print()
	{
		SimpleFormatPrinter sp = new SimpleFormatPrinter();
		
		sp.println("Usage: java -jar NCBIgenomeBatchDownloadFasta.java accessionListFile outputDirectory <batchSize>");
		sp.println();
		sp.println("Available commands:");
		sp.setFirstColumnWidth(20);
		sp.printfln("accessionListFile", "file path to a file containing NCBI accession numbers to be downloaded as FASTA formatted files."); 
		sp.printfln("", "Note: First column contains representative genomes and second column contains neighbor genomes.");
		sp.println();
		sp.printfln("outputDirectory", "file path to a folder that will contain the downloaded FASTA files.");
		sp.println();
		sp.printfln("batchSize", "[Optional] To indicate how many records to download in one batch. This must be an integer between 1 and 900 [default is 900].");
		sp.println();
		sp.println("Author: Camille Menendez, Yazhou Sun, last update 2016-12-07");
		System.exit(0);
	}
}
