package core.filter;


import core.utils.Constants;

import java.util.HashMap;


public class HTMLFilter implements IFilter, Constants {

    private HashMap<String, String> filterRegexMap;

    public HTMLFilter() {
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
        this.addRegex(HTML_TAG_REGEX, "");
        this.addRegex(POSSESSIVE_REGEX, "");
        this.addRegex(SPECIAL_CHARACTER_REGEX, " ");
        this.addRegex(NUMBER_REGEX, " ");
        this.addRegex(MULTISPACE_REGEX, " ");
    }
}
