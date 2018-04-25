package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
//import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.java.memoranda.ContributorList;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.Task;
import main.java.memoranda.TaskList;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.Contributor;
import main.java.memoranda.util.Local;

import javax.swing.JCheckBox;

/*$Id: TaskDialog.java,v 1.25 2005/12/01 08:12:26 alexeya Exp $*/
public class TaskDialog extends JDialog {
    JPanel mPanel = new JPanel(new BorderLayout());
    JPanel areaPanel = new JPanel(new BorderLayout());
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton cancelB = new JButton();
    JButton okB = new JButton();
    Border border1;
    Border border2;
    JPanel dialogTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel header = new JLabel();
    public boolean CANCELLED = true;
    JPanel jPanel8 = new JPanel(new GridBagLayout());
    Border border3;
    Border border4;
//    Border border5;
//    Border border6;
    JPanel jPanel2 = new JPanel(new GridLayout(3, 2));
    
    JTextField todoField = new JTextField();    // For the "Name" of the sprint. 
    //US48 - Add gitmaster to sprint dialog. 
    JComboBox gitmasterField = new JComboBox();
    
    JTextArea descriptionField = new JTextArea();
    JScrollPane descriptionScrollPane = new JScrollPane(descriptionField);
    
//    Border border7;
    Border border8;
    CalendarFrame startCalFrame = new CalendarFrame();
    CalendarFrame endCalFrame = new CalendarFrame();
    String[] priority = {Local.getString("Lowest"), Local.getString("Low"),
        Local.getString("Normal"), Local.getString("High"),
        Local.getString("Highest")};
    boolean ignoreStartChanged = false;
    boolean ignoreEndChanged = false;
    JPanel jPanel6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel jLabel6 = new JLabel();
    JButton setStartDateB = new JButton();
    JPanel jPanel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JLabel jLabel2 = new JLabel();
    JSpinner startDate;
    JSpinner endDate;
//    JSpinner endDate = new JSpinner(new SpinnerDateModel());
    JButton setEndDateB = new JButton();
    JLabel jLabelDescription = new JLabel();
	
    String todoFieldText = "Enter Sprint Name!";
    
	//Forbid to set dates outside the bounds
	CalendarDate startDateMin = CurrentProject.get().getStartDate();
	CalendarDate startDateMax = CurrentProject.get().getEndDate();
	CalendarDate endDateMin = startDateMin;
	CalendarDate endDateMax = startDateMax;
    
    public TaskDialog(Frame frame, String title) {
        super(frame, title, true);
        try {
            jbInit();            
            pack();
        }
        catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }
    
    void jbInit() throws Exception {
	this.setResizable(false);
	this.setSize(new Dimension(450, 300));
        border1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        border2 = BorderFactory.createEtchedBorder(Color.white, 
            new Color(142, 142, 142));
        border3 = new TitledBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0), 
        Local.getString("Name"), TitledBorder.LEFT, TitledBorder.BELOW_TOP);
        border4 = BorderFactory.createEmptyBorder(0, 5, 0, 5);
