
package main.java.memoranda.util;

import java.util.Vector;

public class PullRequestGenerator {

    static Vector<PullRequest> allPulls = null;

    
    public static String getAgenda() {
        
        String header = "<html><body><h2>Pull Requests:</h2><table style=\"width:100%\">";

        String s = "<th> ID </th><th> TotalNumOfPull</th><th>GitHub UserName</th><th>Approver</th><th>Most Recent pull</th>" + 
                                "<th>Status</th><th>Branch Name</th><th>Merge Status</th><th>Closed Status</th><th>Open Status</th>";
       
      

        
        String footer = "</table></body></html>";
        
        
        return header + s +  footer;
       
    }
 
    
}
