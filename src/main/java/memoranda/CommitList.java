/**
 * CommitList.java
 * 
 * @author Jordan Wine
 */

package main.java.memoranda;

import java.util.List;

import main.java.memoranda.util.Commit;
import nu.xom.Document;
/**
 * Defines a list of GitHub Commits.
 */

public interface CommitList {
    
    List<Commit> getAllCommits();
    
    List<Commit> getAllCommitsByAuthor(String login);
    
    Commit getCommit(String sha);
    
    boolean addCommit(Commit commit);
    
    boolean removeCommit(String sha);
        
    int getAllCommitsCount();
    
    Document getXmlContent();

}
