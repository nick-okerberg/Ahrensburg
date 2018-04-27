package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import main.java.memoranda.CommitList;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.EventNotificationListener;
import main.java.memoranda.EventsManager;
import main.java.memoranda.EventsScheduler;
import main.java.memoranda.History;
import main.java.memoranda.NoteList;
import main.java.memoranda.Project;
import main.java.memoranda.ProjectListener;
import main.java.memoranda.ProjectManager;
import main.java.memoranda.PullRequestList;
import main.java.memoranda.ResourcesList;
import main.java.memoranda.TaskList;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.date.CurrentDate;
import main.java.memoranda.date.DateListener;
import main.java.memoranda.util.AgendaGenerator;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Local;
import main.java.memoranda.util.Util;
import nu.xom.Element;

/*$Id: AgendaPanel.java,v 1.11 2005/02/15 16:58:02 rawsushi Exp $*/
public class AgendaPanel extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JButton historyBackB = new JButton();
    JToolBar toolBar = new JToolBar();
    JButton historyForwardB = new JButton();
    JButton export = new JButton();
    JEditorPane viewer = new JEditorPane("text/html", "");
    String[] priorities = { "Muy Alta", "Alta", "Media", "Baja", "Muy Baja" };
    JScrollPane scrollPane = new JScrollPane();

    DailyItemsPanel parentPanel = null;

    // JPopupMenu agendaPPMenu = new JPopupMenu();
    // JCheckBoxMenuItem ppShowActiveOnlyChB = new JCheckBoxMenuItem();

    Collection expandedTasks;
    String gotoTask = null;

    boolean isActive = true;
    private final JButton _btnNewButton = new JButton("+Add Member");
    private final JButton _btnNewButton_1 = new JButton("- Delete Member");
    // US35 - New JButton for setting a GitHub Repo.
    private final JButton _btnNewButtonRepo = new JButton("Set GitHub Repo");
    private final JButton _btnNewButtonUpdate = new JButton("Import Team");

    // US47 Separate each project into its own tab
    JTabbedPane tabbedPane = new JTabbedPane();
    private final JButton btnDeleteProject = new JButton("Delete Project");

    public AgendaPanel(DailyItemsPanel _parentPanel) {
        try {
            parentPanel = _parentPanel;
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        expandedTasks = new ArrayList();
        
        ChangeListener changeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                Util.debug("Selected index: " + index);
                if (index > -1) {
                    CurrentProject.set(
                        (Project)ProjectManager.getAllProjects().get(index));
                }
            }
            
        };
        tabbedPane.addChangeListener(changeListener);
        
        historyBackB.setAction(History.historyBackAction);
        historyBackB.setFocusable(false);
        historyBackB.setBorderPainted(false);
        historyBackB.setToolTipText(Local.getString("History back"));
        historyBackB.setRequestFocusEnabled(false);
        historyBackB.setPreferredSize(new Dimension(24, 24));
        historyBackB.setMinimumSize(new Dimension(24, 24));
        historyBackB.setMaximumSize(new Dimension(24, 24));
        historyBackB.setText("");

        historyForwardB.setAction(History.historyForwardAction);
        historyForwardB.setBorderPainted(false);
        historyForwardB.setFocusable(false);
        historyForwardB.setPreferredSize(new Dimension(24, 24));
        historyForwardB.setRequestFocusEnabled(false);
        historyForwardB.setToolTipText(Local.getString("History forward"));
        historyForwardB.setMinimumSize(new Dimension(24, 24));
        historyForwardB.setMaximumSize(new Dimension(24, 24));
        historyForwardB.setText("");

        this.setLayout(borderLayout1);
        this.add(tabbedPane, BorderLayout.CENTER);


        toolBar.setFloatable(false);
        toolBar.add(historyBackB, null);
        toolBar.add(historyForwardB, null);
        toolBar.addSeparator(new Dimension(8, 24));

        this.add(toolBar, BorderLayout.NORTH);
        _btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                MemberAdd.main(null);
                System.out.println(CurrentProject.get().getTitle());
            }
        });

        toolBar.add(_btnNewButton);
        _btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MemberDelete.main(null);
                System.out.println(CurrentProject.get().getTitle());
            }
        });

        toolBar.add(_btnNewButton_1);

        /*
         * US35 - Begin code modification Action Listener for pressing the new
         * "Set GitHub Repo" button.
         */

        // First, add the new button to the toolBar at the top of the "Agenda"
        // view.
        toolBar.add(_btnNewButtonRepo);

        // Next, add an action listener for this new button, calling RepoSet
        // function.
        _btnNewButtonRepo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RepoSet.main(null);
                // Get the project name that is currently selected.
                System.out.println(CurrentProject.get().getTitle());
            }
        });
        // End of US35 modification for action listener.

        /*
         * US41 - Begin code modification Action LIstener for pressing the new
         * "Update" Button
         */

        // first, add the new button to the toolbar at the top of the "Agenda"
        // view.
        toolBar.add(_btnNewButtonUpdate);
        btnDeleteProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String currentProjectTitle = CurrentProject.get().getTitle();
                String msg = Local.getString("Delete project")
                            + " '"
                            + currentProjectTitle
                            + "'.\n"
                            + Local.getString("Are you sure?");
                int n =
                    JOptionPane.showConfirmDialog(
                        App.getFrame(),
                        msg,
                        Local.getString("Delete project"),
                        JOptionPane.YES_NO_OPTION);
                if (n != JOptionPane.YES_OPTION) {
                    return;
                }
                else { //Yes option
                    if(ProjectManager.getAllProjects().size() == 1) {
                        JOptionPane.showMessageDialog(null, "SchedStack must have atleast 1 project. That's the rule");
                    }
                    else {
                        ProjectManager.removeProject(CurrentProject.get().getID());
                        App.getFrame().refreshAgenda();
                    }

                    
                }
                    
            }
        });
        
        toolBar.add(btnDeleteProject);

        // Next, add an action listener for this new button, calling RepoSet
        // function with parameters
        _btnNewButtonUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    CurrentProject.get().addRepoName(
                            CurrentProject.get().getGitHubRepoName());
                } catch (RuntimeException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                App.getFrame().refreshAgenda();
            }
        });

        /* We don't care what date is selected
        CurrentDate.addDateListener(new DateListener() {
            public void dateChange(CalendarDate d) {
                if (isActive)
                    refresh(d);
            }
        });
        */
        CurrentProject.addProjectListener(new ProjectListener() {

            public void projectChange(Project prj, NoteList nl, TaskList tl,
                    ResourcesList rl, CommitList cl, PullRequestList prl) {
            }

            public void projectWasChanged() {
                // Find index of Project based on Title
                String title = CurrentProject.get().getTitle();
                int projectIndex = tabbedPane.indexOfTab(title);
                
                if (tabbedPane.getSelectedIndex() != projectIndex) {
                    tabbedPane.setSelectedIndex(projectIndex);
                }
            }
        });
        
        refresh(CurrentDate.get());
    }
    
    public void refreshTab(int index, Project p, CalendarDate date) {
        JEditorPane localViewer = new JEditorPane("text/html", "");
        localViewer.setEditable(false);
        localViewer.setOpaque(false);
        
        localViewer.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    String d = e.getDescription();
                    Util.debug("Link clicked with description: " + d);
                    if (d.startsWith("memoranda:tasks")) {
                        String id = d.split("#")[1];
                        CurrentProject.set(ProjectManager.getProject(id));
                        parentPanel.taskB_actionPerformed(null);
                    } else if (d.startsWith("memoranda:project")) {
                        String id = d.split("#")[1];
                        CurrentProject.set(ProjectManager.getProject(id));
                    } else if (d.startsWith("https://github.com")) {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().browse(new URI(d));
                            } catch (IOException | URISyntaxException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        localViewer.setText(AgendaGenerator.getSingleProject(p, date, expandedTasks));
        JScrollPane sp = (JScrollPane) tabbedPane.getComponent(index);
        sp.getViewport().setBackground(Color.white);
        sp.setViewportView(localViewer);
        sp.getViewport().setBackground(Color.white);
        sp.setViewportView(localViewer);
 
    }

    public void refresh(CalendarDate date) {
        String selectedTitle = null;
        int curIndex = tabbedPane.getSelectedIndex();
        if (curIndex != -1) {
            selectedTitle = tabbedPane.getTitleAt(curIndex);
        }
        
        // For now just get rid of all the tabs
        if (tabbedPane.getTabCount() > 0) {
            Util.debug("Removing all tabs");
            tabbedPane.removeAll();
        }
        
        //US47 Add a tab for each project
        for (int i = 0; i < ProjectManager.getAllProjects().size(); i++) {
            Project p = (Project) ProjectManager.getAllProjects().get(i);
            tabbedPane.add(p.getTitle(), new JScrollPane());
            tabbedPane.setTitleAt(i, p.getTitle());
            refreshTab(i, p, date);            
        }
        
        // Check to see if the formerly selected project is still around
        if (selectedTitle != null) {
            int indexOfSelectedTitle = tabbedPane.indexOfTab(selectedTitle);
            if (indexOfSelectedTitle != -1) {
                tabbedPane.setSelectedIndex(indexOfSelectedTitle);
            }
        }
        
        /*
        viewer.setText(AgendaGenerator.getAgenda(date, expandedTasks));
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (gotoTask != null) {
                    viewer.scrollToReference(gotoTask);
                    scrollPane.setViewportView(viewer);
                    Util.debug("Set view port to " + gotoTask);
                }
            }
        });
        */

        Util.debug("Summary updated.");
    }

    public void setActive(boolean isa) {
        isActive = isa;
    }
}
