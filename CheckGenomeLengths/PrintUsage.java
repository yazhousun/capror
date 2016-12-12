package core;

public class PrintUsage {
	public static void print() {
		System.err.println(
				"Usage: java -jar CheckGenomeLengths.java accessionListDirectory outputDirectory [batchSize]\n"
						+ "==================\n"
						+ "genomeListDirectory	: file path to a directory containing the genome sequences.\n"
						+ "outputDirectory  	: file path to a folder that will contain the results file.\n"
						+ "==================\n"
						+ "Author: Camille Menendez, Yazhou Sun, last update 2016-06-30\n");

	}
}
