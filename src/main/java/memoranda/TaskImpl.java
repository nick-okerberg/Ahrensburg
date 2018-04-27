/**
 * DefaultTask.java
 * Created on 12.02.2003, 15:30:40 Alex
 * Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda;

import java.util.Collection;
import java.util.Vector;

import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.date.CurrentDate;
import main.java.memoranda.util.Commit;
import main.java.memoranda.util.JsonApiClass;
import java.util.Calendar;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;

/**
 *
 */
/*$Id: TaskImpl.java,v 1.15 2005/12/01 08:12:26 alexeya Exp $*/
public class TaskImpl implements ITask, Comparable {

    private Element _element = null;
    private ITaskList _tl = null;

    /**
     * Constructor for DefaultTask.
     */
    public TaskImpl(Element taskElement, ITaskList tl) {
        _element = taskElement;
        _tl = tl;
    }

    /**
     * getNumberSprintTests
     * 
     * Added as part of US37.  Iterates through commit objects and counts
     * the number of tests from the commit messages. 
     * 
     * @return The number of tests in the sprint task, in String form. 
     */
    public String getNumberSprintTests() {
    	
    	JsonApiClass JAC;			// For Json API call, when the button was pressed. 
    	
    	String resultStr = "None";	// Initialize String result to return. 
    	int resultInt = 0;			// Initialize integer result to 0. 
    	
    	// Get the project for this sprint. 
    	IProject p = this._tl.getProject();
    	//System.out.println("Project Title: " + p.getTitle());
    	
    	// Get the Project's JsonApiClass object, which should contain commit objects. 
    	JAC = p.getProjectJsonApiClass();
    	
    	// Get all commits from the current project's JsonApiClass. 
    	Vector<Commit> myCommits = new Vector<>();
    	try {
        	myCommits = JAC.getCommitsArrLst();
        }
        catch (NullPointerException e) {
        	//System.out.println("No commits for selected sprint.");
        }
    	
    	//System.out.println("myCommits size: " + myCommits.size());
    	
        // If there are no commits, just return. 
        if (myCommits == null) {
        	return resultStr;
        }
        
        // If the Array List is empty, just return.
        if (myCommits.isEmpty()) {
        	return resultStr;
        }
        
        // Iterate through the ArrayList of commits. 
        for (int i = 0; i < myCommits.size(); i++) {
            
        	//System.out.println("Iterating through commits... index = " + i);
        	
        	// Boolean is true when a commit is detected within this Sprint. 
        	boolean isWithinSprint = false;
        	
        	// A single commit object from the ArrayList. 
        	Commit myCommit = myCommits.get(i);
            
        	// Commit date is the same as the start date of the current sprint. 
        	if (myCommit.getDate().equals(this.getStartDate().getDate())) {
        		isWithinSprint = true;
        	}
        	
        	// Commit date is the same as the end date of the current sprint. 
        	else if (myCommit.getDate().equals(this.getEndDate().getDate())) {
        		isWithinSprint = true;
        	}
        	
        	// Commit date is within the start-date and end-date of the current sprint.
        	else if (myCommit.getDate().after(this.getStartDate().getDate()) &&	
        			(myCommit.getDate().before(this.getEndDate().getDate())) ) {
        		isWithinSprint = true;
        	}
        	
        	// Otherwise, leave boolean false.
        	else {
        		isWithinSprint = false;
        	}
        		
        	//System.out.println("... Commit #" + i + ": isWithinSprint? " + isWithinSprint);
        	
        	// If this commit is within this Sprint, then increment the number of tests. 
        	if (isWithinSprint) {
        		resultInt = resultInt + myCommit.getNumTests();
        	}
        	//System.out.println("Iteration " + i + " result counter = " + resultInt);
        		
        } // End for loop.
        
        // Convert the integer result to string. 
       	if (resultInt > 0) {
       		resultStr = Integer.toString(resultInt);
       	}
                
       	// Return the result back, as a string. 
       	//System.out.println("Returning back: " + resultStr);
       	return resultStr;
        
   	} // End of US37 addition. 
    
    public Element getContent() {
        return _element;
    }

    public CalendarDate getStartDate() {
        return new CalendarDate(_element.getAttribute("startDate").getValue());
    }

    public void setStartDate(CalendarDate date) {
           setAttr("startDate", date.toString());
    }

    public CalendarDate getEndDate() {
		String ed = _element.getAttribute("endDate").getValue();
		if (ed != "")
			return new CalendarDate(_element.getAttribute("endDate").getValue());
		ITask parent = this.getParentTask();
		if (parent != null)
			return parent.getEndDate();
		IProject pr = this._tl.getProject();
		if (pr.getEndDate() != null)
			return pr.getEndDate();
		return this.getStartDate();
        
    }

