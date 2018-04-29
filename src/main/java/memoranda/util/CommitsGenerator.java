/*
 * AgendaGenerator.java Package: net.sf.memoranda.util Created on 13.01.2004
 * 5:52:54 @author Alex
 */
package main.java.memoranda.util;

import java.util.Collection;
import java.util.List;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.date.CalendarDate;





public class CommitsGenerator {
	public static String getAgenda(CalendarDate date, Collection expandedTasks) {
//		String s = HEADER;
//		s += generateAllProjectsInfo(date, expandedTasks);
//		s += generateEventsInfo(date);
//		s += generateStickers(date);
//		//        /*DEBUG*/System.out.println(s+FOOTER);
//		s += FOOTER;
//		//Util.debug(s);
		//generateCommitDetails();
		String header = "<html><body><h2>All Commits in Scrum Project</h2><table style=\"width:100%\">";

		String headerOfTable = "<th>Author</th><th>Commit Message</th> <th>Date</th><th>URL</th><th>Username</th><th>Addtions</th><th>Deletions</th><th>TotalLOC</th>";
		String hardcodedDataTable = "<tr><td>Ovadia Shalom</td> <td>Added 4 lines of nothing</td> <td>5/1/2019</td> <td>https://test.com</td> <td>ovidubs</td> <td>4</td> <td>0</td> <td>4</td> </tr> <tr> <td>Sean Rogers</td> <td>Added 4 lines of nothing</td> <td>5/1/2019</td> <td>https://test.com</td> <td>smrogers</td> <td>4</td> <td>0</td> <td>4</td> </tr>";
		
		String footer = "</table></body></html>";
		return header + headerOfTable + generateCommitDetails() + footer;
		//return header + headerOfTable + hardcodedDataTable + footer;
	}
	public static String generateCommitDetails() {
		
		// Result to Return
		String result = "";
		
		//System.out.println("test");
		
	    //Element el = new Element("project");
	    //el.addAttribute(new Attribute("id", "1"));
	    
	    // US35 added Repo attribute. 
	    //el.addAttribute(new Attribute("Repo", ""));
	    
	    //el.addAttribute(new Attribute("names", ""));
	    //el.addAttribute(new Attribute("gitnames", ""));
		//Project y = new ProjectImpl(el);
		//Project y = CurrentProject.get();
		//System.out.println("[Debug] Current project: " + y.getTitle());
		//JsonApiClass currentProjectJSON = y.getProjectJsonApiClass();
		//System.out.println("[Debug] Current project json: " + currentProjectJSON);
		
    	// Get all commits from the current project's JsonApiClass. 
		//ArrayList<Commit> listOfAllCommits = currentProjectJSON.getCommitsArrLst();	// old commented out.
    	List<Commit> listOfAllCommits = CurrentProject.getCommitList().getAllCommits();
        // If the Array List is empty, just return.
        if (listOfAllCommits.isEmpty()) {
        	return "";
        }
        
        //System.out.println("[DEBUG]: Commits list size: " + listOfAllCommits.size());
		
		for (int i = 0; i < listOfAllCommits.size(); i++) {
			Commit c = listOfAllCommits.get(i);
			//System.out.println("MESSAGE: " + c.getMessage());
			
			// Get Commit Author Name.
			result = result + "<tr><td>" + c.getAuthorName() + "</td> ";
			
			// Get Commit message.
			result = result + "<td>" + c.getMessage() + "</td> ";
			
			// Get Commit Date String.
			result = result + "<td>" + c.getDateString().substring(0, Math.min(c.getDateString().length(), 10)) + "</td> ";
			
			// Get Commit URL String.
			String urlString=c.getHtmlUrl();
			String shaTrunc = c.getSha().substring(0, Math.min(c.getSha().length(), 7));
			result = result + "<td><a id=\"a_ext\" href=\""+ urlString+"\">" + shaTrunc + "</a></td> ";
			
			// Get GitHub username.
			result = result + "<td>" + c.getAuthorLogin() + "</td> ";
			
			// Get Code additions.
			result = result + "<td>" + c.getAdditions() + "</td> ";
			
			// Get Code deletions.
			result = result + "<td>" + c.getDeletions() + "</td> ";
			
			// Get LOC.
			result = result + "<td>" + c.getTotalLoc() + "</td> </tr> ";
			
		}
		return result;
	}
	
}
