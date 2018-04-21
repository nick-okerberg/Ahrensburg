package main.java.memoranda.util;

import org.json.JSONException;
import org.json.JSONObject;

import nu.xom.Attribute;
import nu.xom.Element;

public class Contributor implements Comparable {
    private String login;
    private String avatarUrl;
    private String name;

    /**
     * Constructor built off of specific string inputs.
     * @param login The GitHub username of the contributor
     * @param avatarUrl The URL to the avatar of the contributor
     * @param name The full name of the contributor
     */
    public Contributor(String login, String avatarUrl, String name) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.name = name;
    }

    /**
     * Constructor to accept a JSON object and parse out relevant info.
     * 
     * @param json
     *            JSON object in the form of a GitHub API User
     */
    public Contributor(JSONObject json) throws JSONException {
        this.login = json.getString("login");
        this.avatarUrl = json.getString("avatar_url");
        try {
            this.name = json.getString("name");
        } catch (JSONException ex) {
            this.name = "null";
        }
    }

    /**
     * Parse an nu.xom (basically xml) element for the relevant attributes.
     * 
     * @param root The XOM Element to parse
     */
    public Contributor(Element root) {
        this.setLogin(root.getAttributeValue("login"));
        this.setAvatarUrl(root.getAttributeValue("avatar_url"));
        this.setName(root.getAttributeValue("name"));
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns the XML version of this object.
     * 
     * @return the nu.xom Element of this Contributor.
     */
    public Element toXml() {
        Element root = new Element("contributor");
        root.addAttribute(new Attribute("login", login));
        root.addAttribute(new Attribute("avatar_url", avatarUrl));
        root.addAttribute(new Attribute("name", name));
        return root;
    }

    /**
     * Compares to contributor based on login name.
     * @param contributor Contributor to compare against
     * @return same comparison as strings
     */
    @Override
    public int compareTo(Object contributor) {
        if (contributor instanceof Contributor) {
            return this.getLogin().compareTo(((Contributor)contributor).getLogin());
        } else {
            throw new ClassCastException("this is not a contributor");
        }
    }
}
