package main.java.memoranda.ui;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.ITask;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.util.Commit;
import main.java.memoranda.util.Contributor;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
/**
 *
 * @author Sean Rogers
 */
public class SelectTeamMemberAndSprintDialog extends JDialog {
    private JComboBox _teamMember;
    private JComboBox _sprint;

    public static void main(String[] args) {
        try {
            SelectTeamMemberAndSprintDialog dialog = new SelectTeamMemberAndSprintDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            dialog.setTitle("Select Team Member And Sprint");
            Point loc = App.getFrame().getLocation();
            Dimension frmSize = App.getFrame().getSize();
            Dimension dlgSize = dialog.getSize();
            dialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SelectTeamMemberAndSprintDialog() {
        setBounds(100, 100, 543, 222);
        getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),},
                new RowSpec[] {
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,}));
        {
            JLabel lblTeamMember = new JLabel("Team Member:");
            getContentPane().add(lblTeamMember, "2, 2, 5, 1, left, default");
        }
        {

            _teamMember = new JComboBox();
            String[] arr = CurrentProject.get().getGitNames().split(",");
            for(int i = 0; i < arr.length; i++) {
                _teamMember.addItem((String) arr[i]);
            }
            getContentPane().add(_teamMember, "2, 4, 5, 1, fill, default");
        }
        {
            JLabel lblSprint = new JLabel("Sprint");
            getContentPane().add(lblSprint, "2, 6, 5, 1, left, default");
        }
        {

            _sprint = new JComboBox();
            Collection<ITask> test = CurrentProject.getTaskList().getTopLevelTasks();
            for(ITask t : test) {
                _sprint.addItem((String) t.getText());
            }
            getContentPane().add(_sprint, "2, 8, 5, 1, fill, default");
        }

        DefaultListModel<String> listModel = new DefaultListModel<String>();
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
        }


        JButton btnNewButton = new JButton("Ok");
        getContentPane().add(btnNewButton, "1, 12, 6, 1, fill, default");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            boolean selectedProject = false;
            int selectedIndex = _sprint.getSelectedIndex();
            if (selectedIndex >= taskVector.size()) {
                selectedProject = true;
            }
            // 0 for compare commits, 1 for LOC
            int selectedCompare = _teamMember.getSelectedIndex();
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
                if (!_teamMember.getSelectedItem().equals(team.get(i).getLogin())) {
                    continue;
                }
                
                String login = team.get(i).getLogin();
                List<Integer> scores = new ArrayList<>();

                // Get just the commits for this team member
                List<Commit> commitsForName = 
                        CurrentProject.getCommitList().getAllCommitsByAuthor(login);

                Date start;
                Date end;
                start = sprint.getStartDate().getDate();
                end = sprint.getEndDate().getDate();
                    
                // get days between start and end
                long diff = end.getTime() - start.getTime();
                long dayCount = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

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
                                metricCount++;
                            } else {
                                metricCount++;
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

                graph.displayGraph(login + " ("
                                + sprintTitles.get(_sprint.getSelectedIndex())
                                + ")", image);
                }
            }
        });
        {
            JButton btnCancel = new JButton("Cancel");
            btnCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            getContentPane().add(btnCancel, "1, 14, 6, 1, fill, default");
        }
    }
}
