package main.java.memoranda.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Class to contain a commit to a GitHub repo
 * @author Nergal Givarkes, Jordan Wine
 * @Version 1.1
 *
 */
public class Commit {
	private String sha, authorName, dateString, message, htmlUrl, authorLogin;
	private int additions, deletions, totalLoc;
	private int numTests;	// US37, number of tests in a commit from parsing commit message. Nick Okerberg.
	private Date date;
	
	/**
	 * An unlikely form of constructor to be used but helpful to include
	 * Defaults all attributes to null
	 */
  public Commit() {
    this.setSha(null);
    this.setAuthorName(null);
    this.setDateString(null);
    this.setDate(null);
    this.setMessage(null);
    this.setHtmlUrl(null);
    this.setAuthorLogin(null);
    this.setAdditions(0);
    this.setDeletions(0);
    this.setTotalLoc(0);
    this.numTests = 0;	// US37 default numTests is 0. 
    System.out.println("Commit object built - default const");
  }
	
  /**
   * Initializes all attributes based on specified values
   * @param sha sha of commit
   * @param authorName Full name of author of commit
   * @param dateString The date of the commit as a string
   * @param message The message associated with the commit
   * @param htmlUrl The String representation of the HTML version of the URL
   * @param authorLogin The GitHub login name of the author
   */
	public Commit(String sha, String authorName, 
	    String dateString, String message, 
	    String htmlUrl, String authorLogin,
	    int add, int del, int totalLoc) {
    this.setSha(sha);
    this.setAuthorName(authorName);
    this.setDateString(dateString);
    this.setDate(parseDate(dateString));
    this.setMessage(message);
    this.setHtmlUrl(htmlUrl);
    this.setAuthorLogin(authorLogin);
    this.setAdditions(add);
    this.setDeletions(del);
    this.setTotalLoc(totalLoc);
    // US37 - Parse the message to get numTests. 
    this.numTests = getNumTestsFromMessage(message);
    System.out.println("Commit object built - all-params const");
	}
	
	/**
	 * Constructor to accept a JSON object and parse out relevant info
	 * @param json JSON object in the form of a GitHub API Commit
	 */
	public Commit (JSONObject json) throws JSONException{
	  this.sha = json.getString("sha");
	  this.authorName = json.getJSONObject("commit").getJSONObject("author").getString("name");
	  this.setDateString(json.getJSONObject("commit").getJSONObject("author").getString("date"));
	  this.setDate(parseDate(dateString));
	  this.message = json.getJSONObject("commit").getString("message");
	  this.setHtmlUrl(json.getString("html_url"));
    // Handle if author information is null
    try {
      this.authorLogin = json.getJSONObject("author").getString("login");
    } catch (JSONException ex) {
      this.authorLogin = "null";
    }
    try {
      JSONObject stats = json.getJSONObject("stats");
      this.setAdditions(stats.getInt("additions"));
      this.setDeletions(stats.getInt("deletions"));
      this.setTotalLoc(stats.getInt("total"));
    } catch (JSONException ex) {
      this.setAdditions(0);
      this.setDeletions(0);
      this.setTotalLoc(0);
      ex.printStackTrace();
    }
    System.out.println("Commit object built - Json const");
	}
		
	
	public String getSha() {
		return sha;
	}
	public void setSha(String sha) {
		this.sha = sha;
	}

  public String getAuthorName() {
    return authorName;
  }
  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }
  public String getDateString() {
    return dateString;
  }
  public void setDateString(String dateString) {
    this.dateString = dateString;
  }
  public Date getDate() {
    return date;
  }
  public void setDate(Date date) {
    this.date = date;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public String getHtmlUrl() {
    return htmlUrl;
  }

  public void setHtmlUrl(String htmlUrl) {
    this.htmlUrl = htmlUrl;
  }
	public String getAuthorLogin() {
		return authorLogin;
	}
	public void setAuthorLogin(String authorLogin) {
		this.authorLogin = authorLogin;
	}
	
	public int getAdditions() {
    return additions;
  }

  public void setAdditions(int additions) {
    this.additions = additions;
  }

  public int getDeletions() {
    return deletions;
  }

  public void setDeletions(int deletions) {
    this.deletions = deletions;
  }

  public int getTotalLoc() {
    return totalLoc;
  }

  public void setTotalLoc(int totalLoc) {
    this.totalLoc = totalLoc;
  }
  
  // US37 getter for number of tests. 
  public int getNumTests() {
	  return numTests;
  }
  
  /**
   * Parses a date string in ISO 8601 format and returns a Date object
   * @param sDate the ISO 8601 string to parse
   * @return the Date object representing the input string
   */
  private Date parseDate(String sDate) {
	  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    Date date= null;
    
	  try {
      date = sf.parse(sDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
	  return date;
	}
  
  /**
   * US37 Nick Okerberg
   * 
   * Extracts the number of tests from a specific commit message string. The 
   * message of the commit follows a standard defined in the Ahrensburg team
   * quality policy. The format is specified as follows: 
   * 
   * 	"JUNIT-XX#" where "XX" represents the number of tests included in this commit.
   * 
   * Returns an integer value representing the number of tests. 
   * 
   * @param msg The input String representing the commit message. 
   * @return The number of tests. 
   */
  private int getNumTestsFromMessage(String msg) {
	  // Initialize the result to 0. 
	  int result = 0;
	  
	  /*
	   * Parse the numbers out of the input string. 
	   * 	Format:  "JUNIT-XX#"
	   * This will parse the XX number out of the above example. 
	   * Find the first " - " and end at the " # " symbol. 
	   */
	  if (msg.contains("JUNIT-") && msg.contains("#") && (msg.indexOf("JUNIT-") < msg.indexOf("#")) ){
		  
		  String parsed = msg.substring(msg.indexOf("JUNIT-")+6, msg.indexOf("#")-1);
		  
		  // For debugging:
		  System.out.println("[DEBUG] commit message: " + msg);
		  System.out.println("[DEBUG] parsed test value to return: " + Integer.parseInt(parsed));
		  
		  // Convert the string result representing the integer, to an int. 
		  result = Integer.parseInt(parsed);  
	  }
	  
	  return result;
  } // End of US37 method getNumTestsFromMessage 
  
} // End of class. 
