package main.java.memoranda.util;

import org.json.JSONException;
import org.json.JSONObject;

public class Contributor {
  private String login, avatar_url, name;
	
	public Contributor(String login, String avatar_url, String name) {
		
		this.login = login;
		this.avatar_url = avatar_url;
    this.name = name;
	}
  
  /**
   * Constructor to accept a JSON object and parse out relevant info
   * @param json JSON object in the form of a GitHub API User
   */
  public Contributor (JSONObject json) throws JSONException{
    this.login = json.getString("login");
    this.avatar_url = json.getString("avatar_url");
    try {
      this.name = json.getString("name");
    } catch (JSONException ex) {
      this.name = "null";
    }
  }
  
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public String getName() {
		return name;
	}
	public void setContributions(String name) {
		this.name = name;
	}
	
}
