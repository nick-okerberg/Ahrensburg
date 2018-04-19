package test.java;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import main.java.memoranda.CommitList;
import main.java.memoranda.CommitListImpl;
import main.java.memoranda.util.Commit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommitListImplTest {
    CommitList commitListFromFile;
    CommitList commitListNew;
    Commit cmt; 
    Commit cmt2;
    String filePathFull;
    String filePathEmpty;
    
    /**
     * Builds two commits and two commit lists, one empty, one full.
     */
    @Before
    public void build() {
        filePathFull = "full.xml";
        filePathEmpty = "empty.xml";
        
        
        
        cmt = new Commit("1", "1","2018-04-14T14:36:43Z","1", "1","1",1,1,1);
        cmt2 = new Commit("2", "2","2018-04-14T14:36:43Z","2", "2","2",2,2,2);
        
        // Manually create a file to test with
        String cmtListFull = ""
                + "<?xml version=\"1.0\"?>\r\n"
                + "<commit-list>"
                + "<commit sha=\"c5395fce000c56037df866233fe0cd5750e78fd3\" "
                + "authorName=\"Sean M Rogers\" date=\"2018-04-13T01:38:26Z\" "
                + "message=\"Update SetCredDialog.java\" "
                + "url=\"https://github.com/ser316asu-2018/Ahrensburg/commit/c5395fce000c56037df866233fe0cd5750e78fd3\" "
                + "authorLogin=\"SeanRog\" "
                + "additions=\"42\" "
                + "deletions=\"6\" "
                + "totalLoc=\"48\" "
                + "numTests=\"0\" />"
                + "</commit-list>";
        String cmtListEmpty = "<?xml version=\"1.0\"?>\r\n<commit-list />";
        try {
            PrintWriter writer1 = new PrintWriter(filePathFull);
            writer1.println(cmtListFull);
            writer1.close();
            PrintWriter writer2 = new PrintWriter(filePathEmpty);
            writer2.println(cmtListEmpty);
            writer2.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        commitListFromFile = new CommitListImpl(filePathFull);
        commitListNew = new CommitListImpl(filePathEmpty, true);
        
        
    }

    @Test
    public void emptyFileHasNoCommits() {
        assertTrue(commitListNew.getAllCommitsCount() == 0);
    }

    @Test
    public void fullFileHas1Commit() {
        assertTrue(commitListFromFile.getAllCommitsCount() == 1);
    }
    
    @Test
    public void filterByLoginWorks() {
        String login = "1";
        commitListFromFile.addCommit(cmt);
        List<Commit> list = commitListFromFile.getAllCommitsByAuthor(login);
        assertTrue(list.size() == 1);
        for (int i = 0; i < list.size(); i++) {
            assertTrue(list.get(i).getAuthorLogin().equals(login));
        }
    }
    
    @Test
    public void testGetCommitsEmpty() {
        assertTrue(commitListNew.getAllCommits().size() == 0);
    }
    
    @Test 
    public void testGetCommitsByAuthorInvalid() {
        assertEquals(commitListFromFile.getAllCommitsByAuthor("nothere").size(),0);
    }
    
    @Test
    public void testRemoveCommitExist() {
        commitListFromFile.addCommit(cmt);
        commitListFromFile.addCommit(cmt2);
        assertTrue(commitListFromFile.removeCommit(cmt2.getSha()));
        assertEquals(commitListFromFile.getCommit(cmt2.getSha()),null);
        assertEquals(commitListFromFile.getAllCommitsCount(),2);
    }
    
    @Test
    public void testRemoveCommitInvalid() {
        assertFalse(commitListFromFile.removeCommit("nothere"));
    }
    
    @Test public void testGetCommitExist() {
        commitListFromFile.addCommit(cmt);
        
        Commit dupCommit = commitListFromFile.getCommit(cmt.getSha());
        assertEquals(dupCommit.compareTo(cmt),0);
        
    }
    
    @Test public void testGetCommitInvalid() {
        assertNull(commitListFromFile.getCommit("nothere"));
        
    }

}