    public void setEndDate(CalendarDate date) {
		if (date == null) {
			setAttr("endDate", "");
		}
		else {
			setAttr("endDate", date.toString());
		}
    }

    public long getEffort() {
    	Attribute attr = _element.getAttribute("effort");
    	if (attr == null) {
    		return 0;
    	}
    	else {
    		try {
        		return Long.parseLong(attr.getValue());
    		}
    		catch (NumberFormatException e) {
    			return 0;
    		}
    	}
    }

    public void setEffort(long effort) {
        setAttr("effort", String.valueOf(effort));
    }
	
	/* 
	 * @see net.sf.memoranda.Task#getParentTask()
	 */
	public ITask getParentTask() {
		Node parentNode = _element.getParent();
    	if (parentNode instanceof Element) {
    	    Element parent = (Element) parentNode;
        	if (parent.getLocalName().equalsIgnoreCase("task")) 
        	    return new TaskImpl(parent, _tl);
    	}
    	return null;
	}
	
	public String getParentId() {
		ITask parent = this.getParentTask();
		if (parent != null)
			return parent.getID();
		return null;
	}

    public String getDescription() {
    	Element thisElement = _element.getFirstChildElement("description");
    	if (thisElement == null) {
    		return null;
    	}
    	else {
       		return thisElement.getValue();
    	}
    }

    public void setDescription(String s) {
    	Element desc = _element.getFirstChildElement("description");
    	if (desc == null) {
        	desc = new Element("description");
            desc.appendChild(s);
            _element.appendChild(desc);    	
    	}
    	else {
            desc.removeChildren();
            desc.appendChild(s);    	
    	}
    }

    /**s
     * @see main.java.memoranda.ITask#getStatus()
     */
    public int getStatus(CalendarDate date) {
        CalendarDate start = getStartDate();
        CalendarDate end = getEndDate();
        if (isFrozen())
            return ITask.FROZEN;
        if (isCompleted())
                return ITask.COMPLETED;
        
		if (date.inPeriod(start, end)) {
            if (date.equals(end))
                return ITask.DEADLINE;
            else
                return ITask.ACTIVE;
        }
		else if(date.before(start)) {
				return ITask.SCHEDULED;
		}
		
		if(start.after(end)) {
			return ITask.ACTIVE;
		}

        return ITask.FAILED;
    }
    /**
     * Method isDependsCompleted.
     * @return boolean
     */
/*
    private boolean isDependsCompleted() {
        Vector v = (Vector) getDependsFrom();
        boolean check = true;
        for (Enumeration en = v.elements(); en.hasMoreElements();) {
            Task t = (Task) en.nextElement();
            if (t.getStatus() != Task.COMPLETED)
                check = false;
        }
        return check;
    }
*/
    private boolean isFrozen() {
        return _element.getAttribute("frozen") != null;
    }

    private boolean isCompleted() {
        return getProgress() == 100;
    }

    /**
     * @see main.java.memoranda.ITask#getID()
     */
    public String getID() {
        return _element.getAttribute("id").getValue();
    }

    /**
     * @see main.java.memoranda.ITask#getText()
     */
    public String getText() {
        return _element.getFirstChildElement("text").getValue();
    }

    public String toString() {
        return getText();
    }
    
    /**
     * @see main.java.memoranda.ITask#setText()
     */
    public void setText(String s) {
        _element.getFirstChildElement("text").removeChildren();
        _element.getFirstChildElement("text").appendChild(s);
    }

    /**
     * @see main.java.memoranda.ITask#freeze()
     */
    public void freeze() {
        setAttr("frozen", "yes");
    }

    /**
     * @see main.java.memoranda.ITask#unfreeze()
     */
    public void unfreeze() {
        if (this.isFrozen())
            _element.removeAttribute(new Attribute("frozen", "yes"));
    }

