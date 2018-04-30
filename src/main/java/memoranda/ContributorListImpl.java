/**
 * ContributorListImpl.java
 *
 * @author Jordan Wine
 */

package main.java.memoranda;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Vector;

import main.java.memoranda.util.Contributor;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

/**
 * Implements a list of Contributors as XON Elements.
 */

public class ContributorListImpl implements IContributorList {
    
    
    private String filePath = null;
    Vector<Contributor> contributors = new Vector<Contributor>();
    private Document document = null;
    
    
    /**
     * Accepts the path of an xom docuemnt, parses the document and builds
     * a vector of contributors.
     * @param path The path of the document to parse.
     */
    public ContributorListImpl(String path, boolean isNew) {
        filePath = path;
        
        // Read file and build contributor vector
        Builder builder = new Builder();
        if (isNew) {
            Element root = new Element("contributor-list");
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
                Contributor tempContributor = new Contributor(rs.get(i));
                contributors.add(tempContributor);
            }
        }
    }
    
    public ContributorListImpl(String path) {
        this(path, false);
    }
    
    
    public Vector<Contributor> getAllContributors() {
        return contributors;
    }
       
    /**
     * Searches a vector of contributors for the first contributor to have a id matching
     * the specified id.
     * @param login the login of the contributor to find.
     */
    public Contributor getContributor(String login) {
        Contributor contributor = null;
        for (int i = 0; i < contributors.size(); i++) {
            if (contributors.get(i).getLogin().equals(login)) {
                contributor =  contributors.get(i);
            }
        }
        return contributor;
    }
    
    /**
     * Removes a contributor based on the specified id.
     * @param login the login of the contributor to remove
     * @return true if contributor was found and removed. False otherwise. 
     */
    public boolean removeContributor(String login) {
        Element root = document.getRootElement();
        boolean ret = false;
        for (int i = 0; i < contributors.size(); i++) {
            if (contributors.get(i).getLogin().equals(login)) {
                contributors.remove(i);
                root.removeChild(i);
                ret = true;
            }
        }
        return ret;
        
    }
    
    /**
     * Adds a contributor to both the ArrayList of contributors as well as the document
     * that's holding all the contributors if that contributor hasn't already been added
     * based on the contributor's id.
     * @param contributor The contributor object to be added
     * @return true if the contributor was added, false if it already exists.
     */
    public boolean addContributor(Contributor contributor) {
        // First lets check if the new contributor's id is already in our arraylist.
        if (this.getContributor(contributor.getLogin()) == null) {
            Element root = document.getRootElement();
            contributors.add(contributor);
            Collections.sort(contributors, Collections.reverseOrder());
            root.appendChild(contributor.toXml());
            return true;
        } else {
            return false;
        }
    }
    
    public int getAllContributorsCount() {
        return contributors.size();
    }
    
    /**
     * Returns the contributor list as an XOM document.
     * @return A formatted list of all contributors.
     */
    public Document getXmlContent() {
        return document;
    }

}
