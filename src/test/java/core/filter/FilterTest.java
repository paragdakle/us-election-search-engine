package core.filter;

import core.io.FileHandler;
import org.junit.Test;

import java.util.Map;

public class FilterTest {

    @Test
    public void readFile() {
        HTMLFilter filter = new HTMLFilter();
        filter.construct();
        FileHandler handler = new FileHandler("src/test/resources/seed1.txt", filter);
        Map<String, String> content = handler.read();
        for(String key: content.keySet()) {
            assertTrue(content.get(key).trim().startsWith("experimental investigation of the aerodynamics of a wing in a slipstream"));
            break;
        }
    }
}