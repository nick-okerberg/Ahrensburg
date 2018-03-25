package main.java.memoranda;

import java.util.ArrayList;
import java.util.List;

/**
 * TeamMember class: Defines a team member. This includes the person's name, GitHub username,
 * and which project they are associated with.  
 * 
 * @author okerberg
 *
 */
public class TeamMember {
	
	/*
	 * Global variables for this class
	 */
	private String name;
	private String githubUsername;
	private String project;
	
	/*
	 * Static variable for this class, List of team member objects.
	 */
	private static List<TeamMember> teamMemberList = new ArrayList<TeamMember>();
	
	
	
	/**
	 * Default constructor
	 */
	public TeamMember() {
		// Set default values
//		this.name = "DEFAULT_NAME";
//		this.githubUsername = "DEFAULT_GITHUB_USERNAME";
//		this.project = "Default project";
//		
//		// Add the team member to the list
//		teamMemberList.add(this);
	}
	
	
	
	/**
	 * Constructor with args
	 */
	public TeamMember(String name, String githubUsername, String project) {
		// Set default values
		this.name = name;
		this.githubUsername = githubUsername;
		this.project = project;
		
		// Add the team member to the list
		teamMemberList.add(this);
	}
	


	/**
	 * Getter for the team member's name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * Setter for the team member's name
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * Getter for the GitHub username.
	 * 
	 * @return the githubUsername
	 */
	public String getGithubUsername() {
		return githubUsername;
	}



	/**
	 * Setter for the GitHub username.
	 * 
	 * @param githubUsername the githubUsername to set
	 */
	public void setGithubUsername(String githubUsername) {
		this.githubUsername = githubUsername;
	}



	/**
	 * Getter for the project that the team member is associated with. 
	 * 
	 * @return the project
	 */
	public String getProject() {
		return project;
	}



	/**
	 * Setter for the project that the team member is associated with. 
	 * 
	 * @param project the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}
	
	
	
	/**
	 * Getter for the entire list of members, regardless of which project they 
	 * are associated with.  
	 * 
	 * @return the teamMemberList
	 */
	public static List<TeamMember> teamMemberList(){
		return teamMemberList;
	}
	
	
	
	/**
	 * Getter for a list of team members associated with a specific project.
	 * 
	 * @param the project that the team members are associated with.
	 * @return the list of team members associated with the given project.
	 */
	public static List<TeamMember> teamMemberList(String p){
		// Resulting list to return. 
		List<TeamMember> result = new ArrayList<TeamMember>();
		
		// Iterate through the entire list of team members. 
		for (int i = 0; i < teamMemberList.size(); i++){
			
			// If the team member's project is the same as the one provided. 
			if (teamMemberList.get(i).getProject().equals(p)) {
				
				// Then add the team member object to the result list. 
				result.add(teamMemberList.get(i));
			} // End if
		} // End for loop. 
		
		return result;
	}
	
	
	
	/**
	 * Print the team member's info. 
	 */
	public String toString() {
		String result = "Full Name: " + name + "\n" + "GitHub username: " + githubUsername + "\n";
		return result;
	}
	
	
	
	/**
	 * Main method, used to test this class. 
	 * 
	 * @param args
	 *
	public static void main(String[] args) {

		// Build 3 team member objects. 
		TeamMember tm1 = new TeamMember("Name1", "ghUser1", "project1");
		TeamMember tm2 = new TeamMember("Name2", "ghUser2", "project1");
		TeamMember tm3 = new TeamMember("Name3", "ghUser3", "project2");
		
		// Test each setter.
		tm1.setName("new Name1");
		tm2.setGithubUsername("new ghUser2");
		tm3.setProject("new project3");
		
		// Iterate through the entire team member list and print contents. 
		System.out.println("----- The entire team member list: -----\n");
		for (int i = 0; i < teamMemberList.size(); i++) {
			System.out.println(teamMemberList.get(i).toString());
		}
		
		// Print only the team members associated with a specific project. 
		String projectToTest = "project2";
		List<TeamMember> l = teamMemberList(projectToTest);
		System.out.println("----- The list of team members associated with " + projectToTest + ": -----\n");
		for (int i = 0; i < l.size(); i++) {
			System.out.println(l.get(i).toString());
		}
		
	} // End of main */

} // End of class