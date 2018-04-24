package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import main.java.memoranda.util.Context;
import main.java.memoranda.util.Local;
import java.awt.Font;

/**
 * 
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

/*$Id: WorkPanel.java,v 1.9 2004/04/05 10:05:44 alexeya Exp $*/
public class WorkPanel extends JPanel {
	BorderLayout borderLayout1 = new BorderLayout();
	JToolBar toolBar = new JToolBar();
	JPanel panel = new JPanel();
	CardLayout cardLayout1 = new CardLayout();

	// US101 - removing notes button.
	//public JButton notesB = new JButton();
	
	public DailyItemsPanel dailyItemsPanel = new DailyItemsPanel(this);
	public ResourcesPanel filesPanel = new ResourcesPanel();
	public JButton agendaB = new JButton();
	public JButton tasksB = new JButton();
	// US101 - Remove Events button on main page. 
	//public JButton eventsB = new JButton();
	// US101 - Remove Resources button on main page.
	//public JButton filesB = new JButton();
	public JButton btnCommits = new JButton();
	public JButton pullReqBtn = new JButton();
	public static int initialStart = 0;
	        
	JButton currentB = null;
	Border border1;

	public WorkPanel() {
		try {
			jbInit();
		} catch (Exception ex) {
			new ExceptionDialog(ex);
		}
	}

