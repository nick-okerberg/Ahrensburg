/**
 * ProjectImpl.java
 * Created on 11.02.2003, 23:06:22 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;

import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.date.CurrentDate;
import main.java.memoranda.util.Commit;
import main.java.memoranda.util.Contributor;
import main.java.memoranda.util.JsonApiClass;
import main.java.memoranda.util.PullRequest;
import main.java.memoranda.util.Util;
import nu.xom.Attribute;
import nu.xom.Element;

/**
 * Default implementation of Project interface
 */
/*$Id: ProjectImpl.java,v 1.7 2004/11/22 10:02:37 alexeya Exp $*/
public class ProjectImpl implements Project {

    private Element _root = null;
    private JsonApiClass JAC;

    /**
     * Constructor for ProjectImpl.
     */
    public ProjectImpl(Element root) {        
        _root = root;
    }

	public String getNames() {
        Attribute ta = _root.getAttribute("names");
        if (ta != null)
            return ta.getValue();
        return "";
	}

	public String getGitNames() {
        Attribute ta = _root.getAttribute("gitnames");
        if (ta != null)
            return ta.getValue();
        return "";
	}
	
	/**
	 * US35 implementation. 
	 * Returns the GitHub owner/repo information for a specific project. 
	 * 
	 * @return The github owner/repo string for a project. 
	 */
	public String getGitHubRepoName() {
        Attribute ta = _root.getAttribute("Repo");
        if (ta != null)
            return ta.getValue();
        return "";
	}
	
	/**
	 * US37 implementation
	 * Returns the JsonApiClass object back that this project uses. 
	 * Ensures that duplicate API calls are not made for this US37 implementation.
	 * 
	 * @return The JsonApiClass object for this project. 
	 */
	public JsonApiClass getProjectJsonApiClass() {
		return JAC;
	}

	/**
     * @see main.java.memoranda.Project#getID()
     */
    public String getID() {
        return _root.getAttribute("id").getValue();
    }

    /**
     * @see main.java.memoranda.Project#getStartDate()
     */
    public CalendarDate getStartDate() {
        Attribute d = _root.getAttribute("startDate");
        if (d == null) return null;
        return new CalendarDate(d.getValue());        
    }

    /**
     * @see main.java.memoranda.Project#setStartDate(net.sf.memoranda.util.CalendarDate)
     */
    public void setStartDate(CalendarDate date) {
        if (date != null)
            setAttr("startDate", date.toString());
    }

    /**
     * @see main.java.memoranda.Project#getEndDate()
     */
    public CalendarDate getEndDate() {
        Attribute d = _root.getAttribute("endDate");
        if (d == null) return null;
        return new CalendarDate(d.getValue());
    }

    /**
     * @see main.java.memoranda.Project#setEndDate(net.sf.memoranda.util.CalendarDate)
     */
    public void setEndDate(CalendarDate date) {
        if (date != null)
            setAttr("endDate", date.toString());
        else if (_root.getAttribute("endDate") != null)
            setAttr("endDate", null);
    }

    /**
     * @see main.java.memoranda.Project#getStatus()
     */
    public int getStatus() {
        if (isFrozen())
            return Project.FROZEN;
        CalendarDate today = CurrentDate.get();
        CalendarDate prStart = getStartDate();
        CalendarDate prEnd = getEndDate();
        if (prEnd == null) {
            if (today.before(prStart))
                return Project.SCHEDULED;
            else
                return Project.ACTIVE;                
        }    
        if (today.inPeriod(prStart, prEnd))
            return Project.ACTIVE;
        else if (today.after(prEnd)) {
            //if (getProgress() == 100)
                return Project.COMPLETED;
            /*else
                return Project.FAILED;*/
        }
        else
            return Project.SCHEDULED;
    }

    private boolean isFrozen() {
        return _root.getAttribute("frozen") != null;
    }

   
    /*public int getProgress() {
        Vector v = getAllTasks();
        if (v.size() == 0) return 0;
        int p = 0;
        for (Enumeration en = v.elements(); en.hasMoreElements();) {
          Task t = (Task) en.nextElement();
          p += t.getProgress();
        }
        return (p*100)/(v.size()*100);
    }*/
  
    
    /**
     * @see main.java.memoranda.Project#freeze()
     */
    public void freeze() {
        _root.addAttribute(new Attribute("frozen", "yes"));
    }

    /**
     * @see main.java.memoranda.Project#unfreeze()
     */
    public void unfreeze() {
        if (this.isFrozen())
            _root.removeAttribute(new Attribute("frozen", "yes"));
    }
    
