package core;

public class PrintUsage {
	public static void print()
	{
		System.err.println("Usage: java -jar CaprorUniqueSequenceFinder.java genomeFile kmerListFile outputDirectory kmerLength lessThanValue\n"
				+ "==================\n"
				+ "genomeFile       : file path to a file containing a genome to study.\n"
				+ "kmerListFile     : file path to a file containing all kmers to study.\n"
				+ "outputDirectory  : file path to a folder that will contain the kmer results files.\n"
				+ "kmerLength       : must be an integer between 1 and 15.\n"
				+ "lessThanValue    : must be an integer.\n"
				+ "==================\n"
				+ "Author: Camille Menendez, Yazhou Sun, last update 2016-10-04\n");
		
	}
}