//        border5 = BorderFactory.createEmptyBorder();
//        border6 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
//            Color.white, Color.white, new Color(178, 178, 178),
//            new Color(124, 124, 124));
//        border7 = BorderFactory.createLineBorder(Color.white, 2);
        border8 = BorderFactory.createEtchedBorder(Color.white, 
            new Color(178, 178, 178));
        cancelB.setMaximumSize(new Dimension(100, 26));
        cancelB.setMinimumSize(new Dimension(100, 26));
        cancelB.setPreferredSize(new Dimension(100, 26));
        cancelB.setText(Local.getString("Cancel"));
        cancelB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelB_actionPerformed(e);
            }
        });

        startDate = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.DAY_OF_WEEK));
        endDate = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.DAY_OF_WEEK));
		chkEndDate_actionPerformed(null);
        okB.setMaximumSize(new Dimension(100, 26));
        okB.setMinimumSize(new Dimension(100, 26));
        okB.setPreferredSize(new Dimension(100, 26));
        okB.setText(Local.getString("Ok"));
        okB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okB_actionPerformed(e);
            }
        });
        
        this.getRootPane().setDefaultButton(okB);
        mPanel.setBorder(border1);
        areaPanel.setBorder(border2);
        dialogTitlePanel.setBackground(Color.WHITE);
        dialogTitlePanel.setBorder(border4);
        //dialogTitlePanel.setMinimumSize(new Dimension(159, 52));
        //dialogTitlePanel.setPreferredSize(new Dimension(159, 52));
        header.setFont(new java.awt.Font("Dialog", 0, 20));
        header.setForeground(new Color(0, 0, 124));
        header.setText("Sprint Info");
        header.setIcon(new ImageIcon(main.java.memoranda.ui.TaskDialog.class.getResource(
            "/ui/icons/task48.png")));
        
        GridBagLayout gbLayout = (GridBagLayout) jPanel8.getLayout();
        jPanel8.setBorder(border3);
				
        todoField.setBorder(border8);
        todoField.setPreferredSize(new Dimension(375, 24));
        todoField.setText(todoFieldText);
        GridBagConstraints gbCon = new GridBagConstraints();
        gbCon.gridwidth = GridBagConstraints.REMAINDER;
        gbCon.weighty = 1;
        gbLayout.setConstraints(todoField,gbCon);
        
        //US48 JComboBox.
        String jcomboboxString = "Select GitMaster for this Sprint";
        gitmasterField.addItem(jcomboboxString);
        gitmasterField.setSelectedItem(jcomboboxString);
        gitmasterField.setBorder(border8);
        gitmasterField.setPreferredSize(new Dimension(375, 24));
        gitmasterField.setBackground(Color.WHITE);
        gbCon = new GridBagConstraints();
        gbCon.gridwidth = GridBagConstraints.REMAINDER;
        gbCon.weighty = 1;
        gbLayout.setConstraints(gitmasterField,gbCon);
        // Populate the JComboBox with all of the contributors on the current project. 
        ContributorList teamMembers = CurrentProject.getContributorList();
        Vector<Contributor> teamMember = teamMembers.getAllContributors();
        for (int i = 0; i < teamMember.size(); i++) {
            gitmasterField.addItem(teamMember.get(i).getLogin());
        }
        // End of US48 updates
        
        jLabelDescription.setMaximumSize(new Dimension(100, 16));
        jLabelDescription.setMinimumSize(new Dimension(60, 16));
        jLabelDescription.setText(Local.getString("Description"));
        gbCon = new GridBagConstraints();
        gbCon.gridwidth = GridBagConstraints.REMAINDER;
        gbCon.weighty = 1;
        gbCon.anchor = GridBagConstraints.WEST;
        gbLayout.setConstraints(jLabelDescription,gbCon);

        descriptionField.setBorder(border8);
        descriptionField.setPreferredSize(new Dimension(375, 387)); // 3 additional pixels from 384 so that the last line is not cut off
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        gbCon = new GridBagConstraints();
        gbCon.gridwidth = GridBagConstraints.REMAINDER;
        gbCon.weighty = 3;
        descriptionScrollPane.setPreferredSize(new Dimension(375,96));
        gbLayout.setConstraints(descriptionScrollPane,gbCon);

        startDate.setBorder(border8);
        startDate.setPreferredSize(new Dimension(80, 24));                
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf = (SimpleDateFormat)DateFormat.getDateInstance(DateFormat.SHORT);
		// //Added by (jcscoobyrs) on 14-Nov-2003 at 10:45:16 PM
		startDate.setEditor(new JSpinner.DateEditor(startDate, sdf.toPattern()));

        startDate.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	// it's an ugly hack so that the spinner can increase day by day
            	SpinnerDateModel sdm = new SpinnerDateModel((Date)startDate.getModel().getValue(),null,null,Calendar.DAY_OF_WEEK);
            	startDate.setModel(sdm);

                if (ignoreStartChanged)
                    return;
                ignoreStartChanged = true;
                Date sd = (Date) startDate.getModel().getValue();
                Date ed = (Date) endDate.getModel().getValue();
                
                // The chkEndDate is being removed as part of US33. 
                //if (sd.after(ed) && chkEndDate.isSelected()) {	// comment out old. 
                if (sd.after(ed)) {	// new
                // End of US33 modification. 
                    startDate.getModel().setValue(ed);
                    sd = ed;
                }
				if ((startDateMax != null) && sd.after(startDateMax.getDate())) {
					startDate.getModel().setValue(startDateMax.getDate());
                    sd = startDateMax.getDate();
				}
                if ((startDateMin != null) && sd.before(startDateMin.getDate())) {
                    startDate.getModel().setValue(startDateMin.getDate());
                    sd = startDateMin.getDate();
                }
                startCalFrame.cal.set(new CalendarDate(sd));
                ignoreStartChanged = false;
            }
        });

        jLabel6.setText(Local.getString("Start date"));
        //jLabel6.setPreferredSize(new Dimension(60, 16));
        jLabel6.setMinimumSize(new Dimension(60, 16));
        jLabel6.setMaximumSize(new Dimension(100, 16));
        setStartDateB.setMinimumSize(new Dimension(24, 24));
        setStartDateB.setPreferredSize(new Dimension(24, 24));
        setStartDateB.setText("");
        setStartDateB.setIcon(
            new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/calendar.png")));
        setStartDateB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setStartDateB_actionPerformed(e);
            }
        });
        endDate.setBorder(border8);
        endDate.setPreferredSize(new Dimension(80, 24));
        
		endDate.setEditor(new JSpinner.DateEditor(endDate, sdf.toPattern())); //Added by (jcscoobyrs) on
		//14-Nov-2003 at 10:45:16PM
        
        endDate.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	// it's an ugly hack so that the spinner can increase day by day
            	SpinnerDateModel sdm = new SpinnerDateModel((Date)endDate.getModel().getValue(),null,null,Calendar.DAY_OF_WEEK);
            	endDate.setModel(sdm);
            	
                if (ignoreEndChanged)
                    return;
                ignoreEndChanged = true;
                Date sd = (Date) startDate.getModel().getValue();
                Date ed = (Date) endDate.getModel().getValue();				
				if (ed.before(sd)) {
                    endDate.getModel().setValue(ed);
                    ed = sd;
                }
				if ((endDateMax != null) && ed.after(endDateMax.getDate())) {
					endDate.getModel().setValue(endDateMax.getDate());
                    ed = endDateMax.getDate();
				}
                if ((endDateMin != null) && ed.before(endDateMin.getDate())) {
                    endDate.getModel().setValue(endDateMin.getDate());
                    ed = endDateMin.getDate();
                }
				endCalFrame.cal.set(new CalendarDate(ed));
                ignoreEndChanged = false;
            }
        });
        setEndDateB.setMinimumSize(new Dimension(24, 24));
        setEndDateB.setPreferredSize(new Dimension(24, 24));
        setEndDateB.setText("");
        setEndDateB.setIcon(
            new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/calendar.png")));
        setEndDateB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setEndDateB_actionPerformed(e);
            }
        });
        getContentPane().add(mPanel);
        mPanel.add(areaPanel, BorderLayout.CENTER);
        mPanel.add(buttonsPanel, BorderLayout.SOUTH);
        buttonsPanel.add(okB, null);
        buttonsPanel.add(cancelB, null);
        this.getContentPane().add(dialogTitlePanel, BorderLayout.NORTH);
        dialogTitlePanel.add(header, null);
        areaPanel.add(jPanel8, BorderLayout.NORTH);
        jPanel8.add(todoField, null);
        jPanel8.add(gitmasterField, null);  // US48
        jPanel8.add(jLabelDescription);
        jPanel8.add(descriptionScrollPane, null);
        areaPanel.add(jPanel2, BorderLayout.CENTER);
        jPanel2.add(jPanel6, null);
        jPanel6.add(jLabel6, null);
        jPanel6.add(startDate, null);
        jPanel6.add(setStartDateB, null);
        jPanel2.add(jPanel1, null);
		jLabel2.setMaximumSize(new Dimension(270, 16));
		//jLabel2.setPreferredSize(new Dimension(60, 16));
		jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel2.setText(Local.getString("End date"));
		jPanel1.add(jLabel2, null);
        jPanel1.add(endDate, null);
        jPanel1.add(setEndDateB, null);
        startCalFrame.cal.addSelectionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignoreStartChanged)
                    return;
                startDate.getModel().setValue(startCalFrame.cal.get().getCalendar().getTime());
            }
        });
        
        endCalFrame.cal.addSelectionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignoreEndChanged)
                    return;
                endDate.getModel().setValue(endCalFrame.cal.get().getCalendar().getTime());
            }
        });
    }

	public void setStartDate(CalendarDate d) {
		this.startDate.getModel().setValue(d.getDate());
	}
	
	public void setEndDate(CalendarDate d) {		
		if (d != null) 
			this.endDate.getModel().setValue(d.getDate());
	}
	
	public void setStartDateLimit(CalendarDate min, CalendarDate max) {
		this.startDateMin = min;
		this.startDateMax = max;
	}
	
	public void setEndDateLimit(CalendarDate min, CalendarDate max) {
		this.endDateMin = min;
		this.endDateMax = max;
	}
	
	/**
	 * The OK button was pressed. 
	 * @param e
	 */
    void okB_actionPerformed(ActionEvent e) {
    	
        /*
         * Sprint name validation.
         * Ensure the user enters a name for the sprint. 
         * It cannot be the default name, or "null". 
         */
        if ( todoField.getText().equals(todoFieldText) 
                || todoField.getText().equals(null) 
                || todoField.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Enter a valid Sprint Name!");
            return;
        }
        
        
    	/* 
    	 * US33 task 72 feature. nick-okerberg.
    	 * Prevent sprint start/end date overlapping between multiple sprints within a project. 
    	 */

    	// Get all tasks (Sprints) within the current project. 
    	TaskList tl = CurrentProject.getTaskList(); 
    	Vector sprints = (Vector) tl.getAllSubTasks(null); 
    	
    	// Enable for debugging. Prints the current sprint startDate and endDate in the current Sprint being added/modified. 
    	//System.out.println("[DEBUG] Sprint: Currently being modified: begin date = "
    	//		+ startDate.getValue().toString());
    	//System.out.println("[DEBUG] Sprint: Currently being modified: end date = "
    	//		+ endDate.getValue().toString());
    	
    	// Values of the Start and End dates that are currently in the Calendar GUI window. 
    	CalendarDate cdStart = new CalendarDate(new Date(startDate.getValue().toString()));
    	CalendarDate cdEnd = new CalendarDate(new Date(endDate.getValue().toString()));
    	// For debugging: 
    	//System.out.println("Calendar GUI startDate = " + cdStart.toString());
    	//System.out.println("Calendar GUI endDate = " + cdEnd.toString());
    	
    	// If sprints size is >0, then we need to check for overlapping dates between sprints within a project. 
    	if (sprints.size() > 0) {
    		
    		// Sort
    		Collections.sort(sprints);
    		
    		// Iterate through the list of sprints. 
    		int x = 0; // counter
    		for (Iterator i = sprints.iterator(); i.hasNext();) {
    			
    			// A single task at a time. 
    			Task t = (Task) i.next();
    			
    			// The name of the local task being added/modified, from the "Name" field of the JTextField. 
    			String localTaskName = todoField.getText();
    			//System.out.println("TEST: Local task name: " + localTaskName);
    			//System.out.println("TEST: Iteration task name: " + t.toString());
    			
    			// If the local task is the same as the one in this loop iteration, just continue. 
    			// Don't compare to self. 
    			if (localTaskName.equals(t.toString())) {
    				x++;
    				//System.out.println("[DEBUG] Sprint: comparing to same sprint. Ignoring.");
    				continue;
    			}
    			
    			// Enable for debugging to console. Iterates through all sprints and dumps date info. 
    			//System.out.println("[DEBUG] Sprint: Loop iteration index#" + x);
    			//System.out.println("[DEBUG] Sprint: Title = " + t.toString());
    			//System.out.println("[DEBUG] Sprint: Descr = " + t.getDescription());
    			//System.out.println("[DEBUG] Sprint: begin date = " + t.getStartDate());
    			//System.out.println("[DEBUG] Sprint: end date = " + t.getEndDate());
    			
    			/*
    			 * If the start date, or end date, of the current sprint being added/modified
    			 * is before the end date and after the start date of any other sprint on
    			 * the project, then there is an overlapping date. In such cases, then 
    			 * display a pop up message and do not allow the change, force the user
    			 * to select a new date for the current sprint.  
    			 */
    			
    			// If current start-date is after t's start date & before t's end-date, there is overlap. 
    			if ( cdStart.after(t.getStartDate()) && cdStart.before(t.getEndDate())) {
    				
    				// For debugging, print the name of the Sprint that there was a conflict with. 
    				System.out.println("[DEBUG] Sprint: overlapping Sprints detected, "
    						+ "start-date conflict with: " + t.toString());
    				
    				JOptionPane.showMessageDialog(null, "Error, start-date overlaps with sprint: " + t.toString());
    				return;
    			}
    			
    			// If current end-date is after t's start date & before t's end-date, there is overlap. 
    			if ( cdEnd.after(t.getStartDate()) && cdEnd.before(t.getEndDate())) {
    				
    				// For debugging, print the name of the Sprint that there was a conflict with. 
    				System.out.println("[DEBUG] Sprint: overlapping Sprints detected, "
    						+ "end-date conflict with: " + t.toString());
    				
    				JOptionPane.showMessageDialog(null, "Error, end-date overlaps with sprint: " + t.toString());
    				return;
    			}
    			
    			// If current start-date is the same as t's start date, there is overlap. 
    			if ( cdStart.equals(t.getStartDate())) {
    				
    				// For debugging, print the name of the Sprint that there was a conflict with. 
    				System.out.println("[DEBUG] Sprint: overlapping Sprints detected, "
    						+ "start-date is the same as the one in sprint: " + t.toString());
    				
    				JOptionPane.showMessageDialog(null, "Error, start-date is the same as the one in sprint: " + t.toString());
    				return;
    			}
    			
    			// If current end-date is the same as t's end date, there is overlap. 
    			if ( cdEnd.equals(t.getEndDate())) {
    				
    				// For debugging, print the name of the Sprint that there was a conflict with. 
    				System.out.println("[DEBUG] Sprint: overlapping Sprints detected, "
    						+ "end-date is the same as the one in sprint: " + t.toString());
    				
    				JOptionPane.showMessageDialog(null, "Error, end-date is the same as the one in sprint: " + t.toString());
    				return;
    			}
    			
    			// Increment the loop iteration counter. 
    			x++;
    			
    		} // End of for loop. 
    	} // End outer if. 
    	
    	System.out.println("[DEBUT] OK Button pressed for Sprint task");
    	CANCELLED = false;
        this.dispose();
    } // End of function. End of US33 task 72 feature. 

    void cancelB_actionPerformed(ActionEvent e) {
        this.dispose();
    }
	
	void chkEndDate_actionPerformed(ActionEvent e) {
	    // US33 changes, removing chkEndDate selection box. 
		//if(chkEndDate.isSelected()) { // US33 changes. 
			Date currentEndDate = (Date) endDate.getModel().getValue();
			Date currentStartDate = (Date) startDate.getModel().getValue();
			if(currentEndDate.getTime() < currentStartDate.getTime()) {
				endDate.getModel().setValue(currentStartDate);
			}
		//} // US33 changes
	}

    void setStartDateB_actionPerformed(ActionEvent e) {
        startCalFrame.setLocation(setStartDateB.getLocation());
        startCalFrame.setSize(200, 200);
        this.getLayeredPane().add(startCalFrame);
        startCalFrame.show();

    }

    void setEndDateB_actionPerformed(ActionEvent e) {
        //endCalFrame.setLocation(setEndDateB.getLocation());
        endCalFrame.setLocation(setStartDateB.getLocation()); // Fix for calendar window popping up. 
        endCalFrame.setSize(200, 200);
        this.getLayeredPane().add(endCalFrame);
        endCalFrame.show();
    }

}