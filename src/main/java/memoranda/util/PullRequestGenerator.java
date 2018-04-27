
package main.java.memoranda.util;

import java.util.Vector;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.ITask;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.ui.PullRequestPanel;
/**
 * Display pull requests and update JcomboBox in PullReq page
 * @author NergaL
 *
 */

public class PullRequestGenerator {
    
    private PullRequestsClass prc;
    protected static Vector<PullRequest> allPulls = null;
    private static Vector<PullRequestsClass> sprints = new Vector<>();
  
  public PullRequestGenerator(ITask newTask, CalendarDate cdStart,CalendarDate cdEnd){
     prc =  new PullRequestsClass( newTask,  cdStart, cdEnd);
     sprints.add(prc);
     
  }
  
  //Generate the html for pullReq page
  public static String getAgenda() {
      
        
        allPulls = CurrentProject.getPullRequestList().getAllPullRequests();
        String header = "<html><body><h2>Pull Requests:</h2><table style=\"width:100%\">";

        String s = "<th>ID</th><th>TotalNumOfPull</th><th>GitHub UserName</th>" + 
                                "<th>Branch</th><th>Base</th><th>Date</th>";
      
        for(int index = 0; index < allPulls.size(); index++ ) {
            s += "<tr><td><center>"+ allPulls.elementAt(index).get_id() +"</center></td><td><center>" + allPulls.elementAt(index).get_number()+ 
                 "</center></td><td><center>" + allPulls.elementAt(index).get_user()+ "</center></td>" +
                 "</center><td><center>" + allPulls.elementAt(index).get_head() + "</center></td>";
         if(allPulls.elementAt(index).get_base().contains("master")) {
                s +=  "<td><font color=\"red\"><center>"+ allPulls.elementAt(index).get_base()+"</center></font></td>";
            }
         else {
                s +=  "<td><center>"+ allPulls.elementAt(index).get_base()+"</center></td>";
         }
               s += "<td><center>"+ allPulls.elementAt(index).get_createdAt() + "</center></td></tr>"; 
       }
           
        String footer = "</table></body></html>";
        return header + s +  footer;
    }
    //Inner class which describes every input in JComboBox
    public class PullRequestsClass{
        
        ITask newTask;
        CalendarDate cdStart;
        CalendarDate cdEnd;
        
        public PullRequestsClass(ITask newTask,
                 CalendarDate cdStart,CalendarDate cdEnd) {
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
