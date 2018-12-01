package QueryExpansion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

class QueryExpansion
{
	public static void build_Indices(String path_to_collection, StanfordLemmatizer s) throws Exception
	{
		HashMap<String,String[]> collection_items = Parser.return_collection(path_to_collection,s);
		String[] vocab = Clusters.build_vocab(collection_items);
		Clusters.build_Association(collection_items,vocab);
		Clusters.build_Metric(collection_items,vocab);
		Clusters.build_Scalar(collection_items,vocab);
	}
	public static void main(String args[]) throws Exception
	{
		StanfordLemmatizer s = new StanfordLemmatizer();
		String path_to_collection = args[0]; //path to seed list
		int method = Integer.parseInt(args[1]);
		build_Indices(path_to_collection,s);
		String query;
		Scanner sc=new Scanner(System.in);
		query=sc.nextLine();
		String[] query_tokens = Parser.return_tokens(query,s);
		HashSet<String> v = new HashSet<String>();
		String filename = "scalar.txt";
		if (method==1) //association
			filename = "association.txt";
		if (method==2) //metric
			filename = "metric.txt";
		for(String st:query_tokens)
		{
			v.add(st);
			for(String k:Parser.return_best(filename,st))
				v.add(k);
		}
		sc.close();
		String expanded_query = "";
		for(String st:v)
			expanded_query+= st+" ";
		System.out.println(expanded_query);
	}
};