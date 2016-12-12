package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner; 

public class NCBIgenomeBatchDownloadFastaEntry {

	public static void main(String[] args) throws Exception {

		if (args == null || args.length < 3) {
			PrintUsage.print();
			System.exit(0);
		}

		String accessionListFile = args[0];
		String outputDirectory = args[1];
		String filenamePrefix = args[2];

		int batchAmount = 10;

		if (args.length == 4) {
			batchAmount = Integer.parseInt(args[3]);
		}

		File tempFile = new File(accessionListFile);
		if (!(tempFile.exists() && tempFile.isFile())) {
			PrintUsage.print();
			System.exit(0);
		}
		tempFile = new File(outputDirectory);
		if (!(tempFile.exists() && tempFile.isDirectory())) {
			PrintUsage.print();
			System.exit(0);
		}

		Date date = new Date();
		SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
		String tempInput = null;
		String begBaseURL = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id=";
		String currentURL = null;
		String endBaseURL = "&rettype=fasta";
		int batchNum = 1;
		String resultsFilename = null;

		tempFile = new File(
				outputDirectory + File.separator + filenamePrefix + ".NCBIBD." + currentDate.format(date).toString() + ".fasta");
		if (!(tempFile.exists())) {
			resultsFilename = tempFile.toString();
		} else {
			int fileCount = 1;
			while (tempFile.exists()) {
				tempFile = new File(outputDirectory + File.separator + filenamePrefix + ".NCBIBD." + currentDate.format(date).toString()
						+ "." + fileCount + ".fasta");
				if (tempFile.exists()) {
					fileCount++;
				}
			}
			resultsFilename = tempFile.toString();
		}
		
		// open file of accession numbers
		Scanner accessionNumberInput = getInput(accessionListFile);
		System.out.println("Currently collecting genome data from accession numbers in " + accessionListFile);

		// open PrintWriter
		try {

			PrintWriter viralGenomesOutput = new PrintWriter(resultsFilename);

			// iterate through test file and conduct efetch requests
			while (accessionNumberInput.hasNext()) {

				currentURL = begBaseURL;

				// only place requests for batches - 900 is the highest tested
				for (int i = 0; i < batchAmount; i++) {

					if (accessionNumberInput.hasNext()) {
						if (i > 0) {
							currentURL += ",";
						}

						tempInput = accessionNumberInput.nextLine().trim();
						currentURL += tempInput;
					}
				}

				currentURL += endBaseURL;

				// efetch requests
				URL website = new URL(currentURL);
				Scanner websiteInput = new Scanner(website.openStream());
				String tempOutput = null;

				// write sequences to file
				while (websiteInput.hasNext()) {
					tempOutput = websiteInput.nextLine().trim();
					viralGenomesOutput.println(tempOutput);
				}

				// System.out.println(System.currentTimeMillis());
				Date time1 = new Date();
				System.out.println("Batch " + batchNum + " completed at " + currentTime.format(time1));
				Thread.sleep(2000);
				// System.out.println(System.currentTimeMillis());

				websiteInput.close();
				batchNum++;
			}
			viralGenomesOutput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Date time2 = new Date();
		System.out.println("Genome collection completed at " + currentTime.format(time2) + ". Results stored in "
				+ resultsFilename);

	}

	public static Scanner getInput(String inputFileName) {
		Scanner input = null;

		try {
			input = new Scanner(new File(inputFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		return (input);
	}

}
