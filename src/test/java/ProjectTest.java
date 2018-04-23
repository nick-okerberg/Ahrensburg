package test.java;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.json.JSONException;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import main.java.memoranda.Project;
import main.java.memoranda.ProjectImpl;
import main.java.memoranda.util.JsonApiClass;
import main.java.memoranda.util.Util;
import nu.xom.Attribute;
import nu.xom.Element;

public class ProjectTest {
  Project prj;
  
  /**
   * Builds a JsonApiClass based on Jordan Wine's SER316 repo to use to test.
   */
  @BeforeClass
  public static void authSetup() {
      try {
          new File(Util.getEnvDir()).mkdirs();
          File file = new File(Util.getEnvDir()+"authencoded.txt");
          FileWriter writer = new FileWriter(file);
          String userCredentials = "api-test-user-123:test123";
          String basicAuth = "Basic " + java.util.Base64.getEncoder().encodeToString(userCredentials.getBytes());
          writer.write(basicAuth);
          writer.close();
      } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
  }
  
  @Before
  public void setup() {
    Element el = new Element("project");
    el.addAttribute(new Attribute("id", "1"));
    
    // US35 added Repo attribute. 
    el.addAttribute(new Attribute("Repo", ""));
    
    el.addAttribute(new Attribute("names", ""));
    el.addAttribute(new Attribute("gitnames", ""));
    prj = new ProjectImpl(el);
  }

  /**
   * Test to make sure addRepoName requires a non-empty string.
 * @throws JSONException 
 * @throws RuntimeException 
   */
  @Test (expected=java.lang.RuntimeException.class)
  public void GitRepoNameShouldNotAcceptEmptyString() throws RuntimeException, JSONException {
    prj.addRepoName("");
  }
  
  /**
   * Tests to make sure that addRepoName can only accept repos that
   * are formatted correctly (ie. hello/world)
 * @throws JSONException 
 * @throws RuntimeException 
   */
  @Test (expected=java.lang.RuntimeException.class)
  public void GitRepoShouldFailIfRepoInvalidFormat() throws RuntimeException, JSONException {
    prj.addRepoName("justowner");
  }
  
  /**
   * Tests to make sure that addRepoName can only accept repos that
   * are formatted correctly (ie. hello/world)
 * @throws JSONException 
 * @throws RuntimeException 
   */
  @Test (expected=java.lang.RuntimeException.class)
  public void GitRepoShouldFailIfRepoInvalidFormat2() throws RuntimeException, JSONException {
    prj.addRepoName("/");
  }
  
  /**
   * Tests to make sure that addRepoName fails when the repo
   * doesn't exist on gitHub.
 * @throws JSONException 
 * @throws RuntimeException 
   */
  @Test (expected=java.lang.RuntimeException.class)
  public void GitRepoShouldFailWhenRepoDNE() throws RuntimeException, JSONException {
    // We'll pick a repo name that definitely doesn't exist.
    String testRepo= "hello/world";
    prj.addRepoName(testRepo);
  }
  
  /**
   * Tests to make sure that addRepoName adds a valid repo when given
 * @throws JSONException 
 * @throws RuntimeException 
   */
  @Test
  public void GitRepoShouldAcceptGoodRepo() throws RuntimeException, JSONException {
    // We'll pick a repo name that definitely doesn't exist.
    String testRepo= "regexhq/regex-username";
    prj.addRepoName(testRepo);
    assertTrue("name is updated", prj.getGitHubRepoName().equals(testRepo));
  }

}
