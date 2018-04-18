/**
 * CommitList.java
 * 
 * @author Jordan Wine
 */

package main.java.memoranda;

import java.util.Vector;

import main.java.memoranda.util.Commit;
import nu.xom.Document;
/**
 * Defines a list of GitHub Commits.
 */

public interface CommitList {
    
    Vector<Commit> getAllCommits();
    
    Commit getCommit(String sha);
    
    boolean addCommit(Commit commit);
    
    boolean removeCommit(String sha);
        
    int getAllCommitsCount();
    
    Document getXmlContent();

}
