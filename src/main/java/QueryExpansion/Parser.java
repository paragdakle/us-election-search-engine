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
		String return_value_1="";
		double corr,max_corr_1=0;
		while((text=br.readLine())!=null)
		{
			tokens = text.split("\t");
			if (tokens[0].equals(token))
			{
				corr = Double.parseDouble(tokens[2]);
				if(max_corr_1<corr)
				{
					max_corr_1=corr;
					return_value_1 = tokens[1];
				}
			}
			if (tokens[1].equals(token))
			{
				ccorr = Double.parseDouble(tokens[2]);
				if(max_corr_1<corr)
				{
					max_corr_1=corr;
					return_value_1 = tokens[1];
				}
			}
		}
		String[] returnvalue = {return_value_1,return_value_1};
		return returnvalue;
	}
	public static File[] get_files(String path)
	{
		File[] filesList=new File(path).listFiles();
		return filesList;
	}
	public static String[] return_tokens(String text,StanfordLemmatizer s) throws Exception
	{
		text = text.replaceAll("\\<.*?>", " ");
		text = text.replaceAll("\\d"," ");
		text = text.replaceAll("[+^:,?;=%#&~`$!@*_)(}/{\\.]"," ");
		text = text.replaceAll("\\'s", "");
		text = text.replaceAll("\\'", "");
		text = text.replaceAll("-", " ");
		text = text.replaceAll("\\s+", " ");
		text = text.trim().toLowerCase();
		List<String> lemmas = s.lemmatize(text);
		return lemmas.toArray(new String[0]);
	}
	public static HashMap<String,String[]> return_collection(String path,StanfordLemmatizer s) throws Exception
	{
		String text;
		HashMap<String,String[]> collection_tokens = new HashMap<String,String[]>();
		for (File file:get_files(path))
		{
			Scanner sc=new Scanner(new File(file.toString()));
			text = sc.useDelimiter("\\A").next();
			sc.close();
			collection_tokens.put(file.getName(),return_tokens(text,s));
		}
		return collection_tokens;
	}
};