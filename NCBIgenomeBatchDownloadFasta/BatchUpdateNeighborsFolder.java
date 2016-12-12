package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Iterator;

public class BatchUpdateNeighborsFolder {

	private static int URLlengthRestriction = 1999;

	public static void updateFiles(String accessionListFile, String outputDirectory, int batchAmount) throws Exception {
		File tempFile = null;

		SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
		String begBaseURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id=";
		String currentURL = null;
		String endBaseURL = "&rettype=fasta";
		int batchNum = 1;
		String neighborsGenomeFilePath = null;

		// open file of accession numbers
		Scanner accessionNumberInput = IoHelper.getTextInput(accessionListFile);
		System.out.println("Currently updating neighbor genome data from accession numbers in " + accessionListFile);

		URL website = null;
		Scanner websiteInput = null;
		String tempOutput = null;
		Date currentTimePoint = null;
		String temp = null;
		String fullAccession = null;
		String[] tempArray = null;
		String[] tempArrayInitial = null;
		String[] tempArraySecond = null;

		HashSet<String> listOfNeighbors = new HashSet<String>();

		while (accessionNumberInput.hasNext()) {
			temp = accessionNumberInput.nextLine().trim();
			if (temp.startsWith("#") || temp.length() < 1)
				continue;

			tempArrayInitial = temp.split("\\t");
			if (tempArrayInitial.length > 1) {
				tempArraySecond = tempArrayInitial[1].split(",");
				for (int i = 0; i < tempArraySecond.length; i++) {
					listOfNeighbors.add(tempArraySecond[i]);
				}
			}
		}

		Iterator<String> it = listOfNeighbors.iterator();

		File currentDirectory = new File(outputDirectory + File.separator + "Neighbors");
		HashSet<String> currentNeighFilenames = new HashSet<String>();

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

			if (!(currentNeighFilenames.contains(accessionNum + "." + versionNum))) {
				currentNeighFilenames.add(accessionNum + "." + versionNum);
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

				PrintWriter neighborsGenomeOutput = null;

				// write sequences to file
				tempOutput = websiteInput.nextLine().trim();
				while (websiteInput.hasNext()) {

					if (tempOutput.startsWith(">")) {

						numInFile++;

						tempArray = tempOutput.split("\\s");
						fullAccession = tempArray[0].substring(1, tempArray[0].length());

						if (!(currentNeighFilenames.contains(fullAccession))) {
							tempFile = new File(outputDirectory + File.separator + "Neighbors" + File.separator
									+ fullAccession + "." + System.currentTimeMillis() + ".fasta");
							neighborsGenomeFilePath = tempFile.getAbsolutePath();
							neighborsGenomeOutput = new PrintWriter(neighborsGenomeFilePath);
							neighborsGenomeOutput.println(tempOutput);
							numUpdated++;

							tempOutput = websiteInput.nextLine().trim();

							while ((!tempOutput.startsWith(">")) && websiteInput.hasNext()) {
								neighborsGenomeOutput.println(tempOutput);
								tempOutput = websiteInput.nextLine().trim();
							}

							if (!websiteInput.hasNext()) {
								neighborsGenomeOutput.println(tempOutput);
							}

							neighborsGenomeOutput.close();
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
			System.out.println("Neighbors batch " + batchNum + " completed at " + currentTime.format(currentTimePoint));
			Thread.sleep(2000);

			websiteInput.close();
			batchNum++;
		}

		accessionNumberInput.close();
		System.out.println("Total of " + numInFile + " neighbor genomes genomes scanned: " +  numUpdated + " updated.");
	};
}
