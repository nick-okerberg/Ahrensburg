/**
 * PullRequestListImpl.java
 *
 * @author Jordan Wine
 */

package main.java.memoranda;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Vector;

import main.java.memoranda.util.PullRequest;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

/**
 * Implements a list of PullRequests as XON Elements.
 */

public class PullRequestListImpl implements PullRequestList {
    
    
    private String filePath = null;
    Vector<PullRequest> pullRequests = new Vector<PullRequest>();
    private Document document = null;
    
    
    /**
     * Accepts the path of an xom docuemnt, parses the document and builds
     * a vector of pullRequests.
     * @param path The path of the document to parse.
     */
    public PullRequestListImpl(String path, boolean isNew) {
        filePath = path;
        
        // Read file and build pullRequest vector
        Builder builder = new Builder();
        if (isNew) {
            Element root = new Element("pullRequest-list");
            document = new Document(root);
        } else {
            try {
                document = builder.build(
                        new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            } catch (ParsingException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Element root = document.getRootElement();
            Elements rs = root.getChildElements();
            for (int i = 0; i < rs.size(); i++) {
                PullRequest tempPullRequest = new PullRequest(rs.get(i));
                pullRequests.add(tempPullRequest);
            }
        }
    }
    
    public PullRequestListImpl(String path) {
        this(path, false);
    }
    
    
    public Vector<PullRequest> getAllPullRequests() {
        return pullRequests;
    }

    
    /**
     * Filters for pullRequests that match a specified author GitHub login name
     * and returns a list of pullRequests by that author.
     * @param authorLogin the GitHub login name of the author
     * @return A list of pullRequests authored by the specified login name
     */
    public Vector<PullRequest> getAllPullRequestsByAuthor(String authorLogin) {  
        Vector<PullRequest> tempPullRequests = new Vector<PullRequest>(); 
        for (int i = 0; i < pullRequests.size(); i++) {
            if (pullRequests.get(i).get_user().equals(authorLogin)) {
                tempPullRequests.add(pullRequests.get(i));
            }
        }
        return tempPullRequests;
    }
       
    /**
     * Searches a vector of pullRequests for the first pullRequest to have a id matching
     * the specified id.
     * @param id the id of the pullRequest to find.
     */
    public PullRequest getPullRequest(int id) {
        PullRequest pr = null;
        for (int i = 0; i < pullRequests.size(); i++) {
            if (pullRequests.get(i).get_id() == id) {
                pr =  pullRequests.get(i);
            }
        }
        return pr;
    }
    
    /**
     * Removes a pullRequest based on the specified id.
     * @param id the id of the pullRequest to remove
     * @return true if pullRequest was found and removed. False otherwise. 
     */
    public boolean removePullRequest(int id) {
        Element root = document.getRootElement();
        boolean ret = false;
        for (int i = 0; i < pullRequests.size(); i++) {
            if (pullRequests.get(i).get_id() == id) {
                pullRequests.remove(i);
                root.removeChild(i);
                ret = true;
            }
        }
        return ret;
        
    }
    
    /**
     * Adds a pullRequest to both the ArrayList of pullRequests as well as the document
     * that's holding all the pullRequests if that pullRequest hasn't already been added
     * based on the pullRequest's id.
     * @param pr The pullRequest object to be added
     * @return true if the pullRequest was added, false if it already exists.
     */
    public boolean addPullRequest(PullRequest pr) {
        // First lets check if the new pullRequest's id is already in our arraylist.
        if (this.getPullRequest(pr.get_id()) == null) {
            Element root = document.getRootElement();
            pullRequests.add(pr);
            Collections.sort(pullRequests, Collections.reverseOrder());
            root.appendChild(pr.toXml());
            return true;
        } else {
            return false;
        }
    }
    
    public int getAllPullRequestsCount() {
        return pullRequests.size();
    }
    
    /**
     * Returns the pullRequest list as an XOM document.
     * @return A formatted list of all pullRequests.
     */
    public Document getXmlContent() {
        return document;
    }

}
