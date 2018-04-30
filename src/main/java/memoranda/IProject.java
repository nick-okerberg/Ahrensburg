/**
 * Project.java
 * Created on 11.02.2003, 16:11:47 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda;


import org.json.JSONException;

import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.JsonApiClass;

/**
 * 
 */

/*$Id: Project.java,v 1.5 2004/11/22 10:02:37 alexeya Exp $*/
public interface IProject {
    
    public static final int SCHEDULED = 0;
   
    public static final int ACTIVE = 1;
    
    public static final int COMPLETED = 2;
    
    public static final int FROZEN = 4;
    
    public static final int FAILED = 5;
    
    String getID();
       
    CalendarDate getStartDate();
    void setStartDate(CalendarDate date);
    
    CalendarDate getEndDate();
    void setEndDate(CalendarDate date);
    
    String getTitle();
    void setTitle(String title);
    
    String getNames();
    String getGitNames();

    void addMember(String name, String username);
    boolean deleteMember(String username);
    int checkChar(String s);

    void setNames(String projectTitle);
    void setGitName(String newName);
    void setName(String newName);
    
    // US35 addition for GitHub repo name. 
    void addRepoName(String repo) throws RuntimeException, JSONException;
    String getGitHubRepoName();
    String getGitHubRepoUrl();
    // End of US35 additions.
    
    // US37 additions
    public JsonApiClass getProjectJsonApiClass();
    void addCommitData(String gitHubRepoName); 
    // End of US37 additions
    
    void setDescription(String description);
    String getDescription();
    
    int getStatus();
            
    //int getProgress();
    
    //TaskList getTaskList();
    
    //NoteList getNoteList();
    
    //ResourcesList getResourcesList();
    
    void freeze();
    void unfreeze(); 
    
}
