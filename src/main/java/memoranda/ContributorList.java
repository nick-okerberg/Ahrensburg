/**
 * ContributorList.java
 * 
 * @author Jordan Wine
 */

package main.java.memoranda;

import java.util.Vector;

import main.java.memoranda.util.Contributor;
import nu.xom.Document;
/**
 * Defines a list of GitHub Contributors.
 */

public interface ContributorList {
    
    Vector<Contributor> getAllContributors();
    
    Contributor getContributor(String login);
    
    boolean addContributor(Contributor contributor);
    
    boolean removeContributor(String login);
        
    int getAllContributorsCount();
    
    Document getXmlContent();

}
