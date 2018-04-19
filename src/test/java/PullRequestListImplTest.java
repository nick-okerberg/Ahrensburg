package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.java.memoranda.PullRequestList;
import main.java.memoranda.PullRequestListImpl;
import main.java.memoranda.util.FileStorage;
import main.java.memoranda.util.PullRequest;
import nu.xom.Serializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PullRequestListImplTest {
    PullRequestList pullRequestListFromFile;
    PullRequestList pullRequestListNew;
    PullRequest pr; 
    PullRequest pr2;
    String filePathFull;
    String filePathEmpty;
    
    /**
     * Builds two pullRequests and two pullRequest lists, one empty, one full.
     */
    @Before
    public void build() {
        filePathFull = "full.xml";
        filePathEmpty = "empty.xml";
        
        
        ArrayList<String> reviewers = new ArrayList<String>();
        reviewers.add("1");
        reviewers.add("2");
        ArrayList<String> approvers = new ArrayList<String>();
        approvers.add("1");
        approvers.add("2");
        pr = new PullRequest("1", "1", 1, 1, new Date(), 
                "1", "1", "1", "1", "1,", true, reviewers, approvers);
        pr2 = new PullRequest("2", "2", 2, 2, new Date(), 
                "2", "2", "2", "2", "2,", true, reviewers, approvers);
        
        // Manually create a file to test with
        String prListFull = ""
                + "<?xml version=\"1.0\"?>\r\n"
                + "<pullRequest-list>"
                + "<pullRequest state=\"open\" "
                + "title=\"Devtest\" "
                + "id=\"182667592\" "
                + "number=\"118\" "
                + "created_at=\"2018-04-19T05:11:48Z\" "
                + "url=\"https://api.github.com/repos/ser316asu-2018/Ahrensburg/pulls/118\" "
                + "user=\"DrChunks\" "
                + "merged_by=\"null\" "
                + "head=\"devtest\" "
                + "base=\"dev\" "
                + "merged=\"false\" />"
                + "</pullRequest-list>";
        String prListEmpty = "<?xml version=\"1.0\"?>\r\n<pullRequest-list />";
        try {
            PrintWriter writer1 = new PrintWriter(filePathFull);
            writer1.println(prListFull);
            writer1.close();
            PrintWriter writer2 = new PrintWriter(filePathEmpty);
            writer2.println(prListEmpty);
            writer2.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        pullRequestListFromFile = new PullRequestListImpl(filePathFull);
        pullRequestListNew = new PullRequestListImpl(filePathEmpty, true);
    }
    
    /**
     * Cleans up files created during testing.
     */
    @After
    public void removeTestFiles() {
        File full = new File(filePathFull);
        File empty = new File(filePathEmpty);
        full.delete();
        empty.delete();
    }

    @Test
    public void emptyFileHasNoPullRequests() {
        assertTrue(pullRequestListNew.getAllPullRequestsCount() == 0);
    }

    @Test
    public void fullFileHas1PullRequest() {
        assertTrue(pullRequestListFromFile.getAllPullRequestsCount() == 1);
    }
    
    @Test
    public void filterByLoginWorks() {
        String login = "1";
        pullRequestListFromFile.addPullRequest(pr);
        List<PullRequest> list = pullRequestListFromFile.getAllPullRequestsByAuthor(login);
        assertTrue(list.size() == 1);
        for (int i = 0; i < list.size(); i++) {
            assertTrue(list.get(i).get_user().equals(login));
        }
    }
    
    @Test
    public void testGetPullRequestsEmpty() {
        assertTrue(pullRequestListNew.getAllPullRequests().size() == 0);
    }
    
    @Test 
    public void testGetPullRequestsByAuthorInvalid() {
        assertEquals(pullRequestListFromFile.getAllPullRequestsByAuthor("nothere").size(),0);
    }
    
    @Test
    public void testRemovePullRequestExist() {
        pullRequestListFromFile.addPullRequest(pr);
        pullRequestListFromFile.addPullRequest(pr2);
        assertTrue(pullRequestListFromFile.removePullRequest(pr2.get_id()));
        assertEquals(pullRequestListFromFile.getPullRequest(pr2.get_id()),null);
        assertEquals(pullRequestListFromFile.getAllPullRequestsCount(),2);
    }
    
    @Test
    public void testRemovePullRequestInvalid() {
        assertFalse(pullRequestListFromFile.removePullRequest(-1));
    }
    
    @Test public void testGetPullRequestExist() {
        pullRequestListFromFile.addPullRequest(pr);
        
        PullRequest dupPullRequest = pullRequestListFromFile.getPullRequest(pr.get_id());
        assertEquals(dupPullRequest.compareTo(pr),0);
        
    }
    
    @Test public void testGetPullRequestInvalid() {
        assertNull(pullRequestListFromFile.getPullRequest(-1));
    }

}
