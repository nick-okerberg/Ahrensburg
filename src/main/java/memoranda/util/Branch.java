package main.java.memoranda.util;



import org.json.JSONException;
import org.json.JSONObject;

import nu.xom.Attribute;
import nu.xom.Element;

/**
 * Class to define a Branch to store data from a GitHub repo. When the GitHub
 * data is analyzed and parsed, these Branch objects can be built to store that
 * data for later use in displaying it.
 * 
 * <p>Most of the Branch object data can be built with the API using the following
 * JSONObject format as an example:
 * 
 * <p>https://api.github.com/repos/ser316asu-2018/Ahrensburg/branches
 * 
 * @author Jordan Wine
 * @Version 1.0
 *
 */
public class Branch implements Comparable<Branch> {
    private String name;

    /**
     * Default constructor with no args.
     */
    public Branch() {
        this.setName("");
        // System.out.println("[DEBUG] Branch object built - default
        // constructor");
    }

    /**
     * Constructor by using specified values.
     * 
     * @param name The name of the branch.
     */
    public Branch(String name) {
        this.setName(name);
        // System.out.println("[DEBUG] Branch object built - constructor based
        // on inputs");
    }

    /**
     * Constructor to accept a JSON object and parse out relevant info.
     * 
     * @param json the JSON Object form of a branch
     */
    public Branch(JSONObject json) throws JSONException {
        // Parse the JSONObject, adding the values based on key.
        this.setName(json.getString("name"));
        // System.out.println("[DEBUG] Branch object built - constructor based
        // on json");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Parse an nu.xom (basically xml) element for the relevant attributes.
     * 
     * @param root the XOM Element to parse
     */
    public Branch(Element root) {
        this.setName(root.getAttributeValue("name"));
    }

    /**
     * returns the XML version of this object.
     * 
     * @return the nu.xom Element of this Branch.
     */
    public Element toXml() {
        Element root = new Element("branch");
        root.addAttribute(new Attribute("name", name));
        return root;
    }

    @Override
    public int compareTo(Branch branch) {
        if (branch instanceof Branch) {
            String name = ((Branch) branch).getName();
            String myName = this.getName();
            return myName.compareTo(name);
        } else {
            throw new ClassCastException("this is not a branch");
        }
    }

} // End of class.
