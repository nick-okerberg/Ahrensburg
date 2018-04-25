/**
 * BranchList.java
 * 
 * @author Jordan Wine
 */

package main.java.memoranda;

import java.util.Vector;

import main.java.memoranda.util.Branch;
import nu.xom.Document;
/**
 * Defines a list of GitHub branches.
 */

public interface BranchList {
    
    Vector<Branch> getAllBranches();
    
    Branch getBranch(String name);
    
    boolean addBranch(Branch branch);
    
    boolean removeBranch(String name);
        
    int getAllBranchesCount();
    
    Document getXmlContent();

}
