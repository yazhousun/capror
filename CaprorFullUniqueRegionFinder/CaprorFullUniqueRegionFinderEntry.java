package core;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class CaprorFullUniqueRegionFinderEntry {

	public static void main(String[] args) {

		// Process input parameters
		// Need: genomeFileDirectory currentversionsfile kmerListFile
		// outputDirectory kmerLength lessThanValue gapSize(for relaxed merge) greaterThanbps(for big regions)

		String kmerListFile = "/Users/camille/Desktop/kmerOutputFilesIndFiles.d/viruses.representatives.20160913.allKmers.15bps.kcl";
		Scanner kmerInput = IoHelper.getTextInput(kmerListFile);

		// Create HashSet of unique kmers
		int kmerLength = 15;
		int arraySize = DNAkmerIntIndexFastConverter.calculatePowerInt(4, kmerLength);
		int[] kmers = new int[arraySize];
		int lessThanValue = 2;
		String temp = null;
		int intValueOfInput = 0;
		int lineIndexNumber = 0;
		while (kmerInput.hasNext()) {
			temp = kmerInput.nextLine().trim();
			intValueOfInput = Integer.parseInt(temp);

			if (intValueOfInput < lessThanValue) {
				kmers[lineIndexNumber] = intValueOfInput;
			}
			lineIndexNumber++;

			if (lineIndexNumber % 100000 == 0)
				System.out.println(lineIndexNumber);
		}

		// Gather list of all genomes to study
		Scanner input = null;

		// Currently looking at only one file
		String currentDirectory = "/Users/camille/Desktop/ViralGenomesIndFiles.d/Representatives/";
		String currentFilename = "NC_030200.1.1473196886551.fasta";

		String genomeFile = currentDirectory + currentFilename;

		// get beginning of filename for output file
		currentFilename = IoHelper.removeExtension(currentFilename);

		StringBuilder seq = null;
		String sequence = null;

		input = IoHelper.getTextInput(genomeFile);

		temp = input.nextLine().trim();
		while (input.hasNext()) {
			if (temp.startsWith(">")) {
				seq = new StringBuilder();
				temp = input.nextLine().trim();
				while ((!temp.startsWith(">")) && input.hasNext()) {
					seq.append(temp);
					temp = input.nextLine().trim();
				}

				if (temp.startsWith(">")) {
					sequence = seq.toString().toUpperCase(); // Note:
					// convert
					// to all
					// Upper
					// Cases

				}

				if (!input.hasNext()) {
					seq.append(temp);
					sequence = seq.toString().toUpperCase(); // Note:
					// convert
					// to all
					// Upper
					// Cases
					break;
				}
			}
		}

		// For each genome...

		// Scan the genome and isolate which of the kmers in the HashSet are
		// present in that genome - creating a new HashSet

		HashSet<Integer> kmersInGenome = new HashSet<Integer>();
		String kmer;
		int indexPos = 0;
		int i = 0;

		// forward strand
		for (i = 0; i < sequence.length() - kmerLength + 1; i++) {

			kmer = sequence.substring(i, i + kmerLength);
			indexPos = DNAkmerIntIndexFastConverter.computeIntDNAseqIndex(kmer);

			if (indexPos >= 0) {
				if (kmers[indexPos] > 0) {
					if (!(kmersInGenome.contains(indexPos))) {
						kmersInGenome.add(indexPos);
					}
				}
			}
		}

		// reverse strand
		sequence = DNAreverseComplementUtils.simpleDNAreverseComplement(sequence);
		for (i = 0; i < sequence.length() - kmerLength + 1; i++) {
			kmer = sequence.substring(i, i + kmerLength);
			indexPos = DNAkmerIntIndexFastConverter.computeIntDNAseqIndex(kmer);

			if (indexPos >= 0) {
				if (kmers[indexPos] > 0) {
					if (!(kmersInGenome.contains(indexPos))) {
						kmersInGenome.add(indexPos);
					}
				}
			}
		}

		// change back to forward strand
		sequence = DNAreverseComplementUtils.simpleDNAreverseComplement(sequence);

		// Determine the start/end positions of the regions within the genome
		HashMap<Integer, Integer> forwardStringStartEnd = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> reverseStringStartEnd = new HashMap<Integer, Integer>();

		int isUniqueSeq = 0;
		int startPos = 0;
		int endPos = 0;
		int uniqueStartPos = 0;
		int uniqueEndPos = 0;

		for (i = 0; i < sequence.length() - kmerLength + 1; i++) {

			kmer = sequence.substring(i, i + kmerLength);
			indexPos = DNAkmerIntIndexFastConverter.computeIntDNAseqIndex(kmer);
			startPos = i + 1;
			endPos = startPos + (kmerLength - 1);

			if ((!(kmersInGenome.contains(indexPos)) && isUniqueSeq == 1)) {
				forwardStringStartEnd.put(uniqueStartPos, uniqueEndPos);
				isUniqueSeq = 0;
			}

			if (kmersInGenome.contains(indexPos) && isUniqueSeq == 1) {
				uniqueEndPos = endPos;
			}

			if (kmersInGenome.contains(indexPos) && isUniqueSeq == 0) {
				uniqueStartPos = startPos;
				uniqueEndPos = endPos;
				isUniqueSeq = 1;
			}
		}

		// need to print last end position if last kmer goes until end of
		// sequence
		if ((i + 1) >= sequence.length() - kmerLength + 1 && isUniqueSeq == 1) {
			forwardStringStartEnd.put(uniqueStartPos, uniqueEndPos);
		}

		// for the reverse strand
		sequence = DNAreverseComplementUtils.simpleDNAreverseComplement(sequence);

		isUniqueSeq = 0;
		startPos = 0;
		endPos = 0;
		uniqueStartPos = 0;
		uniqueEndPos = 0;

		for (i = 0; i < sequence.length() - kmerLength + 1; i++) {

			kmer = sequence.substring(i, i + kmerLength);
			indexPos = DNAkmerIntIndexFastConverter.computeIntDNAseqIndex(kmer);
			startPos = i + 1;
			endPos = startPos + (kmerLength - 1);

			if ((!(kmersInGenome.contains(indexPos)) && isUniqueSeq == 1)) {
				reverseStringStartEnd.put(uniqueStartPos, uniqueEndPos);
				isUniqueSeq = 0;
			}

			if (kmersInGenome.contains(indexPos) && isUniqueSeq == 1) {
				uniqueEndPos = endPos;
			}

			if (kmersInGenome.contains(indexPos) && isUniqueSeq == 0) {
				uniqueStartPos = startPos;
				uniqueEndPos = endPos;
				isUniqueSeq = 1;
			}
		}

		// need to print last end position if last kmer goes until end of
		// sequence
		if ((i + 1) >= sequence.length() - kmerLength + 1 && isUniqueSeq == 1) {
			reverseStringStartEnd.put(uniqueStartPos, uniqueEndPos);
		}

		// Merge the start and end positions into "regions"
		// Perfect merge - takes into account regions that overlap
		TreeMap<Integer, Integer> perfectMergedForward = Merger.perfectmerge(forwardStringStartEnd);
		TreeMap<Integer, Integer> perfectMergedReverse = Merger.perfectmerge(reverseStringStartEnd);

		PrintWriter output1 = IoHelper
				.getTextOutput("/Users/camille/desktop/" + currentFilename + ".SAMPLE_12-7-16.txt");

		// Relaxed merge - take into account regions that are within a certain
		// amount of bps
		int gapsize = 5;
		TreeMap<Integer, Integer> relaxedMergedForward = Merger.relaxedmerge(perfectMergedForward, gapsize);
		TreeMap<Integer, Integer> relaxedMergedReverse = Merger.relaxedmerge(perfectMergedReverse, gapsize);

		output1.println("PERFECT-FORWARD" + perfectMergedForward);
		output1.println("PERFECT-REVERSE" + perfectMergedReverse);
		output1.println("RELAXED-FORWARD" + relaxedMergedForward);
		output1.println("RELAXED-REVERSE" + relaxedMergedReverse);
		output1.close();

		// Generate output

		TreeMap<Integer, Integer> longregions = new TreeMap<Integer, Integer>();

		PrintWriter output = IoHelper.getTextOutput("/Users/camille/desktop/" + currentFilename + ".txt");
		int totalPerfectMergedRegions = perfectMergedForward.size() + perfectMergedReverse.size();
		output.println(totalPerfectMergedRegions + " Perfect merged regions");
		int totalRelaxedMergedRegions = relaxedMergedForward.size() + relaxedMergedReverse.size();
		output.println(totalRelaxedMergedRegions + " Relaxed merged regions");
		int greaterThanbps = 100;
		int bigregions = 0;

		int[] forwardTesting = new int[728];
		int[] reverseTesting = new int[728];
		int testingIndex = 0;

		int totalPerfectLengthForward = 0;
		int totalPerfectLengthReverse = 0;
		Iterator it = perfectMergedForward.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			totalPerfectLengthForward += (int) pair.getValue() - (int) pair.getKey() + 1;
			forwardTesting[testingIndex] = (int) pair.getValue() - (int) pair.getKey() + 1;
			testingIndex++;
		}

		testingIndex = 0;
		it = perfectMergedReverse.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			totalPerfectLengthReverse += (int) pair.getValue() - (int) pair.getKey() + 1;
			reverseTesting[testingIndex] = (int) pair.getValue() - (int) pair.getKey() + 1;
			testingIndex++;
		}

		output.println("The sequence is " + sequence.length() + "bps long.");
		output.println("total forward perfect merged region length is: " + totalPerfectLengthForward);
		output.println("total reverse perfect merged region length is: " + totalPerfectLengthReverse);
		double perfectpercentage = (((double) totalPerfectLengthForward) / (sequence.length())) * 100;
		output.println("perfect merged region percentage is: " + perfectpercentage);

		int totalRelaxedLengthForward = 0;
		int totalRelaxedLengthReverse = 0;
		it = relaxedMergedForward.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			totalRelaxedLengthForward += (int) pair.getValue() - (int) pair.getKey() + 1;
			if (((int) pair.getValue() - (int) pair.getKey() + 1) > greaterThanbps) {
				longregions.put((int) pair.getKey(), (int) pair.getValue());
				bigregions++;
			}
		}
		it = relaxedMergedReverse.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			totalRelaxedLengthReverse += (int) pair.getValue() - (int) pair.getKey() + 1;
		}
		output.println("total forward relaxed merged region length is: " + totalRelaxedLengthForward);
		output.println("total reverse relaxed merged region length is: " + totalRelaxedLengthReverse);
		double relaxedpercentage = (((double) totalRelaxedLengthForward) / (sequence.length())) * 100;
		output.println("perfect relaxed region percentage is: " + relaxedpercentage);
		output.println("number of regions greater than " + greaterThanbps + " bps is: " + bigregions + " ; " + longregions.size());
		output.println("regions greater than 100 bps (only relaxed forward): " + longregions);

		output.close();
	}

}
