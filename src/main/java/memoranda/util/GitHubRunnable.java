package main.java.memoranda.util;

import java.io.IOException;
import java.net.URL;
import java.util.List;

<<<<<<< Updated upstream
import javax.swing.JOptionPane;

=======
import main.java.memoranda.BranchList;
>>>>>>> Stashed changes
import main.java.memoranda.CommitList;
import main.java.memoranda.ContributorList;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.Project;
import main.java.memoranda.PullRequestList;
import main.java.memoranda.ui.Loading;

import org.json.JSONException;


public class GitHubRunnable implements Runnable {
    String repo;
    Storage storage;
    Project project;
    
    public GitHubRunnable(String gitHubRepo, Storage store, Project prj) {
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
        
        CommitList cl = storage.openCommitList(project);
        for (int i = 0; i < commits.size(); i++) {
            cl.addCommit(commits.get(i));
        }
        storage.storeCommitList(cl, project);
        
        // Build pull requests
        List<PullRequest> pullRequests = jac.getPullRequests();
        PullRequestList prl = storage.openPullRequestList(project);
        for (int i = 0; i < pullRequests.size(); i++) {
            prl.addPullRequest(pullRequests.get(i));
        }
        storage.storePullRequestList(prl, project);
        
        // Build Contributors
        List<Contributor> contributors = jac.getContributors();
        ContributorList contribList = storage.openContributorList(project);
        for (int i = 0; i < contributors.size(); i++) {
            contribList.addContributor(contributors.get(i));
        }
        storage.storeContributorList(contribList, project);
        
        // Build Branches
        List<Branch> branches = jac.getBranches();
        BranchList branchList = storage.openBranchList(project);
        for (int i = 0; i < branches.size(); i++) {
            branchList.addBranch(branches.get(i));
        }
        storage.storeBranchList(branchList, project);
    }
}
