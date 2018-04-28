package test.java;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import main.java.memoranda.util.PullRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;



public class PullRequestTest {
    PullRequest p;
    
    /**
     * Builds a PullRequest Object using default constructor. 
     * Tests getters on the default constructor built object. 
     */
    @Test
    public void PullRequestConstructorDefault() {
        p = new PullRequest();
        assertEquals(p.get_state(),"");
        assertEquals(p.get_title(),"");
        assertEquals(p.get_id(),-1);
        assertEquals(p.get_number(),-1);
        assertEquals(p.get_createdAt(),null);
        assertEquals(p.get_url(),"");
        assertEquals(p.get_user(),"");
        assertEquals(p.get_mergedBy(),"");
        assertEquals(p.get_head(),"");
        assertEquals(p.get_base(),"");
        assertEquals(p.is_merged(),false);
        assertEquals(p.get_reviewers(),new ArrayList<String>());
        assertEquals(p.get_approvedBy(),new ArrayList<String>());
    }
    
    
    
    /**
     * Builds a PullRequest Object using default constructor. 
     * Tests common setters.  
     */
    @Test
    public void PullRequestTestSetters() {
        // New default PullRequest object.
        p = new PullRequest();
        
        /*
         * Test Setters
         */
        p.set_state("Closed");
        p.set_title("Title");
        p.set_id(100);
        p.set_number(10);
        
        // Date object to test. 
        Date d = new Date("01/11/2011 11:11:11 AM");
        p.set_createdAt(d);
        
        p.set_url("http://asu.edu");
        p.set_user("UserName");
        p.set_mergedBy("UserWhoMerged");
        p.set_head("USx");
        p.set_base("dev");
        p.set_merged(true);
        
        // ArrayList for Reviewer names
        ArrayList<String> a1 = new ArrayList<String>();
        a1.add("Reviewer1");
        a1.add("Reviewer2");
        p.set_reviewers(a1);
        
        // ArrayList for Approvers
        ArrayList<String> a2 = new ArrayList<String>();
        a2.add("Approver1");
        a2.add("Approver2");
        p.set_approvedBy(a2);
        
        // Compare 
        assertEquals(p.get_state(),"Closed");
        assertEquals(p.get_title(),"Title");
        assertEquals(p.get_id(),100);
        assertEquals(p.get_number(),10);
        assertEquals(p.get_createdAt(),d);
        assertEquals(p.get_url(),"http://asu.edu");
        assertEquals(p.get_user(),"UserName");
        assertEquals(p.get_mergedBy(),"UserWhoMerged");
        assertEquals(p.get_head(),"USx");
        assertEquals(p.get_base(),"dev");
        assertEquals(p.is_merged(),true);
        assertEquals(p.get_reviewers(),a1);
        assertEquals(p.get_approvedBy(),a2);
    }
    
    
    