    /**
     * @see main.java.memoranda.Project#getTitle()
     */
    public String getTitle() {
        Attribute ta = _root.getAttribute("title");
        if (ta != null)
            return ta.getValue();
        return "";
    }
    /**
     * @see main.java.memoranda.Project#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
        setAttr("title", title);
    }
    

	public void setNames(String projectTitle) {
		//List<TeamMember> result = TeamMember.teamMemberList(CurrentProject.get().getTitle());
//		/System.out.println(result.toString() + "LOOOOOKK HEEEEERRREEEEE");
		String listOfNames = "";
		String listOfGitNames = "";
		for(int i = 0; i < TeamMember.teamMemberList().size(); i++) {
//			if(i % 2 == 0) {
//			}
			if(projectTitle.equals(TeamMember.teamMemberList().get(i).getProject())) {
				listOfNames += TeamMember.teamMemberList().get(i).getName() + ",";
				listOfGitNames += TeamMember.teamMemberList().get(i).getGithubUsername() + ",";
			}
			
		}
		setAttr("names", listOfNames);
		setAttr("gitnames", listOfGitNames);
		
	}

	public void addMember(String name, String username) {
        Attribute names = _root.getAttribute("names");
        Attribute gitNames = _root.getAttribute("gitnames");

        if(names.getValue().equals("") && gitNames.getValue().equals("")) { //0 elements
            names.setValue(name);
            gitNames.setValue(username);
        }
        else { //if((!names.getValue().equals("") && !gitnames.getValue().equals("")) && checkChar(names.getValue()) == 1 && checkChar(gitnames.getValue()) == 1 ) { //1 element
            String temp1 = names.getValue();
            String temp2 = gitNames.getValue();
            names.setValue(temp1 + "," + name);
            gitNames.setValue(temp2 + "," + username);
        }
    }
    public boolean deleteMember(String username) {
        //""
        //"Ovadia Shalom"
        //"Ovadia Shalom,Sean Rogers"

        Attribute names = _root.getAttribute("names");
        Attribute gitNames = _root.getAttribute("gitnames");
        if(!gitNames.getValue().equals("") && gitNames.getValue().contains(username)) {
            String nameArray[] = names.getValue().split(",");
            String[] userNameArray = gitNames.getValue().split(",");
            if(nameArray.length == 1) {
                names.setValue("");
                gitNames.setValue("");
            }
            else if(nameArray.length >=1) {
                int counter = 0;
                String[] newNameArray = new String[nameArray.length-1];
                String[] newUsernameArray = new String[userNameArray.length-1];
                for (int i = 0; i < nameArray.length; i++) {
                    if(!userNameArray[i].equals(username)) {
                        newNameArray[counter] = nameArray[i];
                        newUsernameArray[counter] = userNameArray[i];
                        counter++;
                    }
                }
                String joined1 = String.join(",", newNameArray);
                String joined2 = String.join(",", newUsernameArray);
                names.setValue(joined1);
                gitNames.setValue(joined2);

            }

            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * US37 implementation for building out the Commit objects when the "RefreshCommits" button is pressed.
     * The Commit objects are built using data from the GitHub API. 
     * 
     * @param repo The GitHub repository name (in user/name) format. 
     */
    public void addCommitData(String repo) {
        try {
            int code = 0;
        	URL url = new URL("https://github.com/"+ repo);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            code = huc.getResponseCode();
            JAC = new JsonApiClass(new URL("https://api.github.com/repos/" + repo), true);
            List<Commit> commits = JAC.getCommitsArrLst();
            CommitList cl = CurrentProject.getCommitList();
            for (int i = 0; i < commits.size(); i++) {
                cl.addCommit(commits.get(i));
            }
            
            // TODO probably need to move this to addPullRequest or something like that
            List<PullRequest> pullRequests = JAC.getPullRequests();
            PullRequestList prl = CurrentProject.getPullRequestList();
            for (int i = 0; i < pullRequests.size(); i++) {
                prl.addPullRequest(pullRequests.get(i));
            }
            
        }
        catch (Exception ex) {
            Util.debug(ex.getMessage());
        }
    }
    
