package core;

import java.io.File;

public class NCBIgenomeBatchDownloadFastaEntry
{
	/*
	 * Note: the accessionListFile is obtained from
	 * http://www.ncbi.nlm.nih.gov/genome/viruses/
	 * More specifically, it is the file
	 * http://www.ncbi.nlm.nih.gov/genomes/GenomesGroup.cgi?taxid=10239&cmd=download2
	 * taxid10239.nbr
	 */
	
	
	public static void main(String[] args) throws Exception
	{

		if (args == null || args.length < 2)
		{
			PrintUsage.print();
			System.exit(0);
		}

		String accessionListFile = args[0];
		String outputDirectory = args[1];

		int batchAmount = 900;

		if (args.length == 3)
		{
			batchAmount = Integer.parseInt(args[2]);
		}
		
		if(batchAmount > 900)
		{
			batchAmount = 900;
		}

		File tempFile = new File(accessionListFile);
		if (!(tempFile.exists() && tempFile.isFile()))
		{
			PrintUsage.print();
			System.exit(0);
		}
		tempFile = new File(outputDirectory);
		if (!(tempFile.exists() && tempFile.isDirectory()))
		{
			PrintUsage.print();
			System.exit(0);
		}
		
		File repDirectory = new File(outputDirectory + File.separator + "Representatives");
		if (!repDirectory.exists()) {
			repDirectory.mkdir();
		}
		
		File neighDirectory = new File(outputDirectory + File.separator + "Neighbors");
		if (!neighDirectory.exists()) {
			neighDirectory.mkdir();
		}
		
		File currentDirectory = new File(outputDirectory + File.separator + "Representatives");
		RedownloadLastFile.redownloadLastFile(currentDirectory);
		currentDirectory = new File(outputDirectory + File.separator + "Neighbors");
		RedownloadLastFile.redownloadLastFile(currentDirectory);
		BatchDownloadAsSeparateFASTAfiles.batchDownload(accessionListFile, outputDirectory, batchAmount);
		
	}
}