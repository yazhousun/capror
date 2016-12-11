package core;

public class DNAreverseComplementUtils
{
	public static String simpleDNAreverseComplement(String temp)
	{
		StringBuilder rc = new StringBuilder();
		
		for(int i = temp.length() - 1 ; i >= 0 ; i --)
		{
			if(temp.charAt(i) == 'A')
			{
				rc.append('T');
			}
			else if(temp.charAt(i) == 'T')
			{
				rc.append('A');
			}
			else if(temp.charAt(i) == 'C')
			{
				rc.append('G');
			}
			else if(temp.charAt(i) == 'G')
			{
				rc.append('C');
			}
			else if(temp.charAt(i) == 'a')
			{
				rc.append('t');
			}
			else if(temp.charAt(i) == 't')
			{
				rc.append('a');
			}
			else if(temp.charAt(i) == 'c')
			{
				rc.append('g');
			}
			else if(temp.charAt(i) == 'g')
			{
				rc.append('c');
			}
			else
			{
				rc.append(temp.charAt(i));
			}
		}
		
		return(rc.toString());
	}
}
