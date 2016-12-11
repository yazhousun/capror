package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class CaprorUniqueRegionFinderEntry {

	public static void main(String[] args) {

		//INPUT unique seq file & kmerlength
		//OUTPUT unique reg file - make sure to specify forward/reverse strand when mapping regions!

		HashSet<Integer> uniqueKmersInGenome = new HashSet<Integer>(Arrays.asList(540489707,
				205724574,
				688078556,
				33494559,
				944277602
));

		int kmerLength = 15;
		char[] kmer = new char[kmerLength];
		String kmerString = null;
		int kmerBinaryValue = 0;
		char[] sequence = null;
		String seq = null;
		HashMap<Integer,Integer> forwardStringStartEnd = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> reverseStringStartEnd = new HashMap<Integer,Integer>();

		//collect sequence
		sequence = retrieveSequence("/Users/camille/Desktop/ViralGenomesIndFiles.d/Representatives/NC_030200.1.1473196886551.fasta");

		int startIndex = 0;
		int isUniqueSeq = 0;
		int uniqueStartPos = 0;
		int uniqueEndPos = 0;

		while (startIndex <= sequence.length - kmerLength) {

			for (int i = 0; i < kmerLength; i++) {
				kmer[i] = sequence[startIndex];
				startIndex++;
			}
			startIndex -= (kmerLength - 1);

			kmerString = String.valueOf(kmer);
			kmerBinaryValue = DNAkmerIntIndexFastConverter.computeIntDNAseqIndex(kmerString);

			if ((!(uniqueKmersInGenome.contains(kmerBinaryValue)) && isUniqueSeq == 1)) {
				System.out.print(uniqueEndPos + "\n");
				forwardStringStartEnd.put(uniqueStartPos, uniqueEndPos);
				isUniqueSeq = 0;
			}

			if (uniqueKmersInGenome.contains(kmerBinaryValue) && isUniqueSeq == 1) {
				uniqueEndPos = (startIndex - 1) + (kmerLength - 1);
			}

			if (uniqueKmersInGenome.contains(kmerBinaryValue) && isUniqueSeq == 0) {
				uniqueStartPos = startIndex;
				System.out.print(uniqueStartPos + "\t");
				uniqueEndPos = uniqueStartPos + (kmerLength - 1);
				isUniqueSeq = 1;
			}
		}

		//need to print last end position if last kmer goes until end of sequence
		if (startIndex >= sequence.length - kmerLength + 1 && isUniqueSeq == 1) {
			System.out.print(uniqueEndPos + "\n");
			forwardStringStartEnd.put(uniqueStartPos, uniqueEndPos);
		}
		
		// need to convert to IOhelper methods instead
		seq = sequence.toString();
		seq = DNAreverseComplementUtils.simpleDNAreverseComplement(seq);
		sequence = seq.toCharArray();
		
		startIndex = 0;
		isUniqueSeq = 0;
		uniqueStartPos = 0;
		uniqueEndPos = 0;

		while (startIndex <= sequence.length - kmerLength) {

			for (int i = 0; i < kmerLength; i++) {
				kmer[i] = sequence[startIndex];
				startIndex++;
			}
			startIndex -= (kmerLength - 1);

			kmerString = String.valueOf(kmer);
			kmerBinaryValue = DNAkmerIntIndexFastConverter.computeIntDNAseqIndex(kmerString);

			if ((!(uniqueKmersInGenome.contains(kmerBinaryValue)) && isUniqueSeq == 1)) {
				System.out.print(uniqueEndPos + "\n");
				reverseStringStartEnd.put(uniqueStartPos, uniqueEndPos);
				isUniqueSeq = 0;
			}

			if (uniqueKmersInGenome.contains(kmerBinaryValue) && isUniqueSeq == 1) {
				uniqueEndPos = (startIndex - 1) + (kmerLength - 1);
			}

			if (uniqueKmersInGenome.contains(kmerBinaryValue) && isUniqueSeq == 0) {
				uniqueStartPos = startIndex;
				System.out.print(uniqueStartPos + "\t");
				uniqueEndPos = uniqueStartPos + (kmerLength - 1);
				isUniqueSeq = 1;
			}
		}

		//need to print last end position if last kmer goes until end of sequence
		if (startIndex >= sequence.length - kmerLength + 1 && isUniqueSeq == 1) {
			System.out.print(uniqueEndPos + "\n");
			reverseStringStartEnd.put(uniqueStartPos, uniqueEndPos);
		}		

		System.out.println("FORWARD" + forwardStringStartEnd);
		System.out.println("REVERSE" + reverseStringStartEnd);


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
