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
        String content = new String(Files.readAllBytes(Paths.get(Util.getEnvDir()+"authencoded.txt")));
        con.setRequestProperty ("Authorization", content);
        assertEquals(con.getHeaderField("x-ratelimit-limit"), "5000");
	}
	/**
	 * Checks to see if file exists
	 * @throws IOException
	 */
	@Test
	public void CheckAuthFile() throws IOException {
		String fileInfo = new String(Files.readAllBytes(Paths.get(Util.getEnvDir()+"authencoded.txt")));
	}

}
