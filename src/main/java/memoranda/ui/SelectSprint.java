package main.java.memoranda.ui;

import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.ITask;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.Commit;
import main.java.memoranda.util.Contributor;

import javax.swing.JLabel;
import java.awt.Image;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;

public class SelectSprint extends JDialog {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            SelectSprint dialog = new SelectSprint();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setPreferredSize(new Dimension(500, 200));
            dialog.pack();
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public SelectSprint() {
        setTitle("Sprint Selector");
        setBounds(100, 100, 450, 300);
        this.setLayout(new BorderLayout());
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JLabel lblSelectSprint = new JLabel("Select Sprint");
        lblSelectSprint.setHorizontalAlignment(SwingConstants.CENTER);
        lblSelectSprint
                .setFont(new Font("Super Mario 256", Font.PLAIN, 18));
        getContentPane().add(lblSelectSprint, Component.LEFT_ALIGNMENT);
        
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        DefaultListModel<String> typeListModel = new DefaultListModel<String>();
        Collection<ITask> listOfSprints = CurrentProject.getTaskList()
                .getTopLevelTasks();
        
        // A helper vector to store sprints. better than a collection.
        Vector<ITask> taskVector = new Vector<>();
        for (ITask task : listOfSprints) {
            listModel.addElement(task.getText());
            taskVector.add(task);
        }
        
        // Build sprint combobox
        Vector<String> sprintTitles = new Vector<>();
        for (int i = 0; i < taskVector.size(); i++) {
            sprintTitles.add(taskVector.get(i).getText());
            System.out.println("--->>>" + taskVector.get(i).getText());
        }
        // Also add entire project
        sprintTitles.add("EntireProject");
        JComboBox sprintCmbBox = new JComboBox(sprintTitles);
        sprintCmbBox.setPreferredSize(new Dimension(50, 5));
        // Build comparison combobox
        String[] comparisonsOptions = {"Commits", "LOC"};
        JComboBox comparisonCmbBox = new JComboBox(comparisonsOptions);
        comparisonCmbBox.setPreferredSize(new Dimension(50, 5));
        getContentPane().add(sprintCmbBox, Component.LEFT_ALIGNMENT);
        getContentPane().add(comparisonCmbBox, Component.LEFT_ALIGNMENT);
        
        Button button = new Button("Select");
        button.setPreferredSize(new Dimension(25, 5));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selectedProject = false;
                int selectedIndex = sprintCmbBox.getSelectedIndex();
                if (selectedIndex >= taskVector.size()) {
                    selectedProject = true;
                }
                // 0 for compare commits, 1 for LOC
                int selectedCompare = comparisonCmbBox.getSelectedIndex();
                /* Because the list and taskVector were built the same their
                indices match. */
                ITask sprint = null;
                if (! selectedProject) {
                    sprint = taskVector.get(selectedIndex);
                }
                
                // First iterate over each team member
                String[] gitNames = CurrentProject.get().getGitNames().split(",");
                Vector<Contributor> team = CurrentProject.getContributorList().getAllContributors();
                for (int i = 0; i < team.size(); i++) {
                    String login = team.get(i).getLogin();
                    List<Integer> scores = new ArrayList<>();
                    
                    // Get just the commits for this team member
                    List<Commit> commitsForName = 
                            CurrentProject.getCommitList().getAllCommitsByAuthor(login);
                    
                    Date start;
                    Date end;
                    if (selectedProject) {
                        start = CurrentProject.get().getStartDate().getDate();
                        end = CurrentProject.get().getEndDate().getDate();
                        
                    } else {
                        start = sprint.getStartDate().getDate();
                        end = sprint.getEndDate().getDate();
                    }
                    // get days between start and end
                    long diff = end.getTime() - start.getTime();
                    long dayCount = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    /*
                    Util.debug("Checking sprint: "
                                + sprint.getText()
                                + "over " + dayCount + "days");
                    */
                    
                    // Next for each day get the number of commits and add to scores
                    Date startDay = start;
                    for (int j = 0; j <= dayCount + 1; j++) {
                        Date endDay = CalendarDate.addDays(startDay,1);
                        // Count commits on this date
                        int metricCount = 0;
                        for (int k = 0; k < commitsForName.size(); k++) {
                            Date thisCmtDate = commitsForName.get(k).getDate();
                            if (thisCmtDate.after(startDay) && thisCmtDate.before(endDay)) {
                                if (selectedCompare == 0) {
                                    metricCount++;
                                } else if (selectedCompare == 1) {
                                    metricCount += commitsForName.get(k).getTotalLoc();
                                }
                            }
                        }
                        startDay = endDay;
                        scores.add(metricCount);
                    }
                    
                    // Now put this on a graph
                    TeamMemberGraph graph = new TeamMemberGraph(scores, start);

                    // Get the avatar for this team member
                    Image image = null;
                    try {
                        URL url = new URL(team.get(i).getAvatarUrl());
                        image = graph.getScaledImage(ImageIO.read(url), 32, 32);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    
                    graph.displayGraph(login 
                            + " - " 
                            + comparisonsOptions[comparisonCmbBox.getSelectedIndex()] 
                            +  " ("
                            + sprintTitles.get(sprintCmbBox.getSelectedIndex())
                            + ")", image);
                }
            }
        });
        //getContentPane().setLayout(new BorderLayout());
        getContentPane().add(button, BorderLayout.SOUTH);
        this.pack();
    }

}
