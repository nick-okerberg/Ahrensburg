package main.java.memoranda.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Provides public methods to get data from GitHub using the GitHub API. GET 
 * calls to GitHub provide JSON objects when are parsed and saved.
 * 
 * @Version 1.1
 *
 */
public class JsonApiClass {
	
	private Deque<Contributor> contributors;
	private Deque<Commit> commits;
	private ArrayList<Commit> commitsArrLst;	// Needed for US37 to iterate over commits without popping them from a Deque. 
	
	private URL url; // Base URL of API for GitHub repo
	private int _ignoredCommitCount, _apiCalls;
  
	//private URL urlCon;	
	//private URL urlCom;	 
  
  /**
   * 
   * @param url the URL of the GitHub API Json
   * @param autoImport set to true to automatically import from GitHub. defaults
   *  to false
   * @throws IOException
   * @throws JSONException
   */
  public JsonApiClass(URL url, boolean autoImport) throws IOException, JSONException {
    this.url = url;
    if (autoImport){
      contributors = importContributors(); 
      //commits = importCommits();		// US37
      commitsArrLst = importCommitsArrLst();	// US37
    }
  }
  
  public JsonApiClass(String urlString, boolean autoImport) throws IOException, JSONException {
    this(new URL(urlString),autoImport);
  }
	
  public JsonApiClass(URL url) throws IOException, JSONException {
    this(url, false); 
  }
  
  public JsonApiClass(String urlString) throws IOException, JSONException {
    this(urlString, false);
  }
  
  public Deque<Contributor> getContributors(){
    if (contributors.isEmpty())
      throw new NullPointerException("No contributors added yet");
    return contributors;    
  }
  
  /**
   * Added for US37, return an Array List of Commit objects. 
   */
  public ArrayList<Commit> getCommitsArrLst(){
	    if (commitsArrLst.isEmpty())
	      throw new NullPointerException("No commits added yet");
	    return commitsArrLst;    
  }
  
  public void setContributors(Deque<Contributor> newContributors){
    contributors = newContributors;    
  }
  
  public Deque<Commit> getCommits() {
    if (commits.isEmpty())
      throw new NullPointerException("No commits added yet");
    
    return commits;    
  }
  
  public void setCommits(Deque<Commit> newCommits){
    commits = newCommits;    
  }
  
  public int getApiCallCount() {
    return _apiCalls;
  }
  
  public int getIgnoredCount() {
    return _ignoredCommitCount;
  }
  
  public void refreshAll() throws JSONException, IOException {
    this.contributors = importContributors(); 
    this.commits = importCommits();
  }
  
  public void refreshContributors() throws JSONException, IOException {
    this.contributors = importContributors(); 
  }
  
  public void refreshCommits() throws JSONException, IOException {
    this.commits = importCommits();
  }
  
