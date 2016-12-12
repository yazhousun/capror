package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class RedownloadLastFile {

	public static void redownloadLastFile(File currentDirectory) {
		if (currentDirectory.list().length > 1) {
			String[] fileNames = currentDirectory.list();
			String[] tempStringArray = null;
			String accessionNum = null;
			String versionNum = null;
			String lastDownloadedAccession = null;
			String lastDownloadedVersion = null;
			long lastMilli = 0;
			long currentMilli = 0;
			String fullAccession = null;

			for (int i = 0; i < fileNames.length; i++) {
				if (!fileNames[i].endsWith(".fasta"))
					continue;

				tempStringArray = fileNames[i].split("\\.");
				accessionNum = tempStringArray[0];
				versionNum = tempStringArray[1];
				currentMilli = Long.parseLong(tempStringArray[2]);

				if (currentMilli > lastMilli) {
					lastMilli = currentMilli;
					lastDownloadedAccession = accessionNum;
					lastDownloadedVersion = versionNum;
				}

			}

			String begBaseURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id=";
			String currentURL = begBaseURL + lastDownloadedAccession + "&rettype=fasta";
			URL website = null;
			try {
				website = new URL(currentURL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			Scanner websiteInput = null;
			try {
				websiteInput = new Scanner(website.openStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				String redownloadFilepath = null;
				PrintWriter redownloadOutput = null;
				File tempFile = null;

				// write sequences to file
				String tempOutput = websiteInput.nextLine().trim();

				while (websiteInput.hasNext()) {

					if (tempOutput.startsWith(">")) {
						String[] tempArray = tempOutput.split("\\s");
						fullAccession = tempArray[0].substring(1, tempArray[0].length());

						tempFile = new File(currentDirectory + File.separator + fullAccession + "."
								+ System.currentTimeMillis() + ".fasta");
						redownloadFilepath = tempFile.getAbsolutePath();
						redownloadOutput = new PrintWriter(redownloadFilepath);
						redownloadOutput.println(tempOutput);

						tempOutput = websiteInput.nextLine().trim();

						while ((!tempOutput.startsWith(">")) && websiteInput.hasNext()) {
							redownloadOutput.println(tempOutput);
							tempOutput = websiteInput.nextLine().trim();
						}

						if (!websiteInput.hasNext()) {
							redownloadOutput.println(tempOutput);
						}

						redownloadOutput.close();
						System.out.println(redownloadFilepath + " was re-downloaded since it was the last file downloaded.");
					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			File lastDownloadedFile = new File(currentDirectory + File.separator + lastDownloadedAccession + "."
					+ lastDownloadedVersion + "." + lastMilli + ".fasta");
			lastDownloadedFile.delete();
		}
	}
}
