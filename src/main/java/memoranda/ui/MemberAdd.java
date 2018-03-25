package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.TeamMember;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MemberAdd extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField name;
	private JTextField gitusername;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MemberAdd dialog = new MemberAdd();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
	        Point loc = App.getFrame().getLocation();
	        Dimension frmSize = App.getFrame().getSize();
	        Dimension dlgSize = dialog.getSize();
	        dialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MemberAdd() {
		setTitle("Add Member");
		setBounds(100, 100, 414, 209);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JLayeredPane layeredPane = new JLayeredPane();
			contentPanel.add(layeredPane);
			
			name = new JTextField();
			name.setBounds(10, 39, 366, 20);
			layeredPane.add(name);
			name.setColumns(10);
			
			JLabel lblNewLabel = new JLabel("Name: ");
			lblNewLabel.setBounds(10, 14, 46, 14);
			layeredPane.add(lblNewLabel);
			
			JLabel lblGithubUsername = new JLabel("Github Username:");
			lblGithubUsername.setBounds(10, 74, 135, 14);
			layeredPane.add(lblGithubUsername);
			
			gitusername = new JTextField();
			gitusername.setBounds(10, 94, 366, 20);
			layeredPane.add(gitusername);
			gitusername.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(name.getText().equals("") || gitusername.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Fill out form values pretty peas");
						}
						else {
							TeamMember newMem = new TeamMember(name.getText(), gitusername.getText(), CurrentProject.get().getTitle());
							TeamMember.teamMemberList().add(newMem);
							System.out.println(name.getText());
							System.out.println(gitusername.getText());
							System.out.println(CurrentProject.get().getTitle());
							CurrentProject.get().setNames();
							dispose();
							
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
