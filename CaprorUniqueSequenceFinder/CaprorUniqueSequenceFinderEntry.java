package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class CaprorUniqueSequenceFinderEntry {

	public static void main(String[] args) {
		
		if (args == null || args.length < 5) {
			PrintUsage.print();
			System.exit(0);
		}

		String genomeFile = args[0];
		String kmerListFile = args[1];
		String outputDirectory = args[2];
		int kmerLength = Integer.parseInt(args[3]);
		int lessThanValue = Integer.parseInt(args[4]);

		File tempFile = new File(genomeFile);
		if (!(tempFile.exists() && tempFile.isFile())) {
			PrintUsage.print();
			System.exit(0);
		}
		
		String outputFileNamePrefix = null;
		outputFileNamePrefix = tempFile.getName();
		outputFileNamePrefix = IoHelper.removeExtension(outputFileNamePrefix);
		String outputFileName = null;
		
		tempFile = new File(
				outputDirectory + File.separator + outputFileNamePrefix + 
				".uniqueKmerSequences." + kmerLength + "bps.lessThan" + lessThanValue + ".txt");
		if (!(tempFile.exists()))
		{
			outputFileName = tempFile.toString();
		} else
		{
			int fileCount = 1;
			while (tempFile.exists())
			{
				tempFile = new File(outputDirectory + File.separator + outputFileNamePrefix + 
						".uniqueKmerSequences." + kmerLength + "bps.lessThan" + lessThanValue + "." + fileCount + ".txt");
				if (tempFile.exists())
				{
					fileCount++;
				}
			}
			outputFileName = tempFile.toString();
		}
		
		tempFile = new File(kmerListFile);
		if (!(tempFile.exists() && tempFile.isFile())) {
			PrintUsage.print();
			System.exit(0);
		}
					
		tempFile = new File(outputDirectory);
		if (!(tempFile.exists() && tempFile.isDirectory())) {
			PrintUsage.print();
			System.exit(0);
		}
		
		char[] kmer = new char[kmerLength];
		String kmerString = null;
		int kmerBinaryValue = 0;
		char[] sequence = null;
			
		//collect sequence
		sequence = retrieveSequence(genomeFile);

		//load "all.kcl"
		Scanner kmerInput = IoHelper.getTextInput(kmerListFile);
		
		int startIndex = 0;
		HashSet<Integer> kmersInGenome = new HashSet<Integer>();
		HashSet<Integer> uniqueKmersInGenome = new HashSet<Integer>();
		
		while (startIndex <= sequence.length - kmerLength) {
			
			for (int i = 0; i < kmerLength; i++) {
				kmer[i] = sequence[startIndex];
				startIndex++;
			}
			startIndex -= (kmerLength - 1);

			kmerString = String.valueOf(kmer);
			kmerBinaryValue = DNAkmerIntIndexFastConverter.computeIntDNAseqIndex(kmerString);
			if (!(kmersInGenome.contains(kmerBinaryValue)) && (kmerBinaryValue > -1)){
				kmersInGenome.add(kmerBinaryValue);
			}
		}
		
		//also check reverse strand
		String seq = new String(sequence);
		seq = DNAreverseComplementUtils.simpleDNAreverseComplement(seq);
		sequence = seq.toCharArray();
		startIndex = 0;
		
		while (startIndex <= sequence.length - kmerLength) {
			
			for (int i = 0; i < kmerLength; i++) {
				kmer[i] = sequence[startIndex];
				startIndex++;
			}
			startIndex -= (kmerLength - 1);

			kmerString = String.valueOf(kmer);
			kmerBinaryValue = DNAkmerIntIndexFastConverter.computeIntDNAseqIndex(kmerString);
			if (!(kmersInGenome.contains(kmerBinaryValue)) && (kmerBinaryValue > -1)){
				kmersInGenome.add(kmerBinaryValue);
			}
		}
		
		String temp = null;
		int intValueOfInput = 0;
		int lineIndexNumber = 0;
		while (kmerInput.hasNext()){
			temp = kmerInput.nextLine().trim();
			if (kmersInGenome.contains(lineIndexNumber)){
				intValueOfInput = Integer.parseInt(temp);
				if (intValueOfInput < lessThanValue){
					uniqueKmersInGenome.add(lineIndexNumber);
				}
			}	
			lineIndexNumber++;
			
			if(lineIndexNumber % 10000 == 0) System.out.println(lineIndexNumber);
		} 
		
		outputUniqueKmersAsFile(outputFileName, uniqueKmersInGenome);
		
	}
	
	public static void outputUniqueKmersAsFile (String outputFileName, HashSet<Integer> uniqueKmersInGenome){
		PrintWriter output = IoHelper.getTextOutput(outputFileName);
		
		Iterator<Integer> it = uniqueKmersInGenome.iterator();
		int tempInt = 0;
		
		while(it.hasNext())
		{
			tempInt = it.next();
			output.println(tempInt);
		}
		
		output.close();
	}
	
	public static char[] retrieveSequence(String filename) {
		Scanner input = getInput(filename);

		String temp = null;
		@SuppressWarnings("unused")
		String[] tempArray = null;
		StringBuilder seq = null;
		String whiteSpaces = new String("[ \\t\\n\\x0B\\f\\r]{1,}");

		temp = input.nextLine().trim();
		while (input.hasNext()) {
			if (temp.startsWith(">")) {
				tempArray = temp.split(whiteSpaces);
				seq = new StringBuilder();

				temp = input.nextLine().trim();
				while ((!temp.startsWith(">")) && input.hasNext()) {
					seq.append(temp);
					temp = input.nextLine().trim();
				}

				if (temp.startsWith(">")) {
					continue;
				}

				if (!input.hasNext()) {
					seq.append(temp);
					continue;
				}
			}

			temp = input.nextLine().trim();
		}

		input.close();

		String sequence = seq.toString();
		char[] array = sequence.toCharArray();

		return array;
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
