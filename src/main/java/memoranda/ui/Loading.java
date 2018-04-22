package main.java.memoranda.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
//from w w w . j  av a 2 s  .c o  m
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Font;

public class Loading {
	public static JFrame thisFrame;
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {

        
      }
    });
  }
  public static void destroy() {
	  try {
		  thisFrame.dispose();
	  }
	  catch(Exception e) {
		  System.out.println("nothing to desotry");
		  e.printStackTrace();
	  }
  }
  public static void display() {
      thisFrame = new JFrame();
      main.java.memoranda.ui.ImagePanel imagePanel = new ImagePanel();
      thisFrame.getContentPane().add(imagePanel);
      
      JLabel lblConnectingToGithub = new JLabel("Connecting to GitHub");
      lblConnectingToGithub.setFont(new Font("Tahoma", Font.PLAIN, 16));
      imagePanel.add(lblConnectingToGithub);

      thisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      thisFrame.setSize(400, 400);
      thisFrame.setVisible(true);
      Point loc = App.getFrame().getLocation();
      Dimension frmSize = App.getFrame().getSize();
      Dimension dlgSize = thisFrame.getSize();
      thisFrame.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
  }
}

class ImagePanel extends JPanel {

  Image image;

  public ImagePanel() {
    image = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") +"\\src\\main\\resources\\ui\\icons\\loading.gif");
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (image != null) {
      g.drawImage(image, 50, 50, this);
    }
  }

}