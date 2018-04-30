
package main.java.memoranda.util;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.ITask;
import main.java.memoranda.date.CalendarDate;

/**
 * Display pull requests and update JcomboBox in PullReq page.
 * 
 * @author NergaL
 *
 */

public class PullRequestGenerator {

    private PullRequestsClass prc;
    protected static Vector<PullRequest> allPulls = null;
    private static Vector<PullRequestsClass> sprints = new Vector<>();

    public PullRequestGenerator(ITask newTask, CalendarDate cdStart,
            CalendarDate cdEnd) {
        prc = new PullRequestsClass(newTask, cdStart, cdEnd);
        sprints.add(prc);

    }

    /**
     * Creates an HTML "agenda" which contains a table of pull requests.
     * TODO Change agenda to something more accurate.
     * @return An HTML string to display the table of pull requests.
     */
    public static String getAgenda() {

        allPulls = CurrentProject.getPullRequestList().getAllPullRequests();
        String header = "<html><body><h2>Pull Requests:</h2><table style=\"width:100%\">";

        String s = "<th>ID</th>"
                + "<th>TotalNumOfPull</th>"
                + "<th>GitHub UserName</th>"
                + "<th>Branch</th>"
                + "<th>Base</th>"
                +"<th>isMerged</th>"
                + "<th>Merged By</th>"
                + "<th>Date</th>"
                + "<th>Sprint</th>";

        for (int index = 0; index < allPulls.size(); index++) {
            PullRequest pr = allPulls.elementAt(index);
            s += "<tr>";
            s += "<td><center>" + pr.get_id() + "</center></td>"
                    + "<td><center>" + pr.get_number() + "</center></td>"
                    + "<td><center>" + pr.get_user() + "</center></td>"
                    + "<td><center>" + pr.get_head() + "</center></td>";
            if (pr.get_base().contains("master")) {
                s += "<td><font color=\"red\"><center>" + pr.get_base()
                        + "</center></font></td>";
            } else {
                s += "<td><center>" + pr.get_base() + "</center></td>";
            }
            s += "<td><center>" + pr.is_merged() + "</center></td>";
            s += "<td><center>" + pr.get_mergedBy() + "</center></td>";
            s += "<td><center>" + pr.get_createdAt() + "</center></td>";
            String withinSprint = "Not within sprint" ;
            Date currentPullDate = pr.get_createdAt();
            Collection<ITask> allSprints = CurrentProject.getTaskList()
                    .getTopLevelTasks();
            for (ITask singleSprint : allSprints) {
                Date start = singleSprint.getStartDate().getDate();
                Date end = singleSprint.getEndDate().getDate();
                if (currentPullDate.after(start)
                        && currentPullDate.before(end)) {
                    withinSprint = singleSprint.getText();
                    break;
                }
            }
            s += "<td><center>" + withinSprint + "</center></td>";
            s += "</tr>";
        }

        String footer = "</table></body></html>";
        return header + s + footer;
    }

    // Inner class which describes every input in JComboBox
    public class PullRequestsClass {

        ITask newTask;
        CalendarDate cdStart;
        CalendarDate cdEnd;

        public PullRequestsClass(ITask newTask, CalendarDate cdStart,
                CalendarDate cdEnd) {
            this.newTask = newTask;
            this.cdStart = cdStart;
            this.cdEnd = cdEnd;
        }

        public ITask getTask() {
            return newTask;
        }

        public CalendarDate getCdStart() {
            return cdStart;
        }

        public CalendarDate getCdEnd() {
            return cdEnd;
        }

    }

}
