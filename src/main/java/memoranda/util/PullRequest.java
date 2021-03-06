package main.java.memoranda.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nu.xom.Attribute;
import nu.xom.Element;

/**
 * Class to define a Pull Request to store data from a GitHub repo. When the GitHub
 * data is analzyed and parsed, these Pull Request objects can be built to store
 * that data for later use in displaying it. 
 * 
 * Most of the PullRequest object data can be built with the API using the following 
 * JSONObject format as an example:
 * 
 *      https://api.github.com/repos/ser316asu-2018/Ahrensburg/pulls/118
 *      
 * However, the approvers are on a separate API page.  The following can be
 * used for the approvers as an example as JSONArray: 
 * 
 *      https://developer.github.com/v3/pulls/reviews/
 * 
 * @author nick-okerberg
 * @Version 1.0
 *
 */
public class PullRequest  implements Comparable {
	private String _state; 		// The Pull request state: Either Open, Closed. Default is Open. 
	private String _title;		// The Pull request title. 
	private int _id; 			// The Pull request id number. 
	private int _number; 		// The Pull request number. 
	private Date _createdAt;	// When the Pull request was created. 
	private String _url;		// The URL as a string, of the Pull request. 
	private String _user;		// The GitHub username of who did the Pull Request. 
	private String _mergedBy;	// The GitHub username of who did the merge. 
	private String _head;		// The branch that is being merged into the base. 
	private String _base;		// The base branch that head is merging into. 
	private boolean _merged; 	// Whether it's merged or not. 
	private ArrayList<String> _reviewers; 	// Array List of reviewer GitHub usernames. 
	private ArrayList<String> _approvedBy;	// Array List of GitHub usernames of who approved. 



    /**
     * Default constructor with no args. 
     */
    public PullRequest() {
        this.set_state("");
        this.set_title("");
        this.set_id(-1);
        this.set_number(-1);
        this.set_createdAt(null);
        this.set_url("");
        this.set_user("");
        this.set_mergedBy("");
        this.set_head("");
        this.set_base("");
        this.set_merged(false);
        this.set_reviewers(new ArrayList<String>());
        this.set_approvedBy(new ArrayList<String>());
        System.out.println("[DEBUG] PullRequest object built - default constructor");
    }

        

    /**
     * Constructor by using specified values. 
     * 
     * @param state The Pull request state: Either Open, Closed. Default is Open. 
     * @param title The Pull request title. 
     * @param id The Pull request id number.
     * @param number The Pull request number.
     * @param createdAt When the Pull request was created. 
     * @param url The URL as a string, of the Pull request. 
     * @param user The GitHub username of who did the Pull Request.
     * @param mergedBy The GitHub username of who did the merge. 
     * @param head The branch that is being merged into the base. 
     * @param base The base branch that head is merging into. 
     * @param merged Whether it's merged or not. 
     * @param reviewers Array List of reviewer GitHub usernames. 
     * @param approvedBy Array List of GitHub usernames of who approved. 
     */
    public PullRequest(String state, String title, int id, int number, Date createdAt, String url,
            String user, String mergedBy, String head, String base, boolean merged,
            ArrayList<String> reviewers, ArrayList<String> approvedBy) {
        this.set_state(state);
        this.set_title(title);
        this.set_id(id);
        this.set_number(number);
        this.set_createdAt(createdAt);
        this.set_url(url);
        this.set_user(user);
        this.set_mergedBy(mergedBy);
        this.set_head(head);
        this.set_base(base);
        this.set_merged(merged);
        this.set_reviewers(reviewers);
        this.set_approvedBy(approvedBy);
        System.out.println("[DEBUG] PullRequest object built - constructor based on inputs");
    }

   
   
