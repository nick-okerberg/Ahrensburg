package main.java.memoranda.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;



public class JsonApiClass {
	
	Deque<JsonContributorClass> JsonCon;
	Deque<JsonCommitClass> JsonCom;
	
	private URL url;
	private URL urlCon;	
	private URL urlCom;	
		
public JsonApiClass(URL url) {
			
	this.url = url;
	JsonCon = new LinkedList<>();	
	JsonCom = new LinkedList<>();
}


public void manage() {
		
	try 
	{	
		URLConnection conn = url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		JSONObject JO = new JSONObject(new JSONTokener(br));
		
		this.urlCon = new URL(JO.getString("contributors_url"));
		this.urlCom = new URL(JO.getString("commits_url"));
		
		saveInFile(this.urlCon,this.urlCom);
		ReadConFileContent();
		ReadComFileContent();
	}
	 
	catch (Exception e) 
	{
		System.out.println(e.getMessage());
	}
}
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
		
		
			JsonContributorClass JCC = new JsonContributorClass(login, id, avatar_url, gravatar_id, Url, html_url, followers_url,
				following_url, gists_url, starred_url, subscriptions_url, organizations_url, repos_url
				, events_url, received_events_url, type, site_admin, contributions);
		
			JsonCon.add(JCC);
		}
	
	}
	
	catch (Exception e) 
	{
		System.out.println(e.getMessage());
	}
	
	
}
public Deque<JsonContributorClass> getCondata(){
	
	return this.JsonCon;
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
			
			JsonCommitClass JCC = 
					new JsonCommitClass(sha, comLogin, comType, autLogin, autType, message, autName, autEmail, comName, comEmail, reason, comId, autId, comSiteAdmin, autSiteAdmin, verified);
			JsonCom.add(JCC);
		
		}
	
	
	
	}
	catch (Exception e)
	{
		System.out.println(e.getMessage());
	}
	
}

public Deque<JsonCommitClass> getComdata(){
	
	return this.JsonCom;
}
	




}
