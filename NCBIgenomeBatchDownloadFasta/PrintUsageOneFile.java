package core;

public class PrintUsage {
	public static void print()
	{
		System.err.println("Usage: java -jar NCBIgenomeBatchDownloadFasta.java accessionListFile outputDirectory [batchSize]\n"
				+ "==================\n"
				+ "accessionListFile: file path to a file containing all accession numbers to download.\n"
				+ "outputDirectory  : file path to a folder that will contain the to-be downloaded fasta files.\n"
				+ "filenamePrefix   : desired prefix for results file. Examples include organism name, taxid, etc.\n"
				+ "[batchSize]      : must be an integer between 1 and 900.\n"
				+ "==================\n"
				+ "Author: Camille Menendez, Yazhou Sun, last update 2016-03-23\n");
		
	}
}
