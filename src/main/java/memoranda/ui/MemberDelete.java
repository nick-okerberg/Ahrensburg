package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.java.memoranda.CurrentProject;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MemberDelete extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField username;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MemberDelete dialog = new MemberDelete();
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
	public MemberDelete() {
		setTitle("Delete Member");
		setBounds(100, 100, 413, 164);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JLayeredPane layeredPane = new JLayeredPane();
			contentPanel.add(layeredPane);
			
			JLabel lblGithubUsername = new JLabel("Github Username:");
			lblGithubUsername.setBounds(10, 11, 135, 14);
			layeredPane.add(lblGithubUsername);
			
			username = new JTextField();
			username.setBounds(10, 36, 366, 20);
			layeredPane.add(username);
			username.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(username.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Fill out form values pretty peas");
						}
						else {
							if(CurrentProject.get().deleteMember(username.getText())) {
								dispose();
			          App.getFrame().refreshAgenda();
							}
							else {
								JOptionPane.showMessageDialog(null, "Username not found");
							}
//							String usernameToDelete = username.getText(),
//									xmlGitNames = CurrentProject.get().getGitNames(),
//									xmlNames = CurrentProject.get().getNames(),
//									xmlArrayGitNames[] = xmlGitNames.split(","),
//									xmlArrayNames[] = xmlNames.split(","),
//									resultGitNames = "",
//									resultNames = "";
//							if(CurrentProject.get().getGitNames().contains(usernameToDelete)) {
//								for(int i = 0; i < xmlArrayGitNames.length; i++) {
//									if(xmlArrayGitNames[i].equals(usernameToDelete)) {
//										//ignore
//									}
//									else {
//										resultGitNames += xmlArrayGitNames[i] + ",";
//										resultNames += xmlArrayNames[i] + ",";
//									}
//								}
//								CurrentProject.get().setGitName(resultGitNames);
//								CurrentProject.get().setName(resultNames);
//								TeamMember.deleteTeamMember(usernameToDelete);
//								dispose();
//							}
//							else {
//								JOptionPane.showMessageDialog(null, "Username not found");
//							}
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
