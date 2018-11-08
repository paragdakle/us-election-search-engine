package filter;

import indexing.io.FileHandler;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static utils.Constants.*;

public class FilterTest {

    @Test
    public void readFile() {
        HTMLFilter filter = new HTMLFilter();
        filter.addRegex(HTML_TAG_REGEX, "");
        filter.addRegex(SPECIAL_CHARACTER_REGEX, " ");
        filter.addRegex(NUMBER_REGEX, " ");
        FileHandler handler = new FileHandler("src/test/resources/filter.txt", filter);
        Map<String, String> content = handler.read();
        for(String key: content.keySet()) {
            assertTrue(content.get(key).trim().startsWith("experimental investigation of the aerodynamics of a wing in a slipstream"));
            break;
        }
    }
}