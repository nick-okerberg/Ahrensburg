/**
 * CommitListImpl.java
 *
 * @author Jordan Wine
 */

package main.java.memoranda;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import main.java.memoranda.util.Commit;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

/**
 * Implements a list of Commits as XON Elements.
 */

public class CommitListImpl implements CommitList {
    
    
    private String filePath = null;
    ArrayList<Commit> commits = new ArrayList<Commit>();
    private Document document = null;
    
    
    /**
     * Accepts the path of an xom docuemnt, parses the document and builds
     * a vector of commits.
     * @param path The path of the document to parse.
     */
    public CommitListImpl(String path, boolean isNew) {
        filePath = path;
        
        // Read file and build commit vector
        Builder builder = new Builder();
        if (isNew) {
            Element root = new Element("commit-list");
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
                Commit tempCommit = new Commit(rs.get(i));
                commits.add(tempCommit);
            }
        }
    }
    
    public CommitListImpl(String path) {
        this(path, false);
    }
    
    
    public List<Commit> getAllCommits() {
        return commits;
    }
    
    /**
     * Searches a vector of commits for the first commit to have a sha matching
     * the specified sha.
     * @param sha the sha of the commit to find.
     */
    public Commit getCommit(String sha) {
        Commit cmt = null;
        for (int i = 0; i < commits.size(); i++) {
            if (commits.get(i).getSha().equals(sha)) {
                cmt =  commits.get(i);
            }
        }
        return cmt;
    }
    
    /**
     * Removes a commit based on the specified sha.
     * @param sha the sha of the commit to remove
     * @return true if commit was found and removed. False otherwise. 
     */
    public boolean removeCommit(String sha) {
        Element root = document.getRootElement();
        boolean ret = false;
        for (int i = 0; i < commits.size(); i++) {
            if (commits.get(i).getSha().equals(sha)) {
                commits.remove(i);
                root.removeChild(i);
                ret = true;
            }
        }
        return ret;
        
    }
    
    /**
     * Adds a commit to both the ArrayList of commits as well as the document
     * that's holding all the commits if that commit hasn't already been added
     * based on the commit's sha.
     * @param cmt The commit object to be added
     * @return true if the commit was added, false if it already exists.
     */
    public boolean addCommit(Commit cmt) {
        // First lets check if the new commit's sha is already in our arraylist.
        if (this.getCommit(cmt.getSha()) == null) {
            Element root = document.getRootElement();
            commits.add(cmt);
            Collections.sort(commits, Collections.reverseOrder());
            root.appendChild(cmt.toXml());
            return true;
        } else {
            return false;
        }
    }
    
    public int getAllCommitsCount() {
        return commits.size();
    }
    
    /**
     * Returns the commit list as an XOM document.
     * @return A formatted list of all commits.
     */
    public Document getXmlContent() {
        return document;
    }

}