    /**
     * US35 implementation for setting a GitHub owner/repository name for a project. 
     * Only one repo name is allowed for a single project.  If there was an existing
     * project name, this functionality will overwrite the existing one with the
     * new one provided from the input string.  
     * @throws JSONException 
     * 
     * @params repo The input string that represents the repository name.  Should
     * already be in format "owner/repository".  
     */
	public void addRepoName(String repo) throws RuntimeException, JSONException{
        Attribute repoName = _root.getAttribute("Repo");
        
        // Check for emtpy string
        if (repo.equals(""))
          throw new RuntimeException("Repo name must not be empty");
        
        // Next check if the repo given matches the correct format "owner/repo"
        if (! repo.matches("^\\w+-?\\w+(?!-)/\\w+-?\\w+(?!-)$"))
            throw new RuntimeException("Repo name invalid");
        
        // Finally check gitHub to see if that repo actually exists.
        int code=0;
        try {
          URL url = new URL("https://github.com/"+repo);
          HttpURLConnection huc = (HttpURLConnection) url.openConnection();
          huc.setRequestMethod("GET");
          huc.connect();
          code = huc.getResponseCode();
         JAC = new JsonApiClass(new URL("https://api.github.com/repos/"+repo));
         //JAC.manage();
         
        }catch (Exception ex) {
          Util.debug(ex.getMessage());
        }
        if (code == 404)
          throw new RuntimeException("Repo: "+repo+" is not found on GitHub");
        
        /*
         * Sets the value of the input repo string that should already be in owner/repository format. 
         * Overwrites any existing value. 
         * Only one repo is allowed per project. 
         */
        //System.out.println("DEBUG: repo input string == " + repo);
        repoName.setValue(repo);
        
        /* US 41 changes
        First we need to get rid of existing names
        TODO this should probably be done differently*/
        Attribute names = _root.getAttribute("names");
        Attribute gitNames = _root.getAttribute("gitnames");
        names.setValue("");
        gitNames.setValue("");
        
        /* next we need to auto import the contributor names as soon as the
        user enters the repo. */
        JsonApiClass jac = null;
        try {
          jac = new JsonApiClass(getGitHubRepoApiUrl());
          jac.refreshContributors();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        Vector<Contributor> contributors = jac.getContributors();
        for (int i = 0; i < contributors.size(); i++) {
            Contributor cb = contributors.get(i);
            this.addMember(cb.getName(), cb.getLogin());
        }
        /* End US41 changes */
	} 
	// End of US35 implementation for GitHub Repo Name. 
	
	
	// US35.2 Implementation for GitHubURL
	/**
	 * Changes the Repo Name to a URL and returns a URL object
	 * @return the URL of the GitHub Repo
	 */
	public String getGitHubRepoUrl() {
	  String gitRepoUrl ="https://github.com/"+this.getGitHubRepoName(); 
    return gitRepoUrl;	
	}
	
  // US41 Implementation for GitHubAPIURL
  /**
   * Changes the Repo Name to a URL and returns a URL object for the GitHub api
   * @return the URL of the GitHub Repo API
   */
  public String getGitHubRepoApiUrl() {
    String gitApiUrl ="https://api.github.com/repos/"+this.getGitHubRepoName(); 
    return gitApiUrl;  
  }
  // End US41 changes
	

    public int checkChar(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == ',') {
                count++;
            }
        }
        return count;
    }

	public void setGitName(String newName) {
		setAttr("gitnames", newName);
	}
	public void setName(String newName) {
		setAttr("names", newName);
	}

	private void setAttr(String name, String value) {
        Attribute a = _root.getAttribute(name);
        if (a == null) {
            if (value != null)
             _root.addAttribute(new Attribute(name, value));
        }
        else if (value != null)        
            a.setValue(value);
        else 
            _root.removeAttribute(a);
    }

	public String getDescription() {
    	Element thisElement = _root.getFirstChildElement("description");
    	if (thisElement == null) {
    		return null;
    	}
    	else {
       		return thisElement.getValue();
    	}
    }

    public void setDescription(String s) {
    	Element desc = _root.getFirstChildElement("description");
    	if (desc == null) {
        	desc = new Element("description");
            desc.appendChild(s);
            _root.appendChild(desc);    	
    	}
    	else {
            desc.removeChildren();
            desc.appendChild(s); 
           
    	}
    }

        
    /**
     * @see net.sf.memoranda.Project#getTaskList()
     */
    /*public TaskList getTaskList() {
        return CurrentStorage.get().openTaskList(this);
    }*/
    /**
     * @see net.sf.memoranda.Project#getNoteList()
     */
    /*public NoteList getNoteList() {
        return CurrentStorage.get().openNoteList(this);
    }*/
    /**
     * @see net.sf.memoranda.Project#getResourcesList()
     */
    /*public ResourcesList getResourcesList() {
        return CurrentStorage.get().openResourcesList(this);
    }*/
}
