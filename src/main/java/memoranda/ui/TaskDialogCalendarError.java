package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import main.java.memoranda.util.Configuration;
import main.java.memoranda.util.Local;

public class TaskDialogCalendarError extends JDialog implements WindowListener {
    
    public boolean CANCELLED = false;
	public JLabel header = new JLabel();
	JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JPanel bottomPanel = new JPanel(new BorderLayout());
	
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    JButton okB = new JButton();
	
	JPanel mainPanel = new JPanel(new BorderLayout());
	
    public TaskDialogCalendarError(Frame frame, String title) {
       super(frame, title, true);
       try {
           jbInit();
           pack();
       }
       catch (Exception ex) {
           new ExceptionDialog(ex);
       }
       super.addWindowListener(this);
    }

	void jbInit() throws Exception {
		this.setResizable(false);
        
		// Build headerPanel
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        header.setFont(new java.awt.Font("Dialog", 0, 20));
        header.setForeground(new Color(0, 0, 124));
        header.setText("Error");
        header.setIcon(new ImageIcon(main.java.memoranda.ui.EventDialog.class.getResource(
            "/ui/icons/exit.png")));
        headerPanel.add(header);
		
		// Build mainPanel
		JLabel confirm = new JLabel();
		confirm.setText("<HTML>Invalid selection of dates. <p>This request has overlapping dates with existing sprint: <p>NAME");
		
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.add(confirm,BorderLayout.CENTER);
		
	    // Build ButtonsPanel
        okB.setMaximumSize(new Dimension(100, 26));
        okB.setMinimumSize(new Dimension(100, 26));
        okB.setPreferredSize(new Dimension(100, 26));
        okB.setText(Local.getString("Ok"));
        okB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okB_actionPerformed(e);
            }
        });
        this.getRootPane().setDefaultButton(okB);
        buttonsPanel.add(okB);
		bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.getRootPane().setDefaultButton(okB);
		
		// Build dialog
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		this.getContentPane().add(headerPanel, BorderLayout.NORTH);
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}
	
	// if donotaskCB is checked update Configuration.
	public void  checkDoNotAsk() {
		Configuration.put("ASK_ON_EXIT", "no");
		Configuration.saveConfig();
	}
	
	// ok button action
    void okB_actionPerformed(ActionEvent e) {
		//checkDoNotAsk();
        this.dispose();
    }
	
    public void windowClosing( WindowEvent e ) {
        CANCELLED = true;
        this.dispose();
    }
    
	public void windowOpened( WindowEvent e ) {}
    public void windowClosed( WindowEvent e ) {}
	public void windowIconified( WindowEvent e ) {}
	public void windowDeiconified( WindowEvent e ) {}
	public void windowActivated( WindowEvent e ) {}
	public void windowDeactivated( WindowEvent e ) {}
}
