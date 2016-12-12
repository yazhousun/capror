package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class IoHelper
{
	public static String removeExtension(String s)
	{
		String filename;

		// Remove the path up to the filename.
		int lastSeparatorIndex = s.lastIndexOf(File.separator);
		if (lastSeparatorIndex == -1)
		{
			filename = s;
		} else
		{
			filename = s.substring(lastSeparatorIndex + 1);
		}

		// Remove the extension.
		int extensionIndex = filename.lastIndexOf('.');
		if (extensionIndex == -1)
			return filename;

		return filename.substring(0, extensionIndex);
	}

	public static Scanner getTextInput(String inputFileName)
	{
		Scanner input = null;

		try
		{
			input = new Scanner(new File(inputFileName));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		return (input);
	}
	
	public static PrintWriter getTextOutput(String outputFileLocation)
	{
		PrintWriter output = null;

		try
		{
			output = new PrintWriter(new File(outputFileLocation));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(-2);
		}

		return (output);
	}
}
