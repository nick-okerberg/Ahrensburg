package test.java;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Deque;

import org.json.JSONException;
import org.junit.Test;

import main.java.memoranda.util.JsonApiClass;
import main.java.memoranda.util.Commit;
import main.java.memoranda.util.Contributor;

public class JsonApiClassTest {
  
  // TODO add real tests

  /**
   * This is just to play around with the methods.
 * @throws JSONException 
   */
  @Test
  public void test() throws JSONException {
    JsonApiClass jac = null;
    try {
      jac = new JsonApiClass("https://api.github.com/repos/ser316asu-2018/Ahrensburg");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    Deque<Commit> commits = null;
    commits = jac.getCommits();
    System.out.println("Found " + commits.size() + " commits.");
    System.out.println("JAC ignored " +jac.getIgnoredCount() + " commits");
    Deque<Contributor> contributors = jac.getContributors();
    System.out.println("Found " + contributors.size() + " contributors.");
    while (commits.peek() != null) {
      Commit commit = commits.pop();
      System.out.println("Commit sha: "+commit.getSha());
      System.out.println("\tTotal LOC: " + commit.getTotalLoc());
      //System.out.println("Message: " +commit.getMessage());
    }
    while (contributors.peek() != null) {
      Contributor cb = contributors.pop();
      System.out.println("Contributor - Name: "+cb.getName() +" Login: " + cb.getLogin());
      //System.out.println("Message: " +commit.getMessage());
    }
    System.out.println("Made " + jac.getApiCallCount() + " API calls.");
  }

}
