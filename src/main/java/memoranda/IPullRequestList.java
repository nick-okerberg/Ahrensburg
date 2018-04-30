/**
 * PullRequestList.java
 * 
 * @author Jordan Wine
 */

package main.java.memoranda;

import java.util.Vector;

import main.java.memoranda.util.PullRequest;
import nu.xom.Document;
/**
 * Defines a list of GitHub Pull Requests.
 */

public interface IPullRequestList {
    
    Vector<PullRequest> getAllPullRequests();
    
    Vector<PullRequest> getAllPullRequestsByAuthor(String login);
    
    PullRequest getPullRequest(int id);
    
    boolean addPullRequest(PullRequest pullRequest);
    
    boolean removePullRequest(int id);
        
    int getAllPullRequestsCount();
    
    Document getXmlContent();

}