  /**
   * builds a list of GitHub commits based on the base repo API url string.
   * 
   * @return List of commits
   * @throws IOException
   * @throws JSONException 
   * @throws InterruptedException
   */
  private Deque<Commit> importCommits() throws IOException, JSONException{
    JSONObject baseJson = getJsonFromURL(this.url);
    Deque<Commit> tempCommits = new LinkedList<>();
    
    // parse the commits URL
    String commitsUrlStr= baseJson.getString("commits_url");
    // Get rid of the sha references
    commitsUrlStr = commitsUrlStr.replaceAll("\\{/sha\\}", "");
    
    // parse branches URL. Need this to iterate over all commits
    String branchString = baseJson.getString("branches_url");
    // Get rid of the branch reference
    branchString = branchString.replaceAll("\\{/branch\\}", "");
    URL branchUrl = new URL(branchString); 
    
    //System.out.println("getting all branches");
    JSONArray branchArray = getJsonArrayFromURL(branchUrl);
    
    /* Build a linked list to keep track of the commits we've checked so far 
    based on the unique sha of the commit.
    We'll use this to prevent adding duplicates. */
    LinkedList<String> checkCommits= new LinkedList<String>();
    
    // Iterate over array of branches to make sure we don't miss any commits
    for (int i=0; i < branchArray.length(); i++) {
      JSONObject branch = branchArray.getJSONObject(i);
      //System.out.println("starting on branch: " + branch.getString("name"));
      
      //System.out.println(branch.toString());
      String latestSha = branch.getJSONObject("commit").getString("sha");
      URL latestUrl = new URL(commitsUrlStr + "?per_page=100&sha=" + latestSha);
      
      // Get the commits on this branch as an array
      JSONArray commitsJson = getJsonArrayFromURL(latestUrl);
      Util.debug("found " + commitsJson.length() + " commits");
      int addCount =0;
      
      // Keep iterating through commits until the commit at the bottom of the list
      JSONObject bottomCommit = null;
      do {
        for (int j = 0; j < commitsJson.length(); j++) {
          String thisSha = commitsJson.getJSONObject(j).getString("sha");
          if (! checkCommits.contains(thisSha)) {
            // Ignore "merge" commits (commits that have two parents)
            if (commitsJson.getJSONObject(j).getJSONArray("parents").length() <2) {
              //System.out.println("adding commit with sha: " + thisSha);
              // To get LOC we actually have to follow this commits URL
              // And make a separate API call for it, unfortunately.
              JSONObject thisCommit = commitsJson.getJSONObject(j);
              URL thisComUrl = new URL(thisCommit.getString("url"));
              tempCommits.add(new Commit(getJsonFromURL(thisComUrl)));
              addCount++;
            } else {
              Util.debug("ignoring \"merge\" commit " + thisSha);
              _ignoredCommitCount++;
            }
            checkCommits.add(thisSha);
          }
        }
        bottomCommit = commitsJson.getJSONObject(commitsJson.length()-1);
        
        // We only have to do this if our API call couldn't get all the commits
        // in one call. Often happens because the max in one call is 100
        if (bottomCommit.getJSONArray("parents").length()  > 0 ) {
          URL nextUrl = new URL(commitsUrlStr
              + "?per_page=100&sha="
              + bottomCommit.getString("sha"));
          commitsJson = getJsonArrayFromURL(nextUrl);
          //System.out.println("found " + commitsJson.length() + " commits");      
        }
      } while (bottomCommit.getJSONArray("parents").length() > 0);
      Util.debug("Added " + addCount + " commits from branch " + branch.getString("name") );
      
    }
    // We've finished with all the commits in each branch
    return tempCommits;
  }
  
