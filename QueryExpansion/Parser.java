import java.util.*;
import java.io.*;
class Parser
{
	public static File[] get_files(String path)
	{
		File[] filesList=new File(path).listFiles();
		return filesList;
	}
	public static String[] return_tokens(String text)
	{
		text = text.replaceAll("\\<.*?>", " ");
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
			Scanner sc=new Scanner(new File(file.toString()));
			text = sc.useDelimiter("\\A").next();
			sc.close();
			collection_tokens.put(file.getName(),return_tokens(text));
		}
		return collection_tokens;
	}
};