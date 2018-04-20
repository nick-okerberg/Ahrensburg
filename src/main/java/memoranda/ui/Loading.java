package main.java.memoranda.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
//from w w w . j  av a 2 s  .c o  m
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import java.awt.Font;

public class Loading {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        JFrame frame = new JFrame();
        main.java.memoranda.ui.ImagePanel imagePanel = new ImagePanel();
        frame.getContentPane().add(imagePanel);
        
        JLabel lblConnectingToGithub = new JLabel("Connecting to GitHub");
        lblConnectingToGithub.setFont(new Font("Tahoma", Font.PLAIN, 16));
        imagePanel.add(lblConnectingToGithub);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
      }
    });
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