    /**
     * Constructor to accept a JSON object and parse out relevant info
     * 
     * @param json JSON object in the form of a GitHub API Pull
     * 	   Example of a pull: 
     * 			https://api.github.com/repos/ser316asu-2018/Ahrensburg/pulls/80
     * 
     */
    public PullRequest(JSONObject json) throws JSONException {
        // Initialize the Reviewers ArrayList.
        this._reviewers = new ArrayList<String>();
        
        // Parse the JSONObject, adding the values based on key. 
    	this._state = json.getString("state");
    	this._title = json.getString("title");
    	this._id = json.getInt("id");
    	this._number = json.getInt("number");
    	this._createdAt = parseDate(json.getString("created_at"));
    	this._url = json.getString("html_url");
    	this._user = json.getJSONObject("user").getString("login");
    	
    	try {
    		this._mergedBy = json.getJSONObject("merged_by").getString("login");
    	} catch (JSONException ex) {
            this._mergedBy = "null";
        }
    	
    	this._head = json.getJSONObject("head").getString("ref");
    	this._base = json.getJSONObject("base").getString("ref");
    	this._merged = json.getBoolean("merged");
    	
    	// Get an Array of the requested reviewers, if any, for the pull request. 
    	JSONArray jReviewers = (JSONArray)json.get("requested_reviewers");
    	if (jReviewers != null) {
    	    for (int i = 0; i < jReviewers.length(); i++) {
    	        //System.out.println("PullRequest debug reviewers: " + jReviewers.getJSONObject(i).getString("login"));
    	        this._reviewers.add(jReviewers.getJSONObject(i).getString("login"));
    	    }
    	}
    	
    	// this._approvedBy is not populated because it requires a different API call. 
    	// Use the Setter for this. 
    	// https://developer.github.com/v3/pulls/reviews/
    	
    	System.out.println("[DEBUG] PullRequest object built - constructor based on json");
    }

    /**
     * Parse an nu.xom (basically xml) element for the relevant attributes.
     * @param root the XOM Element to parse
     */
    public PullRequest(Element root) {
        this.set_state(root.getAttributeValue("state"));
        this.set_title(root.getAttributeValue("title"));
        this.set_id(Integer.parseInt(root.getAttributeValue("id")));
        this.set_number(Integer.parseInt(root.getAttributeValue("number")));
        this.set_createdAt(parseDate(root.getAttributeValue("created_at")));
        this.set_url(root.getAttributeValue("html_url"));
        this.set_user(root.getAttributeValue("user"));
        this.set_mergedBy(root.getAttributeValue("merged_by"));
        this.set_head(root.getAttributeValue("head"));
        this.set_base(root.getAttributeValue("base"));
        this.set_merged(Boolean.parseBoolean(root.getAttributeValue("merged")));
        
        // TODO handle list of reviewers
        // TODO handle approved by list??? I don't see this one in the GitHub API
    }
    
	/**
	 * @return the _state
	 */
	public String get_state() {
		return _state;
	}

	/**
	 * @param _state the _state to set
	 */
	public void set_state(String state) {
		this._state = state;
	}

	/**
	 * @return the _title
	 */
	public String get_title() {
		return _title;
	}

	/**
	 * @param _title the _title to set
	 */
	public void set_title(String title) {
		this._title = title;
	}

	/**
	 * @return the _id
	 */
	public int get_id() {
		return _id;
	}

	/**
	 * @param _id the _id to set
	 */
	public void set_id(int id) {
		this._id = id;
	}

	/**
	 * @return the _number
	 */
	public int get_number() {
		return _number;
	}

	/**
	 * @param _number the _number to set
	 */
	public void set_number(int number) {
		this._number = number;
	}

	/**
	 * @return the _createdAt
	 */
	public Date get_createdAt() {
		return _createdAt;
	}

	/**
	 * @param _createdAt the _createdAt to set
	 */
	public void set_createdAt(Date createdAt) {
		this._createdAt = createdAt;
	}

	/**
	 * @return the _url
	 */
	public String get_url() {
		return _url;
	}

	/**
	 * @param _url the _url to set
	 */
	public void set_url(String url) {
		this._url = url;
	}

	/**
	 * @return the _user
	 */
	public String get_user() {
		return _user;
	}

	/**
	 * @param _user the _user to set
	 */
	public void set_user(String user) {
		this._user = user;
	}

	/**
	 * @return the _mergedBy
	 */
	public String get_mergedBy() {
		return _mergedBy;
	}

