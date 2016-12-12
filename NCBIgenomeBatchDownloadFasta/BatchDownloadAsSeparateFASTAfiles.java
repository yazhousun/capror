package core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BatchDownloadAsSeparateFASTAfiles {
	/*
	 * Note: the accessionListFile is obtained from
	 * http://www.ncbi.nlm.nih.gov/genome/viruses/ More specifically, it is the
	 * file
	 * http://www.ncbi.nlm.nih.gov/genomes/GenomesGroup.cgi?taxid=10239&cmd=
	 * download2 taxid10239.nbr
	 */

	public static void batchDownload(String accessionListFile, String outputDirectory, int batchAmount)
			throws Exception {
		SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
		BatchUpdateRepresentativesFolder.updateFiles(accessionListFile, outputDirectory, batchAmount);
		BatchUpdateNeighborsFolder.updateFiles(accessionListFile, outputDirectory, batchAmount);
		
		Date overallCompletionTimePoint = new Date();
		System.out.println("Genome collection completed at " + currentTime.format(overallCompletionTimePoint)
				+ ". Results stored in " + outputDirectory);
	}
}
