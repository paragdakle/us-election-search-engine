import java.util.*;
import java.io.*;
import java.util.regex.PatternSyntaxException;
class Parser
{
	public static String[] return_best(String file,String token) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String text;
		String[] tokens;
		String return_value_1="",return_value_2="";
		ArrayList<Double> corr = new ArrayList<Double>();
		while((text=br.readLine())!=null)
		{
			tokens = text.split("\t");
			if (tokens[1].equals(token))
				corr.add(Double.parseDouble(tokens[2]));
			Collections.sort(corr);
			try
			{
				if(corr.get(0)==Double.parseDouble(tokens[2]))
					return_value_1=tokens[0];
				if(corr.get(1)==Double.parseDouble(tokens[2]))
					return_value_2=tokens[0];
			}
			catch(Exception e){continue; }
		}
		String[] returnvalue = {return_value_1,return_value_1};
		return returnvalue;
	}
	public static File[] get_files(String path)
	{
		File[] filesList=new File(path).listFiles();
		return filesList;
	}
	public static String[] return_tokens(String text) throws Exception
	{
		text = text.replaceAll("\\<.*?>", " ");
		text = text.replaceAll("\\d"," ");
		text = text.replaceAll("[+^:,?;=%#&~`$!@*_)(}/{\\.]"," ");
		text = text.replaceAll("\\'s", "");
		text = text.replaceAll("\\'", "");
		text = text.replaceAll("-", " ");
		text = text.replaceAll("\\s+", " ");
		text = text.trim().toLowerCase();
		return text.split(" ");
	}
	public static HashMap<String,String[]> return_collection(String path) throws Exception
	{
		String text;
		HashMap<String,String[]> collection_tokens = new HashMap<String,String[]>();
		for (File file:get_files(path))
		{
			System.out.println(file.toString());
			try
			{
				Scanner sc=new Scanner(new File(file.toString()));
				text = sc.useDelimiter("\\A").next();
				sc.close();
				collection_tokens.put(file.getName(),return_tokens(text));
			}
			catch(Exception e){ continue; }
		}
		return collection_tokens;
	}
};