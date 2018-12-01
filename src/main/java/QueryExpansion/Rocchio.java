import java.io.*;
import java.utils.*;
class Rocchio
{
	private static Map<String, Double> sortByComparator(Map<String, Double> unsortMap, final boolean order)
    {
        List<Entry<String, Double> > list = new LinkedList<Entry<String, Double> >(unsortMap.entrySet());
        Collections.sort(list, new Comparator<Entry<String, Double> >()
        {
            public int compare(Entry<String, Double> o1,Entry<String, Double> o2)
            {
                if (order)
                    return o1.getValue().compareTo(o2.getValue());
                else
                    return o2.getValue().compareTo(o1.getValue());
            }
        });
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Entry<String,Double> entry : list)
            sortedMap.put(entry.getKey(),entry.getValue());
        return sortedMap;
    }
	public static String return_best(Query q,Docs[] dp,Docs[] dn)
	{
		HashSet<String> q_final = new HashSet<String>();
		Map<String, Double> vector;
		int ct=2;
		String return_query="";
		for(String s:q.getVector().keySet())
			vector.add(s,0.0);
		for(Doc d:dp)
			for(String s:d.getVector().keySet())
				vector.add(s,0.0);
		for(Doc d:dn)
			for(String s:d.getVector().keySet())
				vector.add(s,0.0);
		for(String s:vector.keySet())
			vector.put(s,rocchio_score(s,q,dp/dp.length(),dn/dn.length()));
		Map<String, Double> new_vector = sortByComparator(vector,DESC);
		for(String s:q.getVector().keySet())
			q_final.add(s);
		for(String s:new_vector.keySet())
			q_final.add(s);
		for(String s:q_final)
		{
			return_query+=s+" ";
			ct--;
			if(ct==0)
				break;
		}
		return return_query;
	}
	public static double rocchio_score(String s, Query q, Docs[] dp, Docs[] dn)
	{
		double alpha = q.getVector().get(s);
		double beta = 0.0;
		double gamma = 0.0;
		for(Doc d:dp)
		{
			for(String str:d.getVector().keySet())
				if s.equals(str)
					beta+=d.getVector().get(str);
		}
		for(Doc d:dn)
		{
			for(String str:d.getVector().keySet())
				if s.equals(str)
					gamma+=d.getVector().get(str);
		}
		return alpha+0.75*beta+0.25*gamma;
	}
}