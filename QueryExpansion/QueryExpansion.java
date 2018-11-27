import java.util.*;
import java.io.*;
class QueryExpansion
{
	public static void main(String args[]) throws Exception
	{
		String path_to_collection = args[0];
		String query;
		Scanner sc=new Scanner(System.in);
		query=sc.nextLine();
		HashMap<String,String[]> collection_tokens = Parser.return_collection(path_to_collection);
		sc.close();
	}
};