	/**
	 * @param _mergedBy the _mergedBy to set
	 */
	public void set_mergedBy(String mergedBy) {
		this._mergedBy = mergedBy;
	}

	/**
	 * @return the _head
	 */
	public String get_head() {
		return _head;
	}

	/**
	 * @param _head the _head to set
	 */
	public void set_head(String head) {
		this._head = head;
	}

	/**
	 * @return the _base
	 */
	public String get_base() {
		return _base;
	}

	/**
	 * @param _base the _base to set
	 */
	public void set_base(String base) {
		this._base = base;
	}

	/**
	 * @return the _merged
	 */
	public boolean is_merged() {
		return _merged;
	}

	/**
	 * @param _merged the _merged to set
	 */
	public void set_merged(boolean merged) {
		this._merged = merged;
	}

	/**
	 * @return the _reviewers
	 */
	public ArrayList<String> get_reviewers() {
		return _reviewers;
	}

	/**
	 * @param _reviewers the _reviewers to set
	 */
	public void set_reviewers(ArrayList<String> reviewers) {
		this._reviewers = reviewers;
	}

	/**
	 * @return the _approvedBy
	 */
	public ArrayList<String> get_approvedBy() {
		return _approvedBy;
	}

	/**
	 * @param _approvedBy the _approvedBy to set
	 */
	public void set_approvedBy(ArrayList<String> approvedBy) {
		this._approvedBy = approvedBy;
	}

    @Override
    public int compareTo(Object pr) {
        if (pr instanceof PullRequest) {
            Integer newId = Integer.valueOf(((PullRequest)pr).get_id());
            Integer myId = Integer.valueOf(this.get_id());
            return myId.compareTo(newId);
        } else {
            throw new ClassCastException("this is not a commit");
        }
    }

    /**
     * returns the XML version of this object.
     * @return the nu.xom Element of this Pull Request.
     */
    public Element toXml() {
        Element root = new Element("pullrequest");
        root.addAttribute(new Attribute("state", _state));
        root.addAttribute(new Attribute("title", _title));
        root.addAttribute(new Attribute("id", String.valueOf(_id)));
        root.addAttribute(new Attribute("number", String.valueOf(_number)));
        root.addAttribute(new Attribute("created_at", dateToString(_createdAt)));
        root.addAttribute(new Attribute("html_url", _url));
        root.addAttribute(new Attribute("user", _user));
        root.addAttribute(new Attribute("merged_by", _mergedBy));
        root.addAttribute(new Attribute("head", _head));
        root.addAttribute(new Attribute("base", _base));
        root.addAttribute(new Attribute("merged", String.valueOf(_merged)));
        // TODO handle reviewers list and approved list 
        return root;
    }
    
    private String dateToString(Date date) {
        String tempDateStr = null;
        // Also update dateString
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        tempDateStr = df.format(date);
        return tempDateStr;
    }
	
	/**
	 * Setter for approvedBy, using JSONArray as input.
	 * Uses this format: 
	 *     https://developer.github.com/v3/pulls/reviews/
	 *     
	 * @param j The input JSON array from the GitHub pulls/reviews API. 
	 * 
	 * @throws JSONException 
	 */
	public void set_approvedBy(JSONArray jApprovers) throws JSONException {
	    
	   // Initialize the Class variable first.
	   this._approvedBy = new ArrayList<String>();
	    
	   // Iterate over the input JSONArray
       // Get an Array of the approvers for the pull request. 
       if (jApprovers != null) {
           for (int i = 0; i < jApprovers.length(); i++) {
               //System.out.println("PullRequest debug approvers: " + jApprovers.getJSONObject(i).getJSONObject("user").getString("login"));
               this._approvedBy.add(jApprovers.getJSONObject(i).getJSONObject("user").getString("login"));
           }
       }
	   
   } // End of method
	
	
	
	/**
	 * Takes a string representing the date as input, then parses
	 * the Date out of it to build a Date object.  Returns 
	 * a Date object back. 
	 * 
	 * @param d The input string representing the date. 
	 * @return date The parsed Date object. 
	 */
    private Date parseDate(String d) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;

        try {
            date = sf.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

} // End of class.
