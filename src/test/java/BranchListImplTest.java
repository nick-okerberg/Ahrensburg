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

import main.java.memoranda.BranchList;
import main.java.memoranda.BranchListImpl;
import main.java.memoranda.util.Branch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BranchListImplTest {
    BranchList branchListFromFile;
    BranchList branchListNew;
    Branch branch; 
    Branch branch2;
    String filePathFull;
    String filePathEmpty;
    
    /**
     * Builds two branchs and two branch lists, one empty, one full.
     */
    @Before
    public void build() {
        filePathFull = "full.xml";
        filePathEmpty = "empty.xml";
        
        branch = new Branch("name1");
        branch2 = new Branch("name2  ");
        
        // Manually create a file to test with
        String branchListFull = ""
                + "<?xml version=\"1.0\"?>\r\n"
                + "<branch-list>"
                + "<branch name=\"US46\" />"
                + "</branch-list>";
        String branchListEmpty = "<?xml version=\"1.0\"?>\r\n<branch-list />";
        try {
            PrintWriter writer1 = new PrintWriter(filePathFull);
            writer1.println(branchListFull);
            writer1.close();
            PrintWriter writer2 = new PrintWriter(filePathEmpty);
            writer2.println(branchListEmpty);
            writer2.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        branchListFromFile = new BranchListImpl(filePathFull);
        branchListNew = new BranchListImpl(filePathEmpty, true);
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
    public void emptyFileHasNoBranchs() {
        assertTrue(branchListNew.getAllBranchesCount() == 0);
    }

    @Test
    public void fullFileHas1Branch() {
        assertTrue(branchListFromFile.getAllBranchesCount() == 1);
    }
    
    @Test
    public void testGetBranchsEmpty() {
        assertTrue(branchListNew.getAllBranches().size() == 0);
    }
    
    @Test
    public void testRemoveBranchExist() {
        branchListFromFile.addBranch(branch);
        branchListFromFile.addBranch(branch2);
        assertTrue(branchListFromFile.removeBranch(branch2.getName()));
        assertEquals(branchListFromFile.getBranch(branch2.getName()),null);
        assertEquals(branchListFromFile.getAllBranchesCount(),2);
    }
    
    @Test
    public void testRemoveBranchInvalid() {
        assertFalse(branchListFromFile.removeBranch("nothere"));
    }
    
    @Test public void testGetBranchExist() {
        branchListFromFile.addBranch(branch);
        
        Branch dupBranch = branchListFromFile.getBranch(branch.getName());
        assertEquals(dupBranch.compareTo(branch),0);
        
    }
    
    @Test public void testGetBranchInvalid() {
        assertNull(branchListFromFile.getBranch("nothere"));
    }

}
