package core;

public class SimpleFormatPrinter
{
	public String singleLineFormat = "%s";
	public String twoColumnFormat = "";
	public int firstColumnWidth;

	public String threeColumnFormat = "";
	public int secondColumnWidth;

	public String leftMostPadding = "  ";

	public SimpleFormatPrinter()
	{
		this(20, 10);
	}

	public SimpleFormatPrinter(int firstColWidth)
	{
		this(firstColWidth, 15);
	}

	public SimpleFormatPrinter(int firstColWidth, int secondColWidth)
	{
		firstColumnWidth = firstColWidth;
		secondColumnWidth = secondColWidth;

		updateFormats();
	}

	public void println(String line)
	{
		System.out.println(line);
	}

	public void println()
	{
		System.out.println();
	}

	public void printf(String col1, String col2)
	{
		System.out.printf(twoColumnFormat, col1, col2);
	}

	public void printfln(String col1, String col2)
	{
		System.out.printf(twoColumnFormat, col1, col2 + "\n");
	}

	public void printf(String col1, String col2, String col3)
	{
		System.out.printf(threeColumnFormat, col1, col2, col3);
	}

	public void updateFormats()
	{
		twoColumnFormat = leftMostPadding + "%-" + firstColumnWidth + "s  %s";
		threeColumnFormat = leftMostPadding + "%-" + firstColumnWidth + "s" + "  %-" + secondColumnWidth + "s    %s";
	}

	public void setFirstColumnWidth(int w)
	{
		firstColumnWidth = w;

		updateFormats();
	}

	public void setSecondColumnWidth(int w)
	{
		secondColumnWidth = w;

		updateFormats();
	}

	public void setLeftMostPadding(int size)
	{
		leftMostPadding = "";

		for (int i = 0; i < size; ++i)
			leftMostPadding = leftMostPadding + " ";

		updateFormats();
	}
}
