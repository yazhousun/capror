package core;

public class PrintUsage {
	public static void print()
	{
		SimpleFormatPrinter sp = new SimpleFormatPrinter();
		
		sp.println("Usage: java -jar CaprorFullUniqueRegionFinder.java ");
		sp.println();
		sp.println("Available commands:");
		sp.setFirstColumnWidth(20);
		sp.printfln("name", "definition."); 
		sp.println();
		sp.println("Author: Camille Menendez, Yazhou Sun, last update 2016-11-01");
		System.exit(0);
	}
}