    /**
     * Builds a PullRequest object using args, then
     * tests values from getters. 
     */
    @Test
    public void PullRequestConstructorArgs() {
        
        // Build a date object.
        Date d = new Date("01/11/2011 11:11:11 AM");
        
        // ArrayList for Reviewer names
        ArrayList<String> a1 = new ArrayList<String>();
        a1.add("Reviewer1");
        a1.add("Reviewer2");
        
        // ArrayList for Approvers
        ArrayList<String> a2 = new ArrayList<String>();
        a2.add("Approver1");
        a2.add("Approver2");
        
        // Build the object using args. 
        p = new PullRequest("Closed", "Title", 100, 10, d, 
                "http://asu.edu", "UserName", "UserWhoMerged",
                "USx", "dev", true, a1, a2);
        
        // Test
        assertEquals(p.get_state(),"Closed");
        assertEquals(p.get_title(),"Title");
        assertEquals(p.get_id(),100);
        assertEquals(p.get_number(),10);
        assertEquals(p.get_createdAt(),d);
        assertEquals(p.get_url(),"http://asu.edu");
        assertEquals(p.get_user(),"UserName");
        assertEquals(p.get_mergedBy(),"UserWhoMerged");
        assertEquals(p.get_head(),"USx");
        assertEquals(p.get_base(),"dev");
        assertEquals(p.is_merged(),true);
        assertEquals(p.get_reviewers(),a1);
        assertEquals(p.get_approvedBy(),a2);
    }
    
    
    /**
     * Builds a PullRequest object using JSON arg, then
     * tests values from getters. 
     * 
     * @throws JSONException 
     */
    @Test
    public void PullRequestConstructorJson() throws JSONException {
        
        // Define a test string in JSON format. 
        String myJsonStr = 
                "{\r\n" + 
                "  \"url\": \"https://api.github.com/repos/ser316asu-2018/Ahrensburg/pulls/118\",\r\n" + 
                "  \"id\": 182667592,\r\n" + 
                "  \"html_url\": \"https://github.com/ser316asu-2018/Ahrensburg/pull/118\",\r\n" + 
                "  \"number\": 118,\r\n" + 
                "  \"state\": \"open\",\r\n" + 
                "  \"locked\": false,\r\n" + 
                "  \"title\": \"Devtest\",\r\n" + 
                "  \"user\": {\r\n" + 
                "    \"login\": \"DrChunks\",\r\n" + 
                "    \"id\": 22462688,\r\n" + 
                "  },\r\n" + 
                "  \"created_at\": \"2018-04-19T05:11:48Z\",\r\n" + 
                "  \"updated_at\": \"2018-04-19T05:11:48Z\",\r\n" + 
                "  \"requested_reviewers\": [\r\n" + 
                "    {\r\n" + 
                "      \"login\": \"nick-okerberg\",\r\n" + 
                "      \"id\": 13067579,\r\n" + 
                "    },\r\n" + 
                "    {\r\n" + 
                "      \"login\": \"ovidubya\",\r\n" + 
                "      \"id\": 22357156,\r\n" + 
                "    },\r\n" + 
                "    {\r\n" + 
                "      \"login\": \"NergalGivarkes\",\r\n" + 
                "      \"id\": 37256884,\r\n" + 
                "    }\r\n" + 
                "  ],\r\n" + 
                "  \"head\": {\r\n" + 
                "    \"label\": \"ser316asu-2018:devtest\",\r\n" + 
                "    \"ref\": \"devtest\",\r\n" + 
                "  },\r\n" + 
                "  \"base\": {\r\n" + 
                "    \"label\": \"ser316asu-2018:dev\",\r\n" + 
                "    \"ref\": \"dev\",\r\n" + 
                "  },\r\n" + 
                "  \"merged\": false,\r\n" + 
                "  \"merged_by\": null,\r\n" + 
                "}";
        
        // Convert the test string to JSON object. 
        JSONObject j = new JSONObject();
        try {
            j = new JSONObject(myJsonStr);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        
        // Build a date object.
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date d = null;
        try {
            d = sf.parse("2018-04-19T05:11:48Z");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        // ArrayList for Reviewer names
        ArrayList<String> a1 = new ArrayList<String>();
        a1.add("nick-okerberg");
        a1.add("ovidubya");
        a1.add("NergalGivarkes");
        
        // Build the PullRequest Object using the JSONObject as an argument. 
        p = new PullRequest(j);
        
        // Test
        assertEquals(p.get_state(),"open");
        assertEquals(p.get_title(),"Devtest");
        assertEquals(p.get_id(),182667592);
        assertEquals(p.get_number(),118);
        assertEquals(p.get_createdAt(),d);
        assertEquals(p.get_url(),"https://github.com/ser316asu-2018/Ahrensburg/pull/118");
        assertEquals(p.get_user(),"DrChunks");
        assertEquals(p.get_mergedBy(),"null");
        assertEquals(p.get_head(),"devtest");
        assertEquals(p.get_base(),"dev");
        assertEquals(p.is_merged(),false);
        assertEquals(p.get_reviewers(),a1);
        
    }
    
    
    
    /**
     * Builds a PullRequest object using default constructor, then
     * tests the setter for approvedBy, using JSONObject as an argument.
     * 
     * Used the following as a test, which shows the API formatting:
     *      https://developer.github.com/v3/pulls/reviews/
     * . 
     * @throws JSONException 
     */
    @Test
    public void PullRequestSetApprovedBy() throws JSONException {
        
        // Define a test string in JSON format. 
        String myJsonStr = 
                "[\r\n" + 
                "  {\r\n" + 
                "    \"id\": 80,\r\n" + 
                "    \"user\": {\r\n" + 
                "      \"login\": \"octocat\",\r\n" + 
                "      \"id\": 1,\r\n" + 
                "      \"avatar_url\": \"https://github.com/images/error/octocat_happy.gif\",\r\n" + 
                "      \"gravatar_id\": \"\",\r\n" + 
                "      \"url\": \"https://api.github.com/users/octocat\",\r\n" + 
                "      \"html_url\": \"https://github.com/octocat\",\r\n" + 
                "      \"followers_url\": \"https://api.github.com/users/octocat/followers\",\r\n" + 
                "      \"following_url\": \"https://api.github.com/users/octocat/following{/other_user}\",\r\n" + 
                "      \"gists_url\": \"https://api.github.com/users/octocat/gists{/gist_id}\",\r\n" + 
                "      \"starred_url\": \"https://api.github.com/users/octocat/starred{/owner}{/repo}\",\r\n" + 
                "      \"subscriptions_url\": \"https://api.github.com/users/octocat/subscriptions\",\r\n" + 
                "      \"organizations_url\": \"https://api.github.com/users/octocat/orgs\",\r\n" + 
                "      \"repos_url\": \"https://api.github.com/users/octocat/repos\",\r\n" + 
                "      \"events_url\": \"https://api.github.com/users/octocat/events{/privacy}\",\r\n" + 
                "      \"received_events_url\": \"https://api.github.com/users/octocat/received_events\",\r\n" + 
                "      \"type\": \"User\",\r\n" + 
                "      \"site_admin\": false\r\n" + 
                "    },\r\n" + 
                "    \"body\": \"Here is the body for the review.\",\r\n" + 
                "    \"commit_id\": \"ecdd80bb57125d7ba9641ffaa4d7d2c19d3f3091\",\r\n" + 
                "    \"state\": \"APPROVED\",\r\n" + 
                "    \"html_url\": \"https://github.com/octocat/Hello-World/pull/12#pullrequestreview-80\",\r\n" + 
                "    \"pull_request_url\": \"https://api.github.com/repos/octocat/Hello-World/pulls/12\",\r\n" + 
                "    \"_links\": {\r\n" + 
                "      \"html\": {\r\n" + 
                "        \"href\": \"https://github.com/octocat/Hello-World/pull/12#pullrequestreview-80\"\r\n" + 
                "      },\r\n" + 
                "      \"pull_request\": {\r\n" + 
                "        \"href\": \"https://api.github.com/repos/octocat/Hello-World/pulls/12\"\r\n" + 
                "      }\r\n" + 
                "    }\r\n" + 
                "  }\r\n" + 
                "]";

        
        // Convert the test string to JSON Array. 
        JSONArray j = new JSONArray();
        try {
            j = new JSONArray(myJsonStr);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        
        // ArrayList for Approver names
        ArrayList<String> a2 = new ArrayList<String>();
        a2.add("octocat");
        
        // Build the PullRequest Object using the default constructor. 
        p = new PullRequest();
        
        // Use the setter, with JSONObject as an argument.
        p.set_approvedBy(j);
        
        // Test
        assertEquals(p.get_approvedBy(),a2);
        
    }
    
} // End of Class
