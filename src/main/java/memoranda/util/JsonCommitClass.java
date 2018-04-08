package main.java.memoranda.util;

public class JsonCommitClass {

	
	String sha, comLogin, comType, autLogin, autType, message, autName, autEmail, comName, comEmail, reason;
	int comId, autId;
	boolean comSiteAdmin, autSiteAdmin, verified;
	
	
	
	public JsonCommitClass(String sha, String comLogin, String comType, String autLogin, String autType, String message,
			String autName, String autEmail, String comName, String comEmail, String reason, int comId, int autId,
			boolean comSiteAdmin, boolean autSiteAdmin, boolean verified) {
		super();
		this.sha = sha;
		this.comLogin = comLogin;
		this.comType = comType;
		this.autLogin = autLogin;
		this.autType = autType;
		this.message = message;
		this.autName = autName;
		this.autEmail = autEmail;
		this.comName = comName;
		this.comEmail = comEmail;
		this.reason = reason;
		this.comId = comId;
		this.autId = autId;
		this.comSiteAdmin = comSiteAdmin;
		this.autSiteAdmin = autSiteAdmin;
		this.verified = verified;
	}
	public String getSha() {
		return sha;
	}
	public void setSha(String sha) {
		this.sha = sha;
	}
	public String getComLogin() {
		return comLogin;
	}
	public void setComLogin(String comLogin) {
		this.comLogin = comLogin;
	}
	public String getComType() {
		return comType;
	}
	public void setComType(String comType) {
		this.comType = comType;
	}
	public String getAutLogin() {
		return autLogin;
	}
	public void setAutLogin(String autLogin) {
		this.autLogin = autLogin;
	}
	public String getAutType() {
		return autType;
	}
	public void setAutType(String autType) {
		this.autType = autType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAutName() {
		return autName;
	}
	public void setAutName(String autName) {
		this.autName = autName;
	}
	public String getAutEmail() {
		return autEmail;
	}
	public void setAutEmail(String autEmail) {
		this.autEmail = autEmail;
	}
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public String getComEmail() {
		return comEmail;
	}
	public void setComEmail(String comEmail) {
		this.comEmail = comEmail;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getComId() {
		return comId;
	}
	public void setComId(int comId) {
		this.comId = comId;
	}
	public int getAutId() {
		return autId;
	}
	public void setAutId(int autId) {
		this.autId = autId;
	}
	public boolean isComSiteAdmin() {
		return comSiteAdmin;
	}
	public void setComSiteAdmin(boolean comSiteAdmin) {
		this.comSiteAdmin = comSiteAdmin;
	}
	public boolean isAutSiteAdmin() {
		return autSiteAdmin;
	}
	public void setAutSiteAdmin(boolean autSiteAdmin) {
		this.autSiteAdmin = autSiteAdmin;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	} 
	
		
}