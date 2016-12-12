package core;

public class PrintUsage {
	public static void print()
	{
		System.err.println("Usage: java -jar ExtractGenomeByName.java genomeListFile groupSeqList outputDirectory\n"
				+ "==================\n"
				+ "genomeListFile   : file path to a file containing the genome sequences.\n"
				+ "groupSeqList     : file path to a file containing the names of the sequences to be studied.\n"
				+ "outputDirectory  : file path to a folder that will contain the kmer results files.\n"
				+ "==================\n"
				+ "Author: Camille Menendez, Yazhou Sun, last update 2016-03-23\n");
		
	}
}
