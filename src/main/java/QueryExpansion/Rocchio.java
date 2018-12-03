package QueryExpansion;

import core.query.model.Document;
import core.query.model.Query;
import core.utils.Utils;

import java.util.*;

public class Rocchio
{
	public static String return_best(Query q, Document[] dp, Document[] dn)
	{
		Map<String, Double> vector = new LinkedHashMap<>();
		int ct=2;
		StringBuilder return_query = new StringBuilder();
		for(String s:q.getVector().keySet()) {
			return_query.append(s).append(" ");
			vector.put(s, 0.0);
		}
		for(Document d:dp) {
			if(d != null) {
				for (String s : d.getVector().keySet())
					vector.put(s, 0.0);
			}
		}
		for(Document d:dn)
			if(d != null) {
				for (String s : d.getVector().keySet())
					vector.put(s, 0.0);
			}
		for(String s:vector.keySet())
			vector.put(s,rocchio_score(s,q,dp,dn));
		Map<String, Double> new_vector = new Utils<String>().sortMap(vector);
		HashSet<String> q_final = new HashSet<>(new_vector.keySet());
		for(String s:q_final)
		{
			return_query.append(s).append(" ");
			ct--;
			if(ct==0)
				break;
		}
		return return_query.toString();
	}

	public static double rocchio_score(String s, Query q, Document[] dp, Document[] dn)
	{
		double alpha = q.getVector().getOrDefault(s, 0.0);
		double beta = 0.0;
		double gamma = 0.0;
		for(Document d:dp)
		{
			if(d != null) {
				for (String str : d.getVector().keySet())
					if (s.equals(str))
						beta += d.getVector().get(str);
			}
		}
		for(Document d:dn)
		{
			if(d != null) {
				for (String str : d.getVector().keySet())
					if (s.equals(str))
						gamma += d.getVector().get(str);
			}
		}
		return alpha+0.75*beta+0.25*gamma;
	}
}