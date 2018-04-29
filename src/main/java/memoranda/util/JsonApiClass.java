package main.java.memoranda.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    private Vector<Contributor> contributors;
    private Vector<Commit> commitsArrLst;
    private Vector<PullRequest> pullRequests;
    private Vector<Branch> branches;

    private URL url; // Base URL of API for GitHub repo
    private int ignoredCommitCount;
    private int apiCalls;

    // private URL urlCon;
    // private URL urlCom;

    /**
     * Instantiates a JsonApiClass from a GitHub API url.
     * 
     * @param url the URL of the GitHub API Json
     * @param autoImport set to true to automatically import from GitHub. defaults to
     *            false
     * @throws IOException Unable to read input stream
     * @throws JSONException The data importing is not formated properly
     */
    public JsonApiClass(URL url, boolean autoImport)
            throws IOException, JSONException {
        this.url = url;
        if (autoImport) {
            contributors = importContributors();
            commitsArrLst = importCommitsArrLst(); 
            pullRequests = importPullRequests();
            branches = importBranches();
        }
    }

    public JsonApiClass(String urlString, boolean autoImport)
            throws IOException, JSONException {
        this(new URL(urlString), autoImport);
    }

    public JsonApiClass(URL url) throws IOException, JSONException {
        this(url, false);
    }

    public JsonApiClass(String urlString) throws IOException, JSONException {
        this(urlString, false);
    }

    /**
     * Gets all the contributors for the JAC.
     * @return a Vector of contributors.
     */
    public Vector<Contributor> getContributors() {
        if (contributors.isEmpty()) {
            throw new NullPointerException("No contributors added yet");
        }
        return contributors;
    }

    /**
     * Added for US37, return an Array List of Commit objects.
     */
    public Vector<Commit> getCommitsArrLst() {
        if (commitsArrLst.isEmpty()) {
            throw new NullPointerException("No commits added yet");
        }
        return commitsArrLst;
    }

    /**
     * Gets the current list of pull requests.
     * @return An Vector of pull requests.
     */
    public Vector<PullRequest> getPullRequests() {
        if (pullRequests.isEmpty()) {
            throw new NullPointerException("No pull requests added yet");
        }
        return pullRequests;
    }

    /**
     * Gets the current list of branches.
     * @return An Vector of pull requests.
     */
    public Vector<Branch> getBranches() {
        if (branches.isEmpty()) {
            throw new NullPointerException("No branches added yet");
        }
        return branches;
    }

    public void setContributors(Vector<Contributor> newContributors) {
        contributors = newContributors;
    }

    public void setCommits(Vector<Commit> newCommits) {
        commitsArrLst = newCommits;
    }

    public int getApiCallCount() {
        return apiCalls;
    }

    public int getIgnoredCount() {
        return ignoredCommitCount;
    }

    public void refreshAll() throws JSONException, IOException {
        this.contributors = importContributors();
        this.commitsArrLst = importCommitsArrLst();
    }

    public void refreshContributors() throws JSONException, IOException {
        this.contributors = importContributors();
    }

    public void refreshCommits() throws JSONException, IOException {
        this.commitsArrLst = importCommitsArrLst();
    }

    /**
     * US37 - Added functionality to import commit objects into an Vector
     * data structure.
     * 
     * @return The Vector of Commit objects.
     * @throws IOException Unable to read input stream
     * @throws JSONException The data importing is not formated properly
     */
    private Vector<Commit> importCommitsArrLst()
            throws IOException, JSONException {
        JSONObject baseJson = getJsonFromUrl(this.url);
        Vector<Commit> tempCommits = new Vector<>();

        // parse the commits URL
        String commitsUrlStr = baseJson.getString("commits_url");
        // Get rid of the sha references
        commitsUrlStr = commitsUrlStr.replaceAll("\\{/sha\\}", "");

        // parse branches URL. Need this to iterate over all commits
        String branchString = baseJson.getString("branches_url");
        // Get rid of the branch reference
        branchString = branchString.replaceAll("\\{/branch\\}", "");
        URL branchUrl = new URL(branchString);

        // System.out.println("getting all branches");
        JSONArray branchArray = getJsonArrayFromUrl(branchUrl);

        /*
         * Build a linked list to keep track of the commits we've checked so far
         * based on the unique sha of the commit. We'll use this to prevent
         * adding duplicates.
         */
        Vector<String> checkCommits = new Vector<String>();

        // Iterate over array of branches to make sure we don't miss any commits
        for (int i = 0; i < branchArray.length(); i++) {
            JSONObject branch = branchArray.getJSONObject(i);
            // System.out.println("starting on branch: " +
            // branch.getString("name"));

            // System.out.println(branch.toString());
            String latestSha = branch.getJSONObject("commit").getString("sha");
            URL latestUrl = new URL(
                    commitsUrlStr + "?per_page=100&sha=" + latestSha);

            // Get the commits on this branch as an array
            JSONArray commitsJson = getJsonArrayFromUrl(latestUrl);
            Util.debug("found " + commitsJson.length() + " commits");
            int addCount = 0;

            // Keep iterating through commits until the commit at the bottom of
            // the list
            JSONObject bottomCommit = null;
            do {
                for (int j = 0; j < commitsJson.length(); j++) {
                    String thisSha = commitsJson.getJSONObject(j)
                            .getString("sha");
                    if (!checkCommits.contains(thisSha)) {
                        // Ignore "merge" commits (commits that have two
                        // parents)
                        if (commitsJson.getJSONObject(j).getJSONArray("parents")
                                .length() < 2) {
                            // System.out.println("adding commit with sha: " +
                            // thisSha);
                            // To get LOC we actually have to follow this
                            // commits URL
                            // And make a separate API call for it,
                            // unfortunately.
                            JSONObject thisCommit = commitsJson
                                    .getJSONObject(j);
                            URL thisComUrl = new URL(
                                    thisCommit.getString("url"));
                            tempCommits.add(
                                    new Commit(getJsonFromUrl(thisComUrl)));
                            addCount++;
                        } else {
                            Util.debug("ignoring \"merge\" commit " + thisSha);
                            ignoredCommitCount++;
                        }
                        checkCommits.add(thisSha);
                    }
                }
                bottomCommit = commitsJson
                        .getJSONObject(commitsJson.length() - 1);

                // We only have to do this if our API call couldn't get all the
                // commits
                // in one call. Often happens because the max in one call is 100
                if (bottomCommit.getJSONArray("parents").length() > 0) {
                    URL nextUrl = new URL(commitsUrlStr + "?per_page=100&sha="
                            + bottomCommit.getString("sha"));
                    commitsJson = getJsonArrayFromUrl(nextUrl);
                    // System.out.println("found " + commitsJson.length() + "
                    // commits");
                }
            } while (bottomCommit.getJSONArray("parents").length() > 0);
            Util.debug("Added " + addCount + " commits from branch "
                    + branch.getString("name"));

        }
        // We've finished with all the commits in each branch
        return tempCommits;
    }

    /**
     * Downloads a JSON object from a URL.
     * 
     * @param url
     *            - The URL of the JSON object
     * @return the downloaded JSON object
     * @throws IOException Unable to read input stream
     * @throws JSONException The data importing is not formated properly
     */
    private JSONObject getJsonFromUrl(URL url)
            throws IOException, JSONException {
        // Got to the Repo URL to get the base JSON object
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            String content = new String(Files.readAllBytes(Paths.get(Util.getEnvDir()+"authencoded.txt")));
            con.setRequestProperty ("Authorization", content);	
        }
        catch(Exception e) {
        	System.out.println("File does not exist...Reverting to 60 request per hour");
        	System.out.println(e.getMessage());
        }
        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        Util.debug("Making gitHub API call to: " + url.toString());
        JSONObject json = new JSONObject(new JSONTokener(br));
        apiCalls++;

        return json;
    }

    /**
     * Downloads a JSON array from a URL.
     * 
     * @param url
     *            - The URL of the JSON array
     * @return the downloaded JSON array
     * @throws IOException Unable to read input stream
     * @throws JSONException The data importing is not formated properly
     */
    private JSONArray getJsonArrayFromUrl(URL url)
            throws IOException, JSONException {
        // Got to the Repo URL to get the base JSON object
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            String content = new String(Files.readAllBytes(Paths.get(Util.getEnvDir()+"authencoded.txt")));
            con.setRequestProperty ("Authorization", content);	
        }
        catch(Exception e) {
        	System.out.println("File does not exist...Reverting to 60 request per hour");
        	System.out.println(e.getMessage());
        }
        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        Util.debug("Making gitHub API call to: " + url.toString());
        JSONArray json = new JSONArray(new JSONTokener(br));
        apiCalls++;

        return json;
    }

    /**
     * Builds a list of GitHub contributors associated with the base URL of a
     * GitHub repo.
     * 
     * @return A list of Contributors
     * @throws IOException Unable to read input stream
     * @throws JSONException The data importing is not formated properly
     */
    private Vector<Contributor> importContributors()
            throws IOException, JSONException {
        JSONObject baseJson = getJsonFromUrl(this.url);
        Vector<Contributor> tempContributors = new Vector<>();

        // parse the contibutors URL
        String contribUrlStr = baseJson.getString("contributors_url");
        URL contribUrl = new URL(contribUrlStr);

        JSONArray contribArray = getJsonArrayFromUrl(contribUrl);
        for (int i = 0; i < contribArray.length(); i++) {
            String thisContribUrlStr = contribArray.getJSONObject(i)
                    .getString("url");
            URL thisContribUrl = new URL(thisContribUrlStr);
            JSONObject contribJson = getJsonFromUrl(thisContribUrl);
            tempContributors.add(new Contributor(contribJson));
        }
        return tempContributors;
    }
    
    /**
     * Imports a list of Pull Requests from GitHub via the GitHub API specified
     * by the saved URL.
     * 
     * @return a Vector of Pull Requests
     * @throws IOException When unable to read input stream
     * @throws JSONException When JSON object read does not parse correctly
     */
    private Vector<PullRequest> importPullRequests()
            throws IOException, JSONException {
        JSONObject baseJson = getJsonFromUrl(this.url);
        Vector<PullRequest> tempPrList = new Vector<>();
        
        // Get the pull request URL
        String prUrlStr = baseJson.getString("pulls_url");
        // Get rid of the number reference
        prUrlStr = prUrlStr.replaceAll("\\{/number\\}", "");
        String prUrlAll = prUrlStr + "?state=all";
        URL prUrl = new URL(prUrlAll);
        JSONArray prArray = getJsonArrayFromUrl(prUrl);
        
        // Get count. Assumes GitHub increments each pull request on a repo by 1
        int count = prArray.getJSONObject(0).getInt("number");
        
        // call API for each Pull Request and add it to the list
        for (int i = count; i > 0; i--) {
            String thisUrlStr = prUrlStr + "/" + i;
            JSONObject jobj = getJsonFromUrl(new URL(thisUrlStr));
            PullRequest pr = new PullRequest(jobj);
            tempPrList.add(pr);
        }
        
        return tempPrList;
    }
    
    /**
     * Imports a list of Pull Requests from GitHub via the GitHub API specified
     * by the saved URL.
     * 
     * @return a Vector of Pull Requests
     * @throws IOException When unable to read input stream
     * @throws JSONException When JSON object read does not parse correctly
     */
    private Vector<Branch> importBranches()
            throws IOException, JSONException {
        JSONObject baseJson = getJsonFromUrl(this.url);
        Vector<Branch> tempBranchList = new Vector<>();
        
        // Get the pull request URL
        String branchUrlStr = baseJson.getString("branches_url");
        // Get rid of the number reference
        branchUrlStr = branchUrlStr.replaceAll("\\{/branch\\}", "");
        String branchUrlAll = branchUrlStr + "?per_page=100";
        URL branchUrl = new URL(branchUrlAll);
        JSONArray prArray = getJsonArrayFromUrl(branchUrl);
        
        // Build each branch from array
        for (int i = 0; i < prArray.length(); i++) {
            Branch branch = new Branch(prArray.getJSONObject(i));
            tempBranchList.add(branch);
        }
        
        return tempBranchList;
    }
}
