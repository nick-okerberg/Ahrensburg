
package main.java.memoranda.util;


import java.util.Vector;

import com.sun.jmx.snmp.internal.SnmpSecuritySubSystem;

import nu.xom.Element;
import nu.xom.Elements;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.Project;
import main.java.memoranda.Task;
import main.java.memoranda.date.CalendarDate;

/**
 * Display pull requests and update JcomboBox in PullReq page
 * @author NergaL
 *
 */

public class PullRequestGenerator {
    
    private PullRequestsClass prc;
    protected static Vector<PullRequest> allPulls = null;
    private static Vector<PullRequestsClass> sprints = new Vector<>();
    private static Vector<PullRequestsClass> savedSprints = new Vector<>();
 
    public PullRequestGenerator() {}
    public PullRequestGenerator(Task newTask, CalendarDate cdStart,CalendarDate cdEnd){
     prc =  new PullRequestsClass( newTask,  cdStart, cdEnd);
     sprints.add(prc);
     
  }
  public static void getSavedSprints(Element e, Project prj ) {
      
      Elements els = e.getChildElements("task");
      for (int i = 0; i < els.size(); i++) {
          Element el = els.get(i);
          if(savedSprints.size() == 0) {
              savedSprints.add( new PullRequestGenerator()
                          .new PullRequestsClass(prj.getTitle(), el.getValue(),
                                  el.getAttributeValue("startDate"),el.getAttributeValue("endDate")));
          }
          
          for( int j = 0; j < savedSprints.size(); j++ ) {
            
              
         //      && !savedSprints.elementAt(j).getSavedSprintName().equals(el.getValue())
             if( !savedSprints.elementAt(j).getSavedProject().equals(prj.getTitle()))  
                  {
               
             
            
                  savedSprints.add( new PullRequestGenerator()
                          .new PullRequestsClass(prj.getTitle(), el.getValue(),
                                  el.getAttributeValue("startDate"),el.getAttributeValue("endDate")));
                  
              
              
                  
              }
              
                 
            }
         
      }
     System.out.println(savedSprints.size()); 
  }
  
 public static Vector<PullRequestsClass> getSprints(){
     return sprints;
 }
 public static Vector<PullRequestsClass> getSavedSprints(){
     return savedSprints;
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
        
        private Task newTask;
        private CalendarDate cdStart;
        private CalendarDate cdEnd;
        private String savedEndDate;
        private String savedStartDate;
        private String savedProject;
        private String savedSprintName;
        
        public PullRequestsClass(String savedProject,
                String savedSprintName, String savedStartDate,String savedEndDate ) {
           this.savedProject = savedProject;
           this.savedSprintName = savedSprintName;
           this.savedStartDate = savedStartDate;
           this.savedEndDate = savedEndDate;
       }
        
       public PullRequestsClass(Task newTask,
                 CalendarDate cdStart,CalendarDate cdEnd) {
            this.newTask = newTask;
            this.cdStart = cdStart;
            this.cdEnd = cdEnd;
        }
        public Task getTask() {
            return newTask;
        }
        public CalendarDate getCdStart() {
            return cdStart;
        }
        public CalendarDate getCdEnd() {
            return cdEnd;
        }
        public String getSavedEndDate() {
            return savedEndDate;
        }
        public String getSavedStartDate() {
            return savedStartDate;
        }
        public String getSavedProject() {
            return savedProject;
        }
        public String getSavedSprintName() {
            return savedSprintName;
        }
    }
 
    
}
