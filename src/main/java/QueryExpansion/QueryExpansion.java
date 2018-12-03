package QueryExpansion;

import core.utils.Constants;

import java.util.*;

public class QueryExpansion
{
	public static void build_Indices(String path_to_collection,String[] v, Set<String> matchingDocNames, byte method) throws Exception
	{
		HashMap<String,String[]> collection_items = Parser.return_collection(path_to_collection, matchingDocNames);
		String[] vocab = Clusters.build_vocab(collection_items);
		switch (method) {
			case Constants.QE_ASSOCIATION:
				Clusters.build_Association(collection_items, vocab, v);
				break;

			case Constants.QE_METRIC:
				Clusters.build_Metric(collection_items,vocab);
				break;

			default:
				Clusters.build_Scalar(collection_items,vocab);
		}
	}
	public static String expander(String query, Set<String> matchingDocNames, byte method) throws Exception
	{
		String path_to_collection = Constants.TOKENIZED_CORPUS_DIR_PATH; //path to seed list
		String[] query_tokens = Parser.return_tokens(query);
		HashSet<String> v = new HashSet<String>();
		build_Indices(path_to_collection,query_tokens, matchingDocNames, method);
		String filename = "scalar.txt";
		if (method== Constants.QE_ASSOCIATION) //association
			filename = "association.txt";
		else if (method== Constants.QE_METRIC) //metric
			filename = "metric.txt";
		for(String st:query_tokens)
		{
			v.add(st);
			Collections.addAll(v, Parser.return_best(filename, st));
		}
		String expanded_query = "";
		for(String st:v)
			expanded_query += st+" ";
		return expanded_query;
	}
};