  /**
   * US37 - Added functionality to import commit objects into an ArrayList
   * data structure. 
   * 
   * @return The ArrayList of Commit objects. 
   * @throws IOException
   * @throws JSONException
   */
  private ArrayList<Commit> importCommitsArrLst() throws IOException, JSONException{
	    JSONObject baseJson = getJsonFromURL(this.url);
	    ArrayList<Commit> tempCommits = new ArrayList<>();
	    
	    // parse the commits URL
	    String commitsUrlStr= baseJson.getString("commits_url");
	    // Get rid of the sha references
	    commitsUrlStr = commitsUrlStr.replaceAll("\\{/sha\\}", "");
	    
	    // parse branches URL. Need this to iterate over all commits
	    String branchString = baseJson.getString("branches_url");
	    // Get rid of the branch reference
	    branchString = branchString.replaceAll("\\{/branch\\}", "");
	    URL branchUrl = new URL(branchString); 
	    
	    //System.out.println("getting all branches");
	    JSONArray branchArray = getJsonArrayFromURL(branchUrl);
	    
	    /* Build a linked list to keep track of the commits we've checked so far 
	    based on the unique sha of the commit.
	    We'll use this to prevent adding duplicates. */
	    LinkedList<String> checkCommits= new LinkedList<String>();
	    
	    // Iterate over array of branches to make sure we don't miss any commits
	    for (int i=0; i < branchArray.length(); i++) {
	      JSONObject branch = branchArray.getJSONObject(i);
	      //System.out.println("starting on branch: " + branch.getString("name"));
	      
	      //System.out.println(branch.toString());
	      String latestSha = branch.getJSONObject("commit").getString("sha");
	      URL latestUrl = new URL(commitsUrlStr + "?per_page=100&sha=" + latestSha);
	      
	      // Get the commits on this branch as an array
	      JSONArray commitsJson = getJsonArrayFromURL(latestUrl);
	      Util.debug("found " + commitsJson.length() + " commits");
	      int addCount =0;
	      
	      // Keep iterating through commits until the commit at the bottom of the list
	      JSONObject bottomCommit = null;
	      do {
	        for (int j = 0; j < commitsJson.length(); j++) {
	          String thisSha = commitsJson.getJSONObject(j).getString("sha");
	          if (! checkCommits.contains(thisSha)) {
	            // Ignore "merge" commits (commits that have two parents)
	            if (commitsJson.getJSONObject(j).getJSONArray("parents").length() <2) {
	              //System.out.println("adding commit with sha: " + thisSha);
	              // To get LOC we actually have to follow this commits URL
	              // And make a separate API call for it, unfortunately.
	              JSONObject thisCommit = commitsJson.getJSONObject(j);
	              URL thisComUrl = new URL(thisCommit.getString("url"));
	              tempCommits.add(new Commit(getJsonFromURL(thisComUrl)));
	              addCount++;
	            } else {
	              Util.debug("ignoring \"merge\" commit " + thisSha);
	              _ignoredCommitCount++;
	            }
	            checkCommits.add(thisSha);
	          }
	        }
	        bottomCommit = commitsJson.getJSONObject(commitsJson.length()-1);
	        
	        // We only have to do this if our API call couldn't get all the commits
	        // in one call. Often happens because the max in one call is 100
	        if (bottomCommit.getJSONArray("parents").length()  > 0 ) {
	          URL nextUrl = new URL(commitsUrlStr
	              + "?per_page=100&sha="
	              + bottomCommit.getString("sha"));
	          commitsJson = getJsonArrayFromURL(nextUrl);
	          //System.out.println("found " + commitsJson.length() + " commits");      
	        }
	      } while (bottomCommit.getJSONArray("parents").length() > 0);
	      Util.debug("Added " + addCount + " commits from branch " + branch.getString("name") );
	      
	    }
	    // We've finished with all the commits in each branch
	    return tempCommits;
	  }
  
  /**
   * Downloads a JSON object from a URL
   * @param url - The URL of the JSON object
   * @return the downloaded JSON object
   * @throws IOException
   * @throws JSONException 
   */
  private JSONObject getJsonFromURL(URL url) throws IOException, JSONException {
    // Got to the Repo URL to get the base JSON object
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    String content = new String(Files.readAllBytes(Paths.get(Util.getEnvDir()+"authencoded.txt")));
    con.setRequestProperty ("Authorization", content);	
    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
    Util.debug("Making gitHub API call to: " + url.toString());
    JSONObject json = new JSONObject(new JSONTokener(br));
    _apiCalls++;
    
    return json;
  }
    
  /**
   * Downloads a JSON array from a URL
   * @param url - The URL of the JSON array
   * @return the downloaded JSON array
   * @throws IOException
   * @throws JSONException 
   */
  private JSONArray getJsonArrayFromURL(URL url) throws IOException, JSONException {
    // Got to the Repo URL to get the base JSON object
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    String content = new String(Files.readAllBytes(Paths.get(Util.getEnvDir()+"authencoded.txt")));
    con.setRequestProperty ("Authorization", content);	
    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
    Util.debug("Making gitHub API call to: " + url.toString());
    JSONArray json = new JSONArray(new JSONTokener(br));
    _apiCalls++;
    
    return json;
  }
  
