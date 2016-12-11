package core;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Scanner;

public class KmerCounterForMultipleFastaFile
{
	public static void compute(String genomeListFile, String outputDirectory, String outputFileNamePrefix,
			int kmerLength)
	{
		Date startingTimePoint = new Date(System.currentTimeMillis());
		int arraySize = DNAkmerIntIndexFastConverter.calculatePowerInt(4, kmerLength);
		int[] kmerOccurenceCountList = new int[arraySize];
		int totalGenomes = 0;

		System.out.println("Currently collecting kmers of length " + kmerLength + " from " + genomeListFile);

		Scanner input = IoHelper.getTextInput(genomeListFile);

		String temp = null;
		// String seqName = null;
		StringBuilder seq = null;
		String sequence = null;

		String outputFileName = null;

		File tempFile = new File(
				outputDirectory + File.separator + outputFileNamePrefix + ".allKmers." + kmerLength + "bps" + ".kcl");
		if (!(tempFile.exists()))
		{
			outputFileName = tempFile.toString();
		} else
		{
			int fileCount = 1;
			while (tempFile.exists())
			{
				tempFile = new File(outputDirectory + File.separator + outputFileNamePrefix + ".allKmers." + kmerLength
						+ "bps" + "." + fileCount + ".kcl");
				if (tempFile.exists())
				{
					fileCount++;
				}
			}
			outputFileName = tempFile.toString();
		}

		temp = input.nextLine().trim();
		while (input.hasNext())
		{

			if (temp.startsWith(">"))
			{
				// seqName = temp.substring(1);
				seq = new StringBuilder();

				if (totalGenomes % 500 == 0)
				{
					System.out.println(totalGenomes + " genomes have been tested.");
				}

				temp = input.nextLine().trim();
				while ((!temp.startsWith(">")) && input.hasNext())
				{
					seq.append(temp);
					temp = input.nextLine().trim();
				}

				if (temp.startsWith(">"))
				{
					sequence = seq.toString().toUpperCase(); // Note: convert to all Upper Cases
					kmerOccurenceCountList = countUniqueKmer(sequence, kmerLength, kmerOccurenceCountList);
					// dealing with the reverse-complement k-mer
					sequence = DNAreverseComplementUtils.simpleDNAreverseComplement(sequence);
					kmerOccurenceCountList = countUniqueKmer(sequence, kmerLength, kmerOccurenceCountList);
					
					totalGenomes++;
					continue;
				}

				if (!input.hasNext())
				{
					seq.append(temp);
					sequence = seq.toString().toUpperCase(); // Note: convert to all Upper Cases
					kmerOccurenceCountList = countUniqueKmer(sequence, kmerLength, kmerOccurenceCountList);
					
					totalGenomes++;
					break;
				}
			}

		}

		input.close();

		outputKmerCountAsText(outputFileName, kmerOccurenceCountList);

		Date endingTimePoint = new Date(System.currentTimeMillis());
		SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
		System.out.println("Kmer collection started at " + currentTime.format(startingTimePoint));
		System.out.println("Kmer collection completed at " + currentTime.format(endingTimePoint) + " for " + totalGenomes
				+ " genomes. Results stored in " + outputDirectory);
	}
	
	private static int[] countUniqueKmer(String seq, int kMerLength, int[] kmerOccurenceCountList)
	{
		String kMer = null;
		int indexPos = 0;
		int i = 0;
		HashSet<Integer> seenkmers = new HashSet<Integer>();
		// for the forward
		for(i = 0 ; i < seq.length() - kMerLength + 1 ; i ++)
		{
			kMer = seq.substring(i, i + kMerLength);
			indexPos = DNAkmerIntIndexFastConverter.computeIntDNAseqIndex(kMer);
			//System.out.println("kMer = " + kMer);
			//System.out.println("indexPos = " + indexPos);
			
			if (indexPos >= 0)
			{
				if (!(seenkmers.contains(indexPos)))
				{
					seenkmers.add(indexPos);
					kmerOccurenceCountList[indexPos]++;
				}
			}
		}
		
		// for the reverse
		seq = DNAreverseComplementUtils.simpleDNAreverseComplement(seq);
		for(i = 0 ; i < seq.length() - kMerLength + 1 ; i ++)
		{
			kMer = seq.substring(i, i + kMerLength);
			indexPos = DNAkmerIntIndexFastConverter.computeIntDNAseqIndex(kMer);
			//System.out.println("kMer = " + kMer);
			//System.out.println("indexPos = " + indexPos);
			
			if (indexPos >= 0)
			{
				if (!(seenkmers.contains(indexPos)))
				{
					seenkmers.add(indexPos);
					kmerOccurenceCountList[indexPos]++;
				}
			}
		}
		
		return(kmerOccurenceCountList);
	}

	private static void outputKmerCountAsText(String outputFileName, int[] kmerList)
	{
		// Output of KmerList Array
		PrintWriter output = IoHelper.getTextOutput(outputFileName);
		for (int i = 0; i < kmerList.length; i++)
		{
			output.println(kmerList[i]);
		}
		output.close();
		System.out.println("K-mer index has been written to file:" + outputFileName);
	}
}
