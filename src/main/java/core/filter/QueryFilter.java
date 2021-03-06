package core.filter;

import java.util.HashMap;

import static core.utils.Constants.*;


public class QueryFilter implements IFilter {

    private HashMap<String, String> filterRegexMap;

    public QueryFilter() {
        this.filterRegexMap = new HashMap<>();
    }

    public void addRegex(String regex, String replaceText) {
        filterRegexMap.put(regex, replaceText);
    }

    @Override
    public String filter(String text) {
        for(String key: filterRegexMap.keySet()) {
            text = text.replaceAll(key, filterRegexMap.get(key));
        }
        return text.trim();
    }

    @Override
    public void construct() {
        this.addRegex(POSSESSIVE_REGEX, "");
        this.addRegex(SPECIAL_CHARACTER_REGEX, " ");
        this.addRegex(NUMBER_REGEX, " ");
        this.addRegex(MULTISPACE_REGEX, " ");
    }
}
