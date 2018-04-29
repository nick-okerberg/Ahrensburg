package test.java;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import main.java.memoranda.util.Commit;
import main.java.memoranda.util.JsonApiClass;
import main.java.memoranda.util.Util;

import org.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.*;



public class JsonApiClassTest {
    URL url = null;
    static JsonApiClass jac = null;
    
    /**
     * Builds a JsonApiClass based on Jordan Wine's SER316 repo to use to test.
     */
    @BeforeClass
    public static void setup() {
        URL url;
        try {
            new File(Util.getEnvDir()).mkdirs();
            File file = new File(Util.getEnvDir()+"authencoded.txt");
            FileWriter writer = new FileWriter(file);
            String userCredentials = "api-test-user-123:test123";
            String basicAuth = "Basic " + java.util.Base64.getEncoder().encodeToString(userCredentials.getBytes());
            writer.write(basicAuth);
            writer.close();
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
     * Simple happy case test for getting branches.
     */
    @Test
    public void testGetPullBranches() {
        assertFalse(jac.getBranches().isEmpty());
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
    /**
     * Tests Wheter merge commits are ignored
     */
    @Test
    public void testMergeCommits() {
    	Vector<Commit> listOfCommits = jac.getCommitsArrLst();
    	for(Commit singleCommit : listOfCommits) {
    		System.out.println("Commit message contains:" + singleCommit.getMessage());
    		assertFalse(singleCommit.getMessage().matches("Merge") || singleCommit.getMessage().matches("merge"));
    	}
    }
    
}
