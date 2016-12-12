package core;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;

public class MostRecentGenomeVersionEntry {

	public static void main(String[] args) 
	{
		if (args == null || args.length < 3)
		{
			PrintUsage.print();
			System.exit(0);
		}

		String genomeListDirectory = args[0];
		String outputDirectory = args[1];
		String outputFileNamePrefix = args[2];
		String filename = null;

		int lastSeparatorIndex = outputFileNamePrefix.lastIndexOf(File.separator);
		if (lastSeparatorIndex == -1)
		{
			filename = outputFileNamePrefix;
		} else
		{
			filename = outputFileNamePrefix.substring(lastSeparatorIndex + 1);
		}

		int extensionIndex = filename.lastIndexOf('.');
		if (extensionIndex == filename.length()-1){
			outputFileNamePrefix = filename;
		}
		else {
			outputFileNamePrefix = filename + ".";
		}
		
		File tempFile = new File(genomeListDirectory);
		if (!(tempFile.exists() && tempFile.isDirectory())) {
			PrintUsage.print();
			System.exit(0);
		}

		tempFile = new File(outputDirectory);
		if (!(tempFile.exists() && tempFile.isDirectory())) {
			PrintUsage.print();
			System.exit(0);
		}

		File baseDir = new File(genomeListDirectory);
		String[] files = baseDir.list();
		HashMap<String, Integer> accessionnumbers = new HashMap<String, Integer>();
		HashMap<String, String> fileNames = new HashMap<String, String>();
		String[] tempStringArray = null;
		String accessionNum = null;
		int versionNum;

		for (int i = 0; i < files.length; i++) {
			
			if (!files[i].endsWith(".fasta"))
				continue;

			tempStringArray = files[i].split("\\.");
			accessionNum = tempStringArray[0];
			versionNum = Integer.parseInt(tempStringArray[1]);

			if (!(accessionnumbers.containsKey(accessionNum))) {
				accessionnumbers.put(accessionNum, versionNum);
				fileNames.put(accessionNum, files[i]);
			}
			if (versionNum > accessionnumbers.get(accessionNum)){
				accessionnumbers.put(accessionNum, versionNum);
				fileNames.put(accessionNum, files[i]);
			}
			
		}
		PrintWriter output = IoHelper.getTextOutput(outputDirectory + File.separator + outputFileNamePrefix + System.currentTimeMillis() + ".currentversions.txt");
		for (String accessionNumber: accessionnumbers.keySet()){
			String versionNumber = accessionnumbers.get(accessionNumber).toString();
			output.println(accessionNumber + "\t" + versionNumber + "\t" + fileNames.get(accessionNumber));
		}
		output.close();
		
		System.out.println("The most recent versions of " + accessionnumbers.size() + " genomes are ready to be studied.");
	}
}
