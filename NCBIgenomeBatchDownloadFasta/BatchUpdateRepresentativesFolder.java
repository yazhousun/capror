package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class BatchUpdateRepresentativesFolder {

	private static int URLlengthRestriction = 1999;

	public static void updateFiles(String accessionListFile, String outputDirectory, int batchAmount) throws Exception {
		File tempFile = null;

		SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
		String begBaseURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id=";
		String currentURL = null;
		String endBaseURL = "&rettype=fasta";
		int batchNum = 1;
		String representativesGenomeFilePath = null;

		// open file of accession numbers
		Scanner accessionNumberInput = IoHelper.getTextInput(accessionListFile);
		System.out.println(
				"Currently updating representative genome data from accession numbers in " + accessionListFile);

		URL website = null;
		Scanner websiteInput = null;
		String tempOutput = null;
		Date currentTimePoint = null;
		String temp = null;
		String fullAccession = null;
		String[] tempArray = null;
		String[] tempArrayInitial = null;
		String[] tempArraySecond = null;

		HashSet<String> listOfRepresentatives = new HashSet<String>();

		while (accessionNumberInput.hasNext()) {
			temp = accessionNumberInput.nextLine().trim();
			if (temp.startsWith("#") || temp.length() < 1)
				continue;

			tempArrayInitial = temp.split("\\t");
			tempArraySecond = tempArrayInitial[0].split(",");
			for (int i = 0; i < tempArraySecond.length; i++) {
				listOfRepresentatives.add(tempArraySecond[i]);
			}

		}

		Iterator<String> it = listOfRepresentatives.iterator();

		File currentDirectory = new File(outputDirectory + File.separator + "Representatives");
		HashSet<String> currentRepFilenames = new HashSet<String>();

		String[] fileNames = currentDirectory.list();
		String[] tempStringArray = null;
		String accessionNum = null;
		String versionNum = null;
		int numInFile = 0;
		int numUpdated = 0;

		for (int i = 0; i < fileNames.length; i++) {
			if (!fileNames[i].endsWith(".fasta"))
				continue;

			tempStringArray = fileNames[i].split("\\.");
			accessionNum = tempStringArray[0];
			versionNum = tempStringArray[1];

			if (!(currentRepFilenames.contains(accessionNum + "." + versionNum))) {
				currentRepFilenames.add(accessionNum + "." + versionNum);
			}
		}

		while (it.hasNext()) {

			currentURL = begBaseURL;
			// only place requests for batches - 900 is the highest tested
			for (int i = 0; i < batchAmount; i++) {
				if (it.hasNext()) {
					if (i > 0) {
						currentURL += ",";
					}

					temp = it.next();
					currentURL += temp;

					if (currentURL.length() > URLlengthRestriction) {
						break;
					}
				}
			}
			if (currentURL.charAt(currentURL.length() - 1) == ',') {
				currentURL = currentURL.substring(0, currentURL.length() - 1);
			}

			currentURL += endBaseURL;

			// efetch requests
			website = new URL(currentURL);
			websiteInput = new Scanner(website.openStream());

			try {

				PrintWriter representativesGenomeOutput = null;

				// write sequences to file
				tempOutput = websiteInput.nextLine().trim();
				while (websiteInput.hasNext()) {

					if (tempOutput.startsWith(">")) {

						numInFile++;

						tempArray = tempOutput.split("\\s");
						fullAccession = tempArray[0].substring(1, tempArray[0].length());

						if (!(currentRepFilenames.contains(fullAccession))) {
							tempFile = new File(outputDirectory + File.separator + "Representatives" + File.separator
									+ fullAccession + "." + System.currentTimeMillis() + ".fasta");
							representativesGenomeFilePath = tempFile.getAbsolutePath();
							representativesGenomeOutput = new PrintWriter(representativesGenomeFilePath);
							representativesGenomeOutput.println(tempOutput);
							numUpdated++;

							tempOutput = websiteInput.nextLine().trim();

							while ((!tempOutput.startsWith(">")) && websiteInput.hasNext()) {
								representativesGenomeOutput.println(tempOutput);
								tempOutput = websiteInput.nextLine().trim();
							}

							if (!websiteInput.hasNext()) {
								representativesGenomeOutput.println(tempOutput);
							}

							representativesGenomeOutput.close();
						} else {
							tempOutput = websiteInput.nextLine().trim();

							while ((!tempOutput.startsWith(">")) && websiteInput.hasNext()) {
								tempOutput = websiteInput.nextLine().trim();
							}
						}
					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			currentTimePoint = new Date();
			System.out.println(
					"Representatives batch " + batchNum + " completed at " + currentTime.format(currentTimePoint));
			Thread.sleep(2000);

			websiteInput.close();
			batchNum++;
		}

		accessionNumberInput.close();
		System.out.println("Total of " + numInFile + " representative genomes scanned: " +  numUpdated + " updated.");
	};
}
