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
import java.util.Collection;
import java.util.List;
import java.util.Random;
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
			lblSelectSprint.setFont(new Font("Super Mario 256", Font.PLAIN, 18));
			getContentPane().add(lblSelectSprint, BorderLayout.NORTH);
		}
		DefaultListModel listModel = new DefaultListModel();
		Collection<Task> listOfSprints = CurrentProject.getTaskList().getTopLevelTasks();
		for(Task task : listOfSprints) {
			listModel.addElement(task.getText());
		}
		JList list = new JList(listModel);
		list.setSelectionMode(0);
		getContentPane().add(list, BorderLayout.CENTER);
		
		Button button = new Button("Select");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] gitNames = CurrentProject.get().getGitNames().split(",");
		        List<Integer> scores = new ArrayList<>();
		        Random random = new Random();
		        int maxDataPoints = 40;
		        int maxScore = 10;
		        for (int i = 0; i < maxDataPoints; i++) {
		            scores.add((int) random.nextInt() % maxScore);
		        }
		        TeamMemberGraph test244;
				for(int i = 0; i < gitNames.length; i++) {
					test244 = new TeamMemberGraph(scores);
					test244.displayGraph();
				}
				
				
			}
		});
		getContentPane().add(button, BorderLayout.SOUTH);
	}

}
