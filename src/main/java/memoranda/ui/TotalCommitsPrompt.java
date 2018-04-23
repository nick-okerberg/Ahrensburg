package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Task;
import main.java.memoranda.TaskList;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.Commit;
import main.java.memoranda.util.Contributor;
import main.java.memoranda.util.Util;

import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Font;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;

public class TotalCommitsPrompt extends JDialog {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            TotalCommitsPrompt dialog = new TotalCommitsPrompt();
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
    public TotalCommitsPrompt() {
        setTitle("Sprint Selector");
        setBounds(100, 100, 565, 448);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JLabel lblSelectSprint = new JLabel("Select Sprint");
        lblSelectSprint.setHorizontalAlignment(SwingConstants.CENTER);
        lblSelectSprint
                .setFont(new Font("Super Mario 256", Font.PLAIN, 18));
        getContentPane().add(lblSelectSprint, Component.LEFT_ALIGNMENT);
        
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        DefaultListModel<String> typeListModel = new DefaultListModel<String>();
        Collection<Task> listOfSprints = CurrentProject.getTaskList()
                .getTopLevelTasks();
        
        // A helper vector to store sprints. better than a collection.
        Vector<Task> taskVector = new Vector<>();
        for (Task task : listOfSprints) {
            listModel.addElement(task.getText());
            taskVector.add(task);
        }
        
        // Build sprint combobox
        Vector<String> sprintTitles = new Vector<>();
        for (int i = 0; i < taskVector.size(); i++) {
            sprintTitles.add(taskVector.get(i).getText());
        }
        JComboBox sprintCmbBox = new JComboBox(sprintTitles);
        sprintCmbBox.setPreferredSize(new Dimension(50, 5));
        // Build comparison combobox
        String[] comparisonsOptions = {"Commits", "LOC"};
        getContentPane().add(sprintCmbBox, Component.LEFT_ALIGNMENT);
        
        Button button = new Button("Select");
        button.setPreferredSize(new Dimension(25, 5));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selectedProject = false;
                int selectedIndex = sprintCmbBox.getSelectedIndex();
                if (selectedIndex >= taskVector.size()) {
                    selectedProject = true;
                }
                /* Because the list and taskVector were built the same their
                indices match. */
                Task sprint = null;
                if (! selectedProject) {
                    sprint = taskVector.get(selectedIndex);
                }
                
                // First iterate over each team member
                String[] gitNames = CurrentProject.get().getGitNames().split(",");
                Vector<Contributor> team = CurrentProject.getContributorList().getAllContributors();
                List<Integer> scores = new ArrayList<>();
                Date start = sprint.getStartDate().getDate();
                Date end = sprint.getEndDate().getDate();
             // Get just the commits for this team member
                List<Commit> commitsForName = CurrentProject.getCommitList().getAllCommits();
                long diff = end.getTime() - start.getTime();
                long dayCount = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                Date startDay = start;
                for (int j = 0; j <= dayCount + 1; j++) {
                    Date endDay = CalendarDate.addDays(startDay,1);
                    // Count commits on this date
                    int metricCount = 0;
                    for (int k = 0; k < commitsForName.size(); k++) {
                        Date thisCmtDate = commitsForName.get(k).getDate();
                        if (thisCmtDate.after(startDay) && thisCmtDate.before(endDay)) {
                            metricCount++;
                        }
                    }
                    startDay = endDay;
                    scores.add(metricCount);
                }
                    
                    
                
                JFrame f = new JFrame();
                f.setSize(400, 300);

                f.getContentPane().add(new TeamMemberGraph(scores, start));    
                f.setVisible(true);
                Point loc = App.getFrame().getLocation();
                Dimension frmSize = App.getFrame().getSize();
                Dimension dlgSize = f.getSize();
                f.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
            }
        });
        //getContentPane().setLayout(new BorderLayout());
        getContentPane().add(button, BorderLayout.SOUTH);
        this.pack();
    }

}
