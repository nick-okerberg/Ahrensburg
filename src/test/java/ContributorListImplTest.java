package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import main.java.memoranda.IContributorList;
import main.java.memoranda.ContributorListImpl;
import main.java.memoranda.util.Contributor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ContributorListImplTest {
    IContributorList contributorListFromFile;
    IContributorList contributorListNew;
    Contributor contrib; 
    Contributor contrib2;
    String filePathFull;
    String filePathEmpty;
    
    /**
     * Builds two contributors and two contributor lists, one empty, one full.
     */
    @Before
    public void build() {
        filePathFull = "full.xml";
        filePathEmpty = "empty.xml";
        
        
        ArrayList<String> reviewers = new ArrayList<String>();
        reviewers.add("1");
        reviewers.add("2");
        ArrayList<String> apcontribovers = new ArrayList<String>();
        apcontribovers.add("1");
        apcontribovers.add("2");
        contrib = new Contributor("login1", "url1", "name1");
        contrib2 = new Contributor("login2", "url2", "name2  ");
        
        // Manually create a file to test with
        String contribListFull = ""
                + "<?xml version=\"1.0\"?>\r\n"
                + "<contributor-list>"
                + "<contributor login=\"DrChunks\" "
                + "avatar_url=\"https://avatars1.githubusercontent.com/u/22462688?v=4\" "
                + "name=\"Jordan Wine\" />"
                + "</contributor-list>";
        String contribListEmpty = "<?xml version=\"1.0\"?>\r\n<contributor-list />";
        try {
            PrintWriter writer1 = new PrintWriter(filePathFull);
            writer1.println(contribListFull);
            writer1.close();
            PrintWriter writer2 = new PrintWriter(filePathEmpty);
            writer2.println(contribListEmpty);
            writer2.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        contributorListFromFile = new ContributorListImpl(filePathFull);
        contributorListNew = new ContributorListImpl(filePathEmpty, true);
    }
    
    /**
     * Cleans up files created during testing.
     */
    @After
    public void removeTestFiles() {
        File full = new File(filePathFull);
        File empty = new File(filePathEmpty);
        full.delete();
        empty.delete();
    }

    @Test
    public void emptyFileHasNoContributors() {
        assertTrue(contributorListNew.getAllContributorsCount() == 0);
    }

    @Test
    public void fullFileHas1Contributor() {
        assertTrue(contributorListFromFile.getAllContributorsCount() == 1);
    }
    
    @Test
    public void testGetContributorsEmpty() {
        assertTrue(contributorListNew.getAllContributors().size() == 0);
    }
    
    @Test
    public void testRemoveContributorExist() {
        contributorListFromFile.addContributor(contrib);
        contributorListFromFile.addContributor(contrib2);
        assertTrue(contributorListFromFile.removeContributor(contrib2.getLogin()));
        assertEquals(contributorListFromFile.getContributor(contrib2.getLogin()),null);
        assertEquals(contributorListFromFile.getAllContributorsCount(),2);
    }
    
    @Test
    public void testRemoveContributorInvalid() {
        assertFalse(contributorListFromFile.removeContributor("nothere"));
    }
    
    @Test public void testGetContributorExist() {
        contributorListFromFile.addContributor(contrib);
        
        Contributor dupContributor = contributorListFromFile.getContributor(contrib.getLogin());
        assertEquals(dupContributor.compareTo(contrib),0);
        
    }
    
    @Test public void testGetContributorInvalid() {
        assertNull(contributorListFromFile.getContributor("nothere"));
    }
    
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    
    /**
     * Included as an exmple of how to get the avatar and display it.
     * @throws InterruptedException
     */
    public void displayAvatar() throws InterruptedException {
        
        Image image = null;
        try {
            URL url = new URL(contributorListFromFile.getAllContributors().get(0).getAvatarUrl());
            image = getScaledImage(ImageIO.read(url), 50, 50);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        JFrame frame = new JFrame();
        frame.setSize(400, 400);;
        JLabel label = new JLabel(new ImageIcon(image));
        frame.add(label);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        Thread.sleep(20000);
        
    }

}