	void jbInit() throws Exception {
		border1 =
			BorderFactory.createCompoundBorder(
				BorderFactory.createBevelBorder(
					BevelBorder.LOWERED,
					Color.white,
					Color.white,
					new Color(124, 124, 124),
					new Color(178, 178, 178)),
				BorderFactory.createEmptyBorder(0, 2, 0, 0));

		this.setLayout(borderLayout1);
		toolBar.setOrientation(JToolBar.VERTICAL);
		toolBar.setBackground(Color.white);

		toolBar.setBorderPainted(false);
		toolBar.setFloatable(false);
		panel.setLayout(cardLayout1);

		agendaB.setBackground(Color.white);
		agendaB.setMaximumSize(new Dimension(60, 80));
		agendaB.setMinimumSize(new Dimension(30, 30));
		agendaB.setFont(new java.awt.Font("Dialog", 1, 10));
		agendaB.setPreferredSize(new Dimension(50, 50));
		agendaB.setBorderPainted(false);
		agendaB.setContentAreaFilled(false);
		agendaB.setFocusPainted(false);
		agendaB.setHorizontalTextPosition(SwingConstants.CENTER);
		agendaB.setText(Local.getString("Projects"));
		agendaB.setVerticalAlignment(SwingConstants.TOP);
		agendaB.setVerticalTextPosition(SwingConstants.BOTTOM);
		agendaB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agendaB_actionPerformed(e);
			}
		});
		agendaB.setIcon(
			new ImageIcon(
				main.java.memoranda.ui.AppFrame.class.getResource(
					"/ui/icons/scrum2.png")));
		agendaB.setOpaque(false);
		agendaB.setMargin(new Insets(0, 0, 0, 0));
		agendaB.setSelected(true);
	
		tasksB.setSelected(true);
		tasksB.setFont(new java.awt.Font("Dialog", 1, 10));
		tasksB.setMargin(new Insets(0, 0, 0, 0));
		tasksB.setIcon(
			new ImageIcon(
				main.java.memoranda.ui.AppFrame.class.getResource(
					"/ui/icons/sprint.png")));
		tasksB.setVerticalTextPosition(SwingConstants.BOTTOM);
		tasksB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tasksB_actionPerformed(e);
			}
		});
		tasksB.setVerticalAlignment(SwingConstants.TOP);
		tasksB.setText(Local.getString("Sprints"));
		tasksB.setHorizontalTextPosition(SwingConstants.CENTER);
		tasksB.setFocusPainted(false);
		tasksB.setBorderPainted(false);
		tasksB.setContentAreaFilled(false);
		tasksB.setPreferredSize(new Dimension(50, 50));
		tasksB.setMinimumSize(new Dimension(30, 30));
		tasksB.setOpaque(false);
		tasksB.setMaximumSize(new Dimension(60, 80));
		tasksB.setBackground(Color.white);
		
		
		
		
		
		
		btnCommits.setBackground(Color.white);
		btnCommits.setMaximumSize(new Dimension(60, 80));
		btnCommits.setMinimumSize(new Dimension(30, 30));

		btnCommits.setFont(new java.awt.Font("Dialog", 1, 10));
		btnCommits.setPreferredSize(new Dimension(50, 50));
		btnCommits.setBorderPainted(false);
		btnCommits.setContentAreaFilled(false);
		btnCommits.setFocusPainted(false);
		btnCommits.setHorizontalTextPosition(SwingConstants.CENTER);
		btnCommits.setText(Local.getString("Projects"));
		btnCommits.setVerticalAlignment(SwingConstants.TOP);
		btnCommits.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnCommits.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	btnCommits_actionPerformed(e);
		    }
		});
		btnCommits.setIcon(
		    new ImageIcon(
		        main.java.memoranda.ui.AppFrame.class.getResource(
		            "/ui/icons/githubicon.png")));
		btnCommits.setOpaque(false);
		btnCommits.setMargin(new Insets(0, 0, 0, 0));
		btnCommits.setSelected(true);

		
		
        pullReqBtn.setBackground(Color.white);
        pullReqBtn.setMaximumSize(new Dimension(60, 80));
        pullReqBtn.setMinimumSize(new Dimension(30, 30));
        pullReqBtn.setFont(new java.awt.Font("Dialog", 1, 10));
        pullReqBtn.setPreferredSize(new Dimension(50, 50));
        pullReqBtn.setBorderPainted(false);
        pullReqBtn.setContentAreaFilled(false);
        pullReqBtn.setFocusPainted(false);
        pullReqBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        pullReqBtn.setText(Local.getString("PullRequests"));
        pullReqBtn.setVerticalAlignment(SwingConstants.TOP);
        pullReqBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        pullReqBtn.addActionListener(new java.awt.event.ActionListener() {
         
            public void actionPerformed(ActionEvent e) {
                pullReqBtn_actionPerformed(e);
            }
        });
        pullReqBtn.setIcon(
            new ImageIcon(
                main.java.memoranda.ui.AppFrame.class.getResource(
                    "/ui/icons/gitPull.png")));
        pullReqBtn.setOpaque(false);
        pullReqBtn.setMargin(new Insets(0, 0, 0, 0));
        pullReqBtn.setSelected(true);
		
		pullReqBtn.setOpaque(false);
		pullReqBtn.setFocusPainted(false);
		pullReqBtn.setContentAreaFilled(false);
        pullReqBtn.setBorderPainted(false);
		pullReqBtn.setSelected(true);
		pullReqBtn.setText("PullReq");
		
		
		this.setPreferredSize(new Dimension(1073, 300));

		
		
		
		this.add(toolBar, BorderLayout.WEST);
		this.add(panel, BorderLayout.CENTER);
		panel.add(dailyItemsPanel, "DAILYITEMS");
		panel.add(filesPanel, "FILES");
		toolBar.add(agendaB, null);
		toolBar.add(tasksB, null);
		toolBar.add(btnCommits, null);
		toolBar.add(pullReqBtn, null);
		
		

		
		currentB = agendaB;
		// Default blue color
		currentB.setBackground(new Color(215, 225, 250));
		currentB.setOpaque(true);

		toolBar.setBorder(null);
		panel.setBorder(null);
		dailyItemsPanel.setBorder(null);
		filesPanel.setBorder(null);

	}

	public void selectPanel(String pan) {
		if (pan != null) {
			if (pan.equals("AGENDA")) {
				agendaB_actionPerformed(null);
			}
			else if (pan.equals("TASKS")) {
				tasksB_actionPerformed(null);
			}
			else if (pan.equals("COMMITS")) {
				btnCommits_actionPerformed(null);
			}
			else if (pan.equals("PULLREQUEST")) {
				pullReqBtn_actionPerformed(null);
			}
			

				
		}
	}

	public void agendaB_actionPerformed(ActionEvent e) {
		cardLayout1.show(panel, "DAILYITEMS");
		dailyItemsPanel.selectPanel("AGENDA");
		setCurrentButton(agendaB);
		Context.put("CURRENT_PANEL", "AGENDA");
		initialStart++;
	}

	
	public void tasksB_actionPerformed(ActionEvent e) {
		cardLayout1.show(panel, "DAILYITEMS");
		dailyItemsPanel.selectPanel("TASKS");
		setCurrentButton(tasksB);
		Context.put("CURRENT_PANEL", "TASKS");
		if(initialStart == 0) {
			agendaB_actionPerformed(null);
			initialStart++;
		}
	}
	
	public void btnCommits_actionPerformed(ActionEvent e) {
		cardLayout1.show(panel, "DAILYITEMS");
		dailyItemsPanel.selectPanel("COMMITS");
		setCurrentButton(btnCommits);
		Context.put("CURRENT_PANEL", "COMMITS");
		if(initialStart == 0) {
			agendaB_actionPerformed(null);
			initialStart++;
		}
	}

	
	public void pullReqBtn_actionPerformed(ActionEvent e) {
	        cardLayout1.show(panel, "DAILYITEMS");
	        dailyItemsPanel.selectPanel("PULLREQUEST");
	        setCurrentButton(pullReqBtn);
	        Context.put("CURRENT_PANEL", "PULLREQUEST");
			if(initialStart == 0) {
				agendaB_actionPerformed(null);
				initialStart++;
			}
	    }
	
	void setCurrentButton(JButton cb) {
		currentB.setBackground(Color.white);
		currentB.setOpaque(false);
		currentB = cb;
		// Default color blue
		currentB.setBackground(new Color(215, 225, 250));
		currentB.setOpaque(true);
	}
}