  /**
   * Builds a list of GitHub contributors associated with the base URL
   * of a GitHub repo
   * @return A list of Contributors
   * @throws IOException
   * @throws JSONException 
   */
  private Deque<Contributor> importContributors() throws IOException, JSONException{
    JSONObject baseJson = getJsonFromURL(this.url);
    Deque<Contributor> tempContributors = new LinkedList<>();
    
    // parse the contibutors URL
    String contribUrlStr= baseJson.getString("contributors_url");
    URL contribUrl = new URL(contribUrlStr);
    
    JSONArray contribArray = getJsonArrayFromURL(contribUrl);
    for (int i=0; i < contribArray.length(); i++) {
      String thisContribUrlStr = contribArray.getJSONObject(i).getString("url");
      URL thisContribUrl = new URL (thisContribUrlStr);
      JSONObject contribJson = getJsonFromURL(thisContribUrl);
      tempContributors.add(new Contributor(contribJson));
    }    
    return tempContributors;
  }
  
  /*
  public void saveInFile(URL urlCon,URL urlCom) {
  	
  	String conLine, comLine;
  	try
  	{
  		File conFile = new File("contributors.json");
  		File comFile = new File("commits.json");
  		
  		if(!conFile.exists())
  		{
  			//URLConnection con = urlCon.openConnection();
  			HttpURLConnection con = (HttpURLConnection) urlCon.openConnection();
  			String pass =  "Basic TmVyZ2FsR0l2YXJrZXM6S2VlcDQ0ZG9n";
  			
  			
  			con.setRequestProperty ("Authorization", pass);
  			
  			
  			BufferedReader brCon = new BufferedReader(new InputStreamReader(con.getInputStream()));
  			PrintWriter writeCon = new PrintWriter("contributors.json");
  			System.out.println("--> " + con.getRequestProperty("x-ratelimit-remaining"));
  			System.out.println("--> " + con.getHeaderField("x-ratelimit-remaining"));
  			while ((conLine = brCon.readLine()) != null) 
  			{
  				writeCon.println(conLine);
  			}
  			writeCon.close();
  			brCon.close();
  		}
  		
  		if(!comFile.exists())
  		{
  			URLConnection com = urlCom.openConnection();
  			BufferedReader brCom = new BufferedReader(new InputStreamReader(com.getInputStream()));
  			PrintWriter writeCom = new PrintWriter("commits.json");
  		
  			while ((comLine = brCom.readLine()) != null) 
  			{
  				writeCom.println(comLine);
  			}
  			writeCom.close();
  			brCom.close();
  		}
  		
  	}
  	catch (FileNotFoundException e )
  	{
  		System.out.println(e.getMessage());
  	} 
  	catch(Exception e)
  	{
  		System.out.println(e.getMessage());
  	}
  }
  public void ReadConFileContent() {
  	
  	try 
  	{
  		FileInputStream in = new FileInputStream("contributors.json");
  		JSONArray JOCon = new JSONArray(new JSONTokener(in));
  
  		for(int i = 0; i < JOCon.length(); i++)
  		{
  			String login = JOCon.getJSONObject(i).getString("login");
  			int id = JOCon.getJSONObject(i).getInt("id");
  			String avatar_url = JOCon.getJSONObject(i).getString("avatar_url");
  			String gravatar_id = JOCon.getJSONObject(i).getString("gravatar_id");
  			String Url = JOCon.getJSONObject(i).getString("url");
  			String html_url = JOCon.getJSONObject(i).getString("html_url");
  			String followers_url = JOCon.getJSONObject(i).getString("followers_url");
  			String following_url = JOCon.getJSONObject(i).getString("following_url");
  			String gists_url = JOCon.getJSONObject(i).getString("gists_url");
  			String starred_url = JOCon.getJSONObject(i).getString("starred_url");
  			String subscriptions_url = JOCon.getJSONObject(i).getString("subscriptions_url");
  			String organizations_url = JOCon.getJSONObject(i).getString("organizations_url");
  			String repos_url = JOCon.getJSONObject(i).getString("repos_url");
  			String events_url = JOCon.getJSONObject(i).getString("events_url");
  			String received_events_url = JOCon.getJSONObject(i).getString("received_events_url");
  			String type = JOCon.getJSONObject(i).getString("type");
  			boolean site_admin = JOCon.getJSONObject(i).getBoolean("site_admin");
  			int contributions = JOCon.getJSONObject(i).getInt("contributions");
  		
  		
  			Contributor JCC = new Contributor(login, id, avatar_url, gravatar_id, Url, html_url, followers_url,
  				following_url, gists_url, starred_url, subscriptions_url, organizations_url, repos_url
  				, events_url, received_events_url, type, site_admin, contributions);
  		
  			contributors.add(JCC);
  		}
  	
  	}
  	
  	catch (Exception e) 
  	{
  		System.out.println(e.getMessage());
  	}
  	
  	
  }
  public Deque<Contributor> getCondata(){
  	
  	return this.contributors;
  }
  public void ReadComFileContent() {
  		
  	String sha = "", comLogin = "", comType = "", autLogin = "" , autType = "", message = "", autName = "",autEmail = "", comName = "", comEmail = "", reason = "";
  	int comId = 0, autId = 0;
  	boolean comSiteAdmin = true, autSiteAdmin = true, verified = true;
  	
  	try 
  	{
  		FileInputStream in = new FileInputStream("commits.json");
  		JSONArray JOCom = new JSONArray(new JSONTokener(in));
  		
  		for(int i = 0; i < JOCom.length(); i++)
  		{
  			JSONObject json = JOCom.getJSONObject(i); 
  			String [] Ob = JSONObject.getNames(json);
  			sha = JOCom.getJSONObject(i).getString("sha");
  			
  			
  			
  			for(int j = 0; j < Ob.length; j++)
  			{
  				
  				if(Ob[j].equals("committer") && !(JOCom.getJSONObject(i).isNull("committer")) )	
  				{
  					
  					comLogin = json.getJSONObject(Ob[j]).getString("login");
  					comId = json.getJSONObject(Ob[j]).getInt("id");
  					comType = json.getJSONObject(Ob[j]).getString("type");
  					comSiteAdmin = json.getJSONObject(Ob[j]).getBoolean("site_admin");
  				}
  				
  				if(Ob[j].equals("author")&& !(JOCom.getJSONObject(i).isNull("author")))
  				{
  					autLogin = json.getJSONObject(Ob[j]).getString("login");
  					autId = json.getJSONObject(Ob[j]).getInt("id");
  					autType = json.getJSONObject(Ob[j]).getString("type");
  					autSiteAdmin = json.getJSONObject(Ob[j]).getBoolean("site_admin");
  				}
  				if(JOCom.getJSONObject(i).isNull("committer") || JOCom.getJSONObject(i).isNull("author")) 
  				{
  					comLogin = autLogin  = "n/a";
  					comId = autId = 0;
  					comType = autType = "n/a";
  					
  				}
  				
  			}
  			
  			JSONObject json2 = json.getJSONObject("commit");
  			String [] Ob2 = JSONObject.getNames(json2);
  			message = json2.getString("message");
  			
  			for(int k = 0; k < Ob2.length; k++)
  			{
  				
  				if(Ob2[k].equals("author"))
  				{
  					autName = json2.getJSONObject(Ob2[k]).getString("name");
  					autEmail = json2.getJSONObject(Ob2[k]).getString("email");
  				}
  				if(Ob2[k].equals("committer"))
  				{
  					comName = json2.getJSONObject(Ob2[k]).getString("name");
  					comEmail = json2.getJSONObject(Ob2[k]).getString("email");
  				}
  				if(Ob2[k].equals("verification"))
  				{
  					verified = json2.getJSONObject(Ob2[k]).getBoolean("verified");
  					reason = json2.getJSONObject(Ob2[k]).getString("reason");
  				}
  				
  			}
  			
  			Commit JCC = 
  					new Commit(sha, comLogin, comType, autLogin, autType, message, autName, autEmail, comName, comEmail, reason, comId, autId, comSiteAdmin, autSiteAdmin, verified);
  			commits.add(JCC);
  		
  		}  	
  	}
  	catch (Exception e)
  	{
  		System.out.println(e.getMessage());
  	}
  }
  */
}
