package test.java;

import java.io.IOException;
import java.net.URL;

import main.java.memoranda.util.JsonApiClass;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;



public class JsonApiClassTest {
    URL url = null;
    JsonApiClass jac = null;
    
    /**
     * Builds a JsonApiClass based on Jordan Wine's SER316 repo to use to test.
     */
    public JsonApiClassTest() {
        URL url;
        try {
            url = new URL("https://api.github.com/repos/DrChunks/jwine2_review");
            jac = new JsonApiClass(url,true);
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Simple happy case test for getting pull requests.
     */
    @Test
    public void testGetPullRequests() {
        assertFalse(jac.getPullRequests().isEmpty());
    }
    
    /**
     * Tests the number of commits returned based on a known repo.
     */
    @Test
    public void testPrCount() {
        // Since we know that this particular repo only ever had 3 pull requests
        // We will test against 3 pull requests.
        assertEquals(jac.getPullRequests().size(),3);
    }
}
