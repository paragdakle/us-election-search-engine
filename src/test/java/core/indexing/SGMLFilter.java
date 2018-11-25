package core.indexing;

import core.filter.IFilter;
import core.utils.TConstants;

import java.util.HashMap;

public class SGMLFilter implements IFilter, TConstants {

    private HashMap<String, String> filterRegexMap;

    public SGMLFilter() {
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
        this.addRegex(SGML_TAG_REGEX, "");
        this.addRegex(POSSESSIVE_REGEX, "");
        this.addRegex(SPECIAL_CHARACTER_REGEX, " ");
        this.addRegex(NUMBER_REGEX, " ");
        this.addRegex(MULTISPACE_REGEX, " ");
    }
}
