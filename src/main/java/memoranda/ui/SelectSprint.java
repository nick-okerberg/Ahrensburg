package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.Task;
import main.java.memoranda.TaskList;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.Commit;
import main.java.memoranda.util.Util;

import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
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

public class SelectSprint extends JDialog {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            SelectSprint dialog = new SelectSprint();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
        getContentPane().setLayout(new BorderLayout(0, 0));
        {
            JLabel lblSelectSprint = new JLabel("Select Sprint");
            lblSelectSprint.setHorizontalAlignment(SwingConstants.CENTER);
            lblSelectSprint
                    .setFont(new Font("Super Mario 256", Font.PLAIN, 18));
            getContentPane().add(lblSelectSprint, BorderLayout.NORTH);
        }
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        Collection<Task> listOfSprints = CurrentProject.getTaskList()
                .getTopLevelTasks();
        
        // A helper vector to store sprints. better than a collection.
        Vector<Task> taskVector = new Vector<>();
        for (Task task : listOfSprints) {
            listModel.addElement(task.getText());
            taskVector.add(task);
        }
        JList<String> list = new JList<String>(listModel);
        list.setSelectionMode(0);
        getContentPane().add(list, BorderLayout.CENTER);

        Button button = new Button("Select");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = list.getSelectedIndex();
                /* Because the list and taskVector were built the same their
                indices match. */
                Task sprint = taskVector.get(selectedIndex);
                
                
                
                
                // First iterate over each team member
                String[] gitNames = CurrentProject.get().getGitNames().split(",");
                for (int i = 0; i < gitNames.length; i++) {
                    String name = gitNames[i];
                    List<Integer> scores = new ArrayList<>();
                    
                    // Get just the commits for this team member
                    List<Commit> commitsForName = 
                            CurrentProject.getCommitList().getAllCommitsByAuthor(name);
                    Date start = sprint.getStartDate().getDate();
                    Date end = sprint.getEndDate().getDate();
                    // get days between start and end
                    long diff = end.getTime() - start.getTime();
                    long dayCount = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    
                    Util.debug("Checking sprint: "
                                + sprint.getText()
                                + "over " + dayCount + "days");
                    
                    // Next for each day get the number of commits and add to scores
                    Date startDay = start;
                    for (int j = 0; j <= dayCount + 1; j++) {
                        Date endDay = addDays(startDay,1);
                        // Count commits on this date
                        int dateCommitCount = 0;
                        for (int k = 0; k < commitsForName.size(); k++) {
                            Date thisCmtDate = commitsForName.get(k).getDate();
                            if (thisCmtDate.after(startDay) && thisCmtDate.before(endDay)) {
                                dateCommitCount++;
                            }
                        }
                        startDay = endDay;
                        scores.add(dateCommitCount);
                    }
                    
                    // Now put this on a graph
                    TeamMemberGraph graph = new TeamMemberGraph(scores);
                    graph.displayGraph(name + " - Commit Count (" + sprint.getText() + ")");
                }
            }
        });
        getContentPane().add(button, BorderLayout.SOUTH);
    }
    
    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

}