    /**
     * @see main.java.memoranda.ITask#getDependsFrom()
     */
    public Collection getDependsFrom() {
        Vector v = new Vector();
        Elements deps = _element.getChildElements("dependsFrom");
        for (int i = 0; i < deps.size(); i++) {
            String id = deps.get(i).getAttribute("idRef").getValue();
            ITask t = _tl.getTask(id);
            if (t != null)
                v.add(t);
        }
        return v;
    }
    /**
     * @see main.java.memoranda.ITask#addDependsFrom(main.java.memoranda.ITask)
     */
    public void addDependsFrom(ITask task) {
        Element dep = new Element("dependsFrom");
        dep.addAttribute(new Attribute("idRef", task.getID()));
        _element.appendChild(dep);
    }
    /**
     * @see main.java.memoranda.ITask#removeDependsFrom(main.java.memoranda.ITask)
     */
    public void removeDependsFrom(ITask task) {
        Elements deps = _element.getChildElements("dependsFrom");
        for (int i = 0; i < deps.size(); i++) {
            String id = deps.get(i).getAttribute("idRef").getValue();
            if (id.equals(task.getID())) {
                _element.removeChild(deps.get(i));
                return;
            }
        }
    }
    /**
     * @see main.java.memoranda.ITask#getProgress()
     */
    public int getProgress() {
        return new Integer(_element.getAttribute("progress").getValue()).intValue();
    }
    /**
     * @see main.java.memoranda.ITask#setProgress(int)
     */
    public void setProgress(int p) {
        if ((p >= 0) && (p <= 100))
            setAttr("progress", new Integer(p).toString());
    }
    /**
     * @see main.java.memoranda.ITask#getPriority()
     */
    public int getPriority() {
        Attribute pa = _element.getAttribute("priority");
        if (pa == null)
            return ITask.PRIORITY_NORMAL;
        return new Integer(pa.getValue()).intValue();
    }
    /**
     * @see main.java.memoranda.ITask#setPriority(int)
     */
    public void setPriority(int p) {
        setAttr("priority", String.valueOf(p));
    }
    
    public String getGitMaster() {
    	return _element.getAttribute("gitmaster").toString();
    }
    public void setGitMaster(String newGitMaster) {
    	setAttr("gitmaster", newGitMaster);
    }
    private void setAttr(String a, String value) {
        Attribute attr = _element.getAttribute(a);
        if (attr == null)
           _element.addAttribute(new Attribute(a, value));
        else
            attr.setValue(value);
    }

	/**
	 * A "Task rate" is an informal index of importance of the task
	 * considering priority, number of days to deadline and current 
	 * progress. 
	 * 
	 * rate = (100-progress) / (numOfDays+1) * (priority+1)
	 * @param CalendarDate
	 * @return long
	 */

	private long calcTaskRate(CalendarDate d) {
		Calendar endDateCal = getEndDate().getCalendar();
		Calendar dateCal = d.getCalendar();
		int numOfDays = (endDateCal.get(Calendar.YEAR)*365 + endDateCal.get(Calendar.DAY_OF_YEAR)) - 
						(dateCal.get(Calendar.YEAR)*365 + dateCal.get(Calendar.DAY_OF_YEAR));
		if (numOfDays < 0) return -1; //Something wrong ?
		return (100-getProgress()) / (numOfDays+1) * (getPriority()+1);
	}

    /**
     * @see main.java.memoranda.ITask#getRate()
     */
	 
     public long getRate() {
/*	   Task t = (Task)task;
	   switch (mode) {
		   case BY_IMP_RATE: return -1*calcTaskRate(t, date);
		   case BY_END_DATE: return t.getEndDate().getDate().getTime();
		   case BY_PRIORITY: return 5-t.getPriority();
		   case BY_COMPLETION: return 100-t.getProgress();
	   }
       return -1;
*/
		return -1*calcTaskRate(CurrentDate.get());
	 }
	   
	 /*
	  * Comparable interface
	  */
	  
	 public int compareTo(Object o) {
		 ITask task = (ITask) o;
		 	if(getRate() > task.getRate())
				return 1;
			else if(getRate() < task.getRate())
				return -1;
			else 
				return 0;
	 }
	 
	 public boolean equals(Object o) {
	     return ((o instanceof ITask) && (((ITask)o).getID().equals(this.getID())));
	 }

	/* 
	 * @see net.sf.memoranda.Task#getSubTasks()
	 */
	public Collection getSubTasks() {
		Elements subTasks = _element.getChildElements("task");
            return convertToTaskObjects(subTasks);
	}

	private Collection convertToTaskObjects(Elements tasks) {
        Vector v = new Vector();
        for (int i = 0; i < tasks.size(); i++) {
            ITask t = new TaskImpl(tasks.get(i), _tl);
            v.add(t);
        }
        return v;
    }
	
	/* 
	 * @see net.sf.memoranda.Task#getSubTask(java.lang.String)
	 */
	public ITask getSubTask(String id) {
		Elements subTasks = _element.getChildElements("task");
		for (int i = 0; i < subTasks.size(); i++) {
			if (subTasks.get(i).getAttribute("id").getValue().equals(id))
				return new TaskImpl(subTasks.get(i), _tl);
		}
		return null;
	}

	/* 
	 * @see net.sf.memoranda.Task#hasSubTasks()
	 */
	public boolean hasSubTasks(String id) {
		Elements subTasks = _element.getChildElements("task");
		for (int i = 0; i < subTasks.size(); i++) 
			if (subTasks.get(i).getAttribute("id").getValue().equals(id))
				return true;
		return false;
	}

	
}
