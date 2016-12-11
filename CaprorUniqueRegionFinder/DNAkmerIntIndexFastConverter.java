package core;

public class DNAkmerIntIndexFastConverter
{
	public static char[] dnaUnit = { 'A', 'C', 'G', 'T' };

	public static int[] binaryIndexMap = {   
		-1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
        -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
        -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
        -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
        -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
        -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
        -1,   -1,   -1,   -1,   -1,    0,   -1,    1,   -1,   -1,
        -1,    2,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
        -1,   -1,   -1,   -1,    3,   -1,   -1,   -1,   -1,   -1,
        -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1};

	// 85 slots but we will use only 4 ones below
	// binaryIndexMap[(int)'A'] = 0;
	// binaryIndexMap[(int)'C'] = 1;
	// binaryIndexMap[(int)'G'] = 2;
	// binaryIndexMap[(int)'T'] = 3;
	
	public static String computeDNAseq(int binaryIndex, int kMerLength)
	{
		char[] dnaSeq = new char[kMerLength];

		int unitBinaryDNA = 0;

		for (int i = kMerLength - 1; i >= 0; --i)
		{
			// take out the rightmost 2 bits
			unitBinaryDNA = binaryIndex & 3;
			// fill in the dnaSeq array from right to left
			dnaSeq[i] = dnaUnit[unitBinaryDNA];
			// shift 2 bits to the right (or divide by 4 arithmetically)
			binaryIndex = binaryIndex >>> 2;
		}

		return new String(dnaSeq);
	}

	public static int computeIntDNAseqIndex(String dnaSeqString)
	{
		int binaryIndex = 0;

		int kMerLength = dnaSeqString.length();

		int unitBinaryDNA = 0;

		for (int i = 0; i < kMerLength - 1; ++i) // for n - 1 character
		{
			unitBinaryDNA = binaryIndexMap[dnaSeqString.charAt(i)];
			
			// check if this is a non-ACGT character
			if(unitBinaryDNA == -1) return(-99);

			binaryIndex = binaryIndex | unitBinaryDNA; // add it up via |
			// shift to left for the next entry add-up
			// this step can only be done to the second to last character
			binaryIndex = binaryIndex << 2; 
		}
		
		// add the last character of the string
		unitBinaryDNA = binaryIndexMap[dnaSeqString.charAt(kMerLength - 1)];
		binaryIndex = binaryIndex | unitBinaryDNA;
		
		return binaryIndex;
	}
	
	/*
	 * Static methods using static variables
	 * Purpose: to increase efficiency
	 */
	private static char[] dnaSeq = null;
	private static int kMerLength = 0;
	private static int unitBinaryDNA = 0;
	private static int i = 0;
	private static int binaryIndex = 0;
	
	public static void initializeConverterForFullStaticMethods(int kMerLengthSet)
	{
		kMerLength = kMerLengthSet;
		dnaSeq = new char[kMerLength];
	}
	
	public static String computeDNAseqFullStatic(int binaryIndex)
	{
		// reset unitBinaryDNA
		unitBinaryDNA = 0;

		for (i = kMerLength - 1; i >= 0; --i)
		{
			// take out the rightmost 2 bits
			unitBinaryDNA = binaryIndex & 3;
			// fill in the dnaSeq array from right to left
			dnaSeq[i] = dnaUnit[unitBinaryDNA];
			// shift 2 bits to the right (or divide by 4 arithmetically)
			binaryIndex = binaryIndex >>> 2;
		}

		return new String(dnaSeq);
	}
	
	public static int computeIntDNAseqIndexFullStatic(String dnaSeqString)
	{
		// reset binaryIndex
		binaryIndex = 0;

		kMerLength = dnaSeqString.length();
		
		// reset unitBinaryDNA
		unitBinaryDNA = 0;

		for (i = 0; i < kMerLength - 1; ++i) // for n - 1 character
		{
			unitBinaryDNA = binaryIndexMap[dnaSeqString.charAt(i)];
			
			// check if this is a non-ACGT character
			if(unitBinaryDNA == -1) return(-99);

			binaryIndex = binaryIndex | unitBinaryDNA; // add it up via |
			// shift to left for the next entry add-up
			// this step can only be done to the second to last character
			binaryIndex = binaryIndex << 2; 
		}
		
		// add the last character of the string
		unitBinaryDNA = binaryIndexMap[dnaSeqString.charAt(kMerLength - 1)];
		binaryIndex = binaryIndex | unitBinaryDNA;
		
		return binaryIndex;
	}
	
	public static long calculatePowerLong(int base, int exponent)
	{
		long retVal = 1;

		for (int i = 0; i < exponent; i++)
		{
			retVal = retVal * ((long) base);
		}

		return (retVal);
	}
	
	public static int calculatePowerInt(int base, int exponent)
	{
		int retVal = 1;

		for (int i = 0; i < exponent; i++)
		{
			retVal = retVal * base;
		}

		return (retVal);
	}
}
