package main.java.memoranda.ui;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

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
import main.java.memoranda.Task;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
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
            //_teamMember.setColumns(10);
        }
        {
            JLabel lblSprint = new JLabel("Sprint");
            getContentPane().add(lblSprint, "2, 6, 5, 1, left, default");
        }
        {
            
            _sprint = new JComboBox();
            Collection<Task> test = CurrentProject.getTaskList().getTopLevelTasks();
            for(Task t : test) {
                _sprint.addItem((String) t.getText());
            }
            getContentPane().add(_sprint, "2, 8, 5, 1, fill, default");
        }
        {
            JButton btnNewButton = new JButton("Ok");
            getContentPane().add(btnNewButton, "1, 12, 6, 1, fill, default");
            btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Not implemented yet");
                }
            });
        }
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
