/**
 * Storage.java
 * Created on 12.02.2003, 0:58:42 Alex
 * Package: net.sf.memoranda.util
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda.util;

import main.java.memoranda.IBranchList;
import main.java.memoranda.ICommitList;
import main.java.memoranda.IContributorList;
import main.java.memoranda.INote;
import main.java.memoranda.INoteList;
import main.java.memoranda.IProject;
import main.java.memoranda.IPullRequestList;
import main.java.memoranda.IResourcesList;
import main.java.memoranda.ITaskList;
/**
 * 
 */
/*$Id: Storage.java,v 1.4 2004/01/30 12:17:42 alexeya Exp $*/
public interface IStorage {
            
    ITaskList openTaskList(IProject prj);    
    void storeTaskList(ITaskList tl, IProject prj);
    
    INoteList openNoteList(IProject prj);
    void storeNoteList(INoteList nl, IProject prj);
    
    void storeNote(INote note, javax.swing.text.Document doc);    
    javax.swing.text.Document openNote(INote note);
    void removeNote(INote note);
    
    String getNoteURL(INote note);
    
    void openProjectManager();    
    void storeProjectManager();
    
    void openEventsManager();
    void storeEventsManager();
    
    void openMimeTypesList();
    void storeMimeTypesList();
    
    void createProjectStorage(IProject prj);
    void removeProjectStorage(IProject prj);
   
    IResourcesList openResourcesList(IProject prj);
    void storeResourcesList(IResourcesList rl, IProject prj);
    
    ICommitList openCommitList(IProject prj);
    void storeCommitList(ICommitList cl, IProject prj);
    
    IPullRequestList openPullRequestList(IProject prj);
    void storePullRequestList(IPullRequestList prl, IProject prj);
    
    IContributorList openContributorList(IProject prj);
    void storeContributorList(IContributorList contribList, IProject prj);
    
    IBranchList openBranchList(IProject prj);
    void storeBranchList(IBranchList branchList, IProject prj);
    
    void restoreContext();
    void storeContext(); 
       
}
