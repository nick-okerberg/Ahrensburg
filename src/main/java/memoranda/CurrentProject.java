/**
 * CurrentProject.java
 * Created on 13.02.2003, 13:16:52 Alex
 * Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 *
 */
package main.java.memoranda;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

import main.java.memoranda.ui.AppFrame;
import main.java.memoranda.util.Context;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.IStorage;

/**
 *
 */
/*$Id: CurrentProject.java,v 1.6 2005/12/01 08:12:26 alexeya Exp $*/
public class CurrentProject {

    private static IProject _project = null;
    private static ITaskList _tasklist = null;
    private static INoteList _notelist = null;
    private static IResourcesList _resources = null;
    private static ICommitList _commits = null;
    private static IPullRequestList _pullRequests = null;
    private static IContributorList _contributors = null;
    private static IBranchList _branches = null;
    private static Vector projectListeners = new Vector();

        
    static {
        String prjId = (String)Context.get("LAST_OPENED_PROJECT_ID");
        if (prjId == null) {
            prjId = "__default";
            Context.put("LAST_OPENED_PROJECT_ID", prjId);
        }
        //ProjectManager.init();
        _project = ProjectManager.getProject(prjId);
		
		if (_project == null) {
			// alexeya: Fixed bug with NullPointer when LAST_OPENED_PROJECT_ID
			// references to missing project
			_project = ProjectManager.getProject("__default");
			if (_project == null) 
				_project = (IProject)ProjectManager.getActiveProjects().get(0);						
            Context.put("LAST_OPENED_PROJECT_ID", _project.getID());
			
		}		
		
        _tasklist = CurrentStorage.get().openTaskList(_project);
        _notelist = CurrentStorage.get().openNoteList(_project);
        _resources = CurrentStorage.get().openResourcesList(_project);
        _commits = CurrentStorage.get().openCommitList(_project);
        _pullRequests = CurrentStorage.get().openPullRequestList(_project);
        _contributors = CurrentStorage.get().openContributorList(_project);
        _branches = CurrentStorage.get().openBranchList(_project);
        AppFrame.addExitListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();                                               
            }
        });
    }
        

    public static IProject get() {
        return _project;
    }

    public static ITaskList getTaskList() {
            return _tasklist;
    }

    public static INoteList getNoteList() {
            return _notelist;
    }
    
    public static IResourcesList getResourcesList() {
            return _resources;
    }
    
    public static ICommitList getCommitList() {
        // Reopen in case it's been saved since we last got it.
        return CurrentStorage.get().openCommitList(_project);
    }
    
    public static IPullRequestList getPullRequestList() {
        // Reopen in case it's been saved since we last got it.
        return CurrentStorage.get().openPullRequestList(_project);
    }
    
    public static IContributorList getContributorList() {
        // Reopen in case it's been saved since we last got it.
        return CurrentStorage.get().openContributorList(_project);
    }
    
    public static IBranchList getBranchList() {
        // Reopen in case it's been saved since we last got it.
        return CurrentStorage.get().openBranchList(_project);
    }

    public static void set(IProject project) {
        if (project.getID().equals(_project.getID())) return;
        ITaskList newtasklist = CurrentStorage.get().openTaskList(project);
        INoteList newnotelist = CurrentStorage.get().openNoteList(project);
        IResourcesList newresources = CurrentStorage.get().openResourcesList(project);
        ICommitList newcommits = CurrentStorage.get().openCommitList(project);
        IPullRequestList newpullrequests = CurrentStorage.get().openPullRequestList(project);
        IContributorList newcontributors = CurrentStorage.get().openContributorList(project);
        IBranchList newbranches = CurrentStorage.get().openBranchList(project);
        notifyListenersBefore(project, newnotelist, newtasklist, 
                newresources, newcommits, newpullrequests);
        _project = project;
        _tasklist = newtasklist;
        _notelist = newnotelist;
        _resources = newresources;
        _commits = newcommits;
        _pullRequests = newpullrequests;
        _contributors = newcontributors;
        _branches = newbranches;
        notifyListenersAfter();
        Context.put("LAST_OPENED_PROJECT_ID", project.getID());
    }

    public static void addProjectListener(IProjectListener pl) {
        projectListeners.add(pl);
    }

    public static Collection getChangeListeners() {
        return projectListeners;
    }

    private static void notifyListenersBefore(IProject project, INoteList nl, 
            ITaskList tl, IResourcesList rl, ICommitList cl, IPullRequestList prl) {
        for (int i = 0; i < projectListeners.size(); i++) {
            ((IProjectListener)projectListeners.get(i)).projectChange(project, nl, tl, rl, cl, prl);
            /*DEBUGSystem.out.println(projectListeners.get(i));*/
        }
    }
    
    private static void notifyListenersAfter() {
        for (int i = 0; i < projectListeners.size(); i++) {
            ((IProjectListener)projectListeners.get(i)).projectWasChanged();            
        }
    }

    public static void save() {
        IStorage storage = CurrentStorage.get();

        storage.storeNoteList(_notelist, _project);
        storage.storeTaskList(_tasklist, _project); 
        storage.storeResourcesList(_resources, _project);
        /* Storage will be handle by the GitHubRunnable thread that pulls the 
         * data so it doesn't get overwritten. This class can read, just never
         * write this data.
        storage.storeCommitList(_commits, _project);
        storage.storePullRequestList(_pullRequests, _project);
        */
        storage.storeProjectManager();
    }
    
    public static void free() {
        _project = null;
        _tasklist = null;
        _notelist = null;
        _resources = null;
        _commits = null;
        _pullRequests = null;
        _contributors = null;
        _branches = null;
    }
}
