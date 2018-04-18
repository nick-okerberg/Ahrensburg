package test.java;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import main.java.memoranda.CommitList;
import main.java.memoranda.CommitListImpl;
import main.java.memoranda.util.Commit;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

public class CommitListImplTest {
    String path= "C:\\Users\\jorda\\.memoranda\\-42bb0695-4dca-78b4-216a\\.commits";
    CommitList cl = new CommitListImpl(path, true);
    
    Commit cmt = new Commit("1", "1","2018-04-14T14:36:43Z","1", "1","1",1,1,1);
    Commit cmt2 = new Commit("12", "1","2018-04-14T14:36:43Z","1", "1","1",1,1,1);
    

    @Test
    public void test() {
        Document doc = cl.getXmlContent();
        Element root = doc.getRootElement();
        int count  = root.getChildCount();
        System.out.println("commit count: " + count);
        
        cl.addCommit(cmt);
        doc = cl.getXmlContent();
        root = doc.getRootElement();
        count  = root.getChildCount();
        System.out.println("commit count: " + count);
        
        try {
            Serializer serializer = new Serializer(System.out, "ISO-8859-1");
            serializer.setIndent(4);
            serializer.setMaxLength(64);
            serializer.write(doc);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        cl.addCommit(cmt2);
        doc = cl.getXmlContent();
        root = doc.getRootElement();
        count  = root.getChildCount();
        System.out.println("commit count: " + count);
        
        try {
            Serializer serializer = new Serializer(System.out, "ISO-8859-1");
            serializer.setIndent(4);
            serializer.setMaxLength(64);
            serializer.write(doc);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
