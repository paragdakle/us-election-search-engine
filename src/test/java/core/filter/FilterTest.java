package core.filter;

import core.indexing.io.FileHandler;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static core.utils.Constants.*;

public class FilterTest {

    @Test
    public void readFile() {
        HTMLFilter filter = new HTMLFilter();
        filter.addRegex(HTML_TAG_REGEX, "");
        filter.addRegex(SPECIAL_CHARACTER_REGEX, " ");
        filter.addRegex(NUMBER_REGEX, " ");
        FileHandler handler = new FileHandler("src/test/resources/core.filter.txt", filter);
        Map<String, String> content = handler.read();
        for(String key: content.keySet()) {
            assertTrue(content.get(key).trim().startsWith("experimental investigation of the aerodynamics of a wing in a slipstream"));
            break;
        }
    }
}