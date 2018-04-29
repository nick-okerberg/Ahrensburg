/**
 * TaskTableModel.java         
 * -----------------------------------------------------------------------------
 * Project           Memoranda
 * Package           net.sf.memoranda.ui
 * Original author   Alex V. Alishevskikh
 *                   [alexeya@gmail.com]
 * Created           18.05.2005 15:16:11
 * Revision info     $RCSfile: TaskTableModel.java,v $ $Revision: 1.7 $ $State: Exp $  
 *
 * Last modified on  $Date: 2005/12/01 08:12:26 $
 *               by  $Author: alexeya $
 * 
 * @VERSION@ 
 *
 * @COPYRIGHT@
 * 
 * @LICENSE@ 
 */

package main.java.memoranda.ui;

import javax.swing.event.*;
import javax.swing.tree.TreePath;

import main.java.memoranda.*;
import main.java.memoranda.date.CurrentDate;
import main.java.memoranda.ui.treetable.AAbstractTreeTableModel;
import main.java.memoranda.ui.treetable.ITreeTableModel;
import main.java.memoranda.util.Context;
import main.java.memoranda.util.Local;

import java.util.Hashtable;

import nu.xom.Attribute;

/**
 * JAVADOC:
 * <h1>TaskTableModel</h1>
 * 
 * @version $Id: TaskTableModel.java,v 1.7 2005/12/01 08:12:26 alexeya Exp $
 * @author $Author: alexeya $
 */
public class TaskTableModel extends AAbstractTreeTableModel implements ITreeTableModel {

//    String[] columnNames = {"", Local.getString("To-do"), 
//            Local.getString("Start date"), Local.getString("End date"),
//            Local.getString("Priority"), Local.getString("Status"),
//            "% " + Local.getString("done") };
    String[] columnNames = {"", Local.getString("Name"), //Ovadia Shalom US8
            Local.getString("Start date"), 
            Local.getString("End date"),
    		Local.getString("Tests"),  // Nick Okerberg US37, number of JUnit tests per Sprint. 
    		Local.getString("Commits"),
    		Local.getString("GitMaster")};  // Nick Okerberg US48, Display GitMaster per Sprint. 
    protected EventListenerList listenerList = new EventListenerList();

    private boolean activeOnly = check_activeOnly();
        
    /**
     * JAVADOC: Constructor of <code>TaskTableModel</code>
     * 
     * @param root
     */
    public TaskTableModel(){
        super(CurrentProject.get());
    }

    /**
     * @see main.java.memoranda.ui.treetable.ITreeTableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * @see main.java.memoranda.ui.treetable.ITreeTableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * @see main.java.memoranda.ui.treetable.ITreeTableModel#getValueAt(java.lang.Object,
     *      int)
     */
    public Object getValueAt(Object node, int column) {
        if (node instanceof IProject)
            return null;
        ITask t = (ITask) node;
        switch (column) {
        case 0:
            return "";
        // Column for Sprint Name. 
        case 1:
            return t;
        // Column for Sprint Start Date. 
        case 2:
            return t.getStartDate().getDate();
        // Column for Sprint End Date. 
        case 3:
            if (t.getEndDate() == null)
                return null;
            else
                return t.getEndDate().getDate();   
        // Column for total number of tests in the Sprint.  US37. 
        case 4:
        	// Old code for Priority column, deactivated as part of US37. 
            //return getPriorityString(t.getPriority());
        	/* 
        	 * US 37 code for Total number of tests in the Sprint. US37. 
        	 * Call the getNumberSprintTests method from TaskImpl. 
        	 */
        	//System.out.println("TaskTableModel: Triggered Getting Number Sprint Tests");
        	return t.getNumberSprintTests();
        // Column for total number of Commits. 
        case 5:
            return t.getNumberCommits();
        // Column for gitMaster for specific Sprint. 
        case 6:
            //System.out.println("TEST: GitMaster is: " + t.getGitMaster());
            return t.getGitMaster();    // US48 Nick Okerberg - Display GitMaster per sprint.
        case 7:             
            //return new Integer(t.getProgress());
			return t;
        case TaskTable.TASK_ID:
            return t.getID();
        case TaskTable.TASK:
            return t;
        }
        return "";
    }

    String getStatusString(int status) {
        switch (status) {
        case ITask.ACTIVE:
            return Local.getString("Active");
        case ITask.DEADLINE:
            return Local.getString("Deadline");
        case ITask.COMPLETED:
            return Local.getString("Completed");
        case ITask.FAILED:
            return Local.getString("Failed");
        case ITask.FROZEN:
            return Local.getString("Frozen");
        case ITask.LOCKED:
            return Local.getString("Locked");
        case ITask.SCHEDULED:
            return Local.getString("Scheduled");
        }
        return "";
    }

    String getPriorityString(int p) {
        switch (p) {
        case ITask.PRIORITY_NORMAL:
            return Local.getString("Normal");
        case ITask.PRIORITY_LOW:
            return Local.getString("Low");
        case ITask.PRIORITY_LOWEST:
            return Local.getString("Lowest");
        case ITask.PRIORITY_HIGH:
            return Local.getString("High");
        case ITask.PRIORITY_HIGHEST:
            return Local.getString("Highest");
        }
        return "";
    }

    /**
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {
        if (parent instanceof IProject) {
		if( activeOnly() ){
			return CurrentProject.getTaskList().getActiveSubTasks(null, CurrentDate.get()).size();
		}
		else return CurrentProject.getTaskList().getTopLevelTasks().size();
        }
        ITask t = (ITask) parent;
        if(activeOnly()) return CurrentProject.getTaskList().getActiveSubTasks(t.getID(), CurrentDate.get()).size();
	else return t.getSubTasks().size();
    }

    /**
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    public Object getChild(Object parent, int index) {
        if (parent instanceof IProject)
            if( activeOnly() ) return CurrentProject.getTaskList().getActiveSubTasks(null, CurrentDate.get()).toArray()[index];
	    else return CurrentProject.getTaskList().getTopLevelTasks().toArray()[index];
        ITask t = (ITask) parent;
        if(activeOnly()) return CurrentProject.getTaskList().getActiveSubTasks(t.getID(), CurrentDate.get()).toArray()[index];
	else return t.getSubTasks().toArray()[index];
    }

    /**
     * @see main.java.memoranda.ui.treetable.ITreeTableModel#getColumnClass(int)
     */
    public Class getColumnClass(int column) {
        try {
            switch (column) {
            case 1:
                return ITreeTableModel.class;
            case 0:
                return TaskTable.class;
            case 4:
            case 5:
                return Class.forName("java.lang.String");
            case 2:
            case 3:
                return Class.forName("java.util.Date");
            case 6:
                return Class.forName("java.lang.Integer");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public void fireTreeStructureChanged(){	    
	    fireTreeStructureChanged( this,
	    			new Object[]{getRoot()},
				new int[0],
				new Object[0]
				);
    }
    
    
    /**
     * Update cached data
     */
    public void fireUpdateCache(){
		activeOnly = check_activeOnly();
    }

    public static boolean check_activeOnly(){
		Object o = Context.get("SHOW_ACTIVE_TASKS_ONLY");
		if(o == null) return false;
		return o.toString().equals("true");
	}

    public boolean activeOnly(){
		return activeOnly;
    }
    
    public boolean isCellEditable(Object node, int column) {
		if(column == 6) return true; 
        return super.isCellEditable(node, column); 
    }

}