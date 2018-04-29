/**
 * BranchListImpl.java
 *
 * @author Jordan Wine
 */

package main.java.memoranda;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Vector;

import main.java.memoranda.util.Branch;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

/**
 * Implements a list of Branchs as XON Elements.
 */

public class BranchListImpl implements IBranchList {
    
    
    private String filePath = null;
    Vector<Branch> branches = new Vector<Branch>();
    private Document document = null;
    
    
    /**
     * Accepts the path of an xom docuemnt, parses the document and builds
     * a vector of branches.
     * @param path The path of the document to parse.
     */
    public BranchListImpl(String path, boolean isNew) {
        filePath = path;
        
        // Read file and build branch vector
        Builder builder = new Builder();
        if (isNew) {
            Element root = new Element("branch-list");
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
                Branch tempBranch = new Branch(rs.get(i));
                branches.add(tempBranch);
            }
        }
    }
    
    public BranchListImpl(String path) {
        this(path, false);
    }
    
    
    public Vector<Branch> getAllBranches() {
        return branches;
    }
       
    /**
     * Searches a vector of branches for the first branch to have a id matching
     * the specified id.
     * @param id the id of the branch to find.
     */
    public Branch getBranch(String name) {
        Branch branch = null;
        for (int i = 0; i < branches.size(); i++) {
            if (branches.get(i).getName().equals(name)) {
                branch =  branches.get(i);
            }
        }
        return branch;
    }
    
    /**
     * Removes a branch based on the specified id.
     * @param id the id of the branch to remove
     * @return true if branch was found and removed. False otherwise. 
     */
    public boolean removeBranch(String name) {
        Element root = document.getRootElement();
        boolean ret = false;
        for (int i = 0; i < branches.size(); i++) {
            if (branches.get(i).getName().equals(name)) {
                branches.remove(i);
                root.removeChild(i);
                ret = true;
            }
        }
        return ret;
        
    }
    
    /**
     * Adds a branch to both the ArrayList of branches as well as the document
     * that's holding all the branches if that branch hasn't already been added
     * based on the branch's id.
     * @param branch The branch object to be added
     * @return true if the branch was added, false if it already exists.
     */
    public boolean addBranch(Branch branch) {
        // First lets check if the new branch's id is already in our arraylist.
        if (this.getBranch(branch.getName()) == null) {
            Element root = document.getRootElement();
            branches.add(branch);
            Collections.sort(branches, Collections.reverseOrder());
            root.appendChild(branch.toXml());
            return true;
        } else {
            return false;
        }
    }
    
    public int getAllBranchesCount() {
        return branches.size();
    }
    
    /**
     * Returns the branch list as an XOM document.
     * @return A formatted list of all branches.
     */
    public Document getXmlContent() {
        return document;
    }

}
