package test.java;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import main.java.memoranda.ui.SetCredDialog;
import main.java.memoranda.util.Util;

public class GitCredTest {


	/**
	 * Checks to see if authencoded.txt exists and valid
	 * @throws IOException
	 */
	@Test
	public void CheckGitCredentials() throws IOException {
    	URL url = new URL("https://api.github.com/repos/ser316asu-2018/Ahrensburg");
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
        String userCredentials = "api-test-user-123:test123";
    	String content = "Basic " + java.util.Base64.getEncoder().encodeToString(userCredentials.getBytes());
        con.setRequestProperty ("Authorization", content);
        assertEquals(con.getHeaderField("x-ratelimit-limit"), "5000");
	}

}
