package main.java.memoranda.util;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.JOptionPane;
import main.java.memoranda.IBranchList;
import main.java.memoranda.ICommitList;
import main.java.memoranda.IContributorList;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.IProject;
import main.java.memoranda.IPullRequestList;
import main.java.memoranda.ui.Loading;

import org.json.JSONException;


public class GitHubRunnable implements Runnable {
    String repo;
    IStorage storage;
    IProject project;
    
    public GitHubRunnable(String gitHubRepo, IStorage store, IProject prj) {
        repo = gitHubRepo;
        storage = store;
        project = prj;
    }
    
    /**
     * Builds a JsonApiClass and updates all the current project data.
     */
    public void run() {
        // build JSON API getter
        JsonApiClass jac = null;
        try {
            jac = new JsonApiClass(new URL("https://api.github.com/repos/" + repo), true);
        } catch (JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error downloading Github Repo Info");
            Loading.destroy();
        }
        
        // Build commits
        List<Commit> commits = jac.getCommitsArrLst();
        
        ICommitList cl = storage.openCommitList(project);
        for (int i = 0; i < commits.size(); i++) {
            cl.addCommit(commits.get(i));
        }
        storage.storeCommitList(cl, project);
        
        // Build pull requests
        List<PullRequest> pullRequests = jac.getPullRequests();
        IPullRequestList prl = storage.openPullRequestList(project);
        for (int i = 0; i < pullRequests.size(); i++) {
            prl.addPullRequest(pullRequests.get(i));
        }
        storage.storePullRequestList(prl, project);
        
        // Build Contributors
        List<Contributor> contributors = jac.getContributors();
        IContributorList contribList = storage.openContributorList(project);
        for (int i = 0; i < contributors.size(); i++) {
            contribList.addContributor(contributors.get(i));
        }
        storage.storeContributorList(contribList, project);
        
        // Build Branches
        List<Branch> branches = jac.getBranches();
        IBranchList branchList = storage.openBranchList(project);
        for (int i = 0; i < branches.size(); i++) {
            branchList.addBranch(branches.get(i));
        }
        storage.storeBranchList(branchList, project);
    }
}
