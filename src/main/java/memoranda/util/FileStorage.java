/**
 * Storage.java
 * Created on 12.02.2003, 0:21:40 Alex
 * Package: net.sf.memoranda.util
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import main.java.memoranda.IBranchList;
import main.java.memoranda.BranchListImpl;
import main.java.memoranda.ICommitList;
import main.java.memoranda.CommitListImpl;
import main.java.memoranda.IContributorList;
import main.java.memoranda.ContributorListImpl;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.EventsManager;
import main.java.memoranda.INote;
import main.java.memoranda.INoteList;
import main.java.memoranda.NoteListImpl;
import main.java.memoranda.IProject;
import main.java.memoranda.ProjectManager;
import main.java.memoranda.IPullRequestList;
import main.java.memoranda.PullRequestListImpl;
import main.java.memoranda.IResourcesList;
import main.java.memoranda.ResourcesListImpl;
import main.java.memoranda.ITaskList;
import main.java.memoranda.TaskListImpl;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.ui.ExceptionDialog;
import main.java.memoranda.ui.Loading;
import main.java.memoranda.ui.PullRequestPanel;
import main.java.memoranda.ui.htmleditor.AltHTMLWriter;
import nu.xom.Builder;
import nu.xom.Document;


/**
 *
 */
/*$Id: FileStorage.java,v 1.15 2006/10/09 23:31:58 alexeya Exp $*/
public class FileStorage implements IStorage {

    public static String JN_DOCPATH = Util.getEnvDir();
    private HTMLEditorKit editorKit = new HTMLEditorKit();

    public FileStorage() {
        /*The 'MEMORANDA_HOME' key is an undocumented feature for 
          hacking the default location (Util.getEnvDir()) of the memoranda 
          storage dir. Note that memoranda.config file is always placed at fixed 
          location (Util.getEnvDir()) anyway */
        String mHome = (String) Configuration.get("MEMORANDA_HOME");
        if (mHome.length() > 0) {
            JN_DOCPATH = mHome;
            /*DEBUG*/
        	System.out.println("[DEBUG]***Memoranda storage path has set to: " +
        	 JN_DOCPATH);
        }
    }

    public static void saveDocument(Document doc, String filePath) {
        /**
         * @todo: Configurable parameters
         */
        try {
            /*The XOM bug: reserved characters are not escaped*/
            //Serializer serializer = new Serializer(new FileOutputStream(filePath), "UTF-8");
            //serializer.write(doc);
        	
            OutputStreamWriter fw =
                new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
            fw.write(doc.toXML());
            fw.flush();
            fw.close();
        }
        catch (IOException ex) {
            System.out.println("Project already deleted");
        }
    }

    public static Document openDocument(InputStream in) throws Exception {
        Builder builder = new Builder();
        return builder.build(new InputStreamReader(in, "UTF-8"));
    }

    public static Document openDocument(String filePath) {
        try {
            return openDocument(new FileInputStream(filePath));
        }
        catch (Exception ex) {
            new ExceptionDialog(
                ex,
                "Failed to read a document from " + filePath,
                "");
        }
        return null;
    }

    public static boolean documentExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * @see main.java.memoranda.util.IStorage#storeNote(main.java.memoranda.INote)
     */
    public void storeNote(INote note, javax.swing.text.Document doc) {
        String filename =
            JN_DOCPATH + note.getProject().getID() + File.separator;
        doc.putProperty(
            javax.swing.text.Document.TitleProperty,
            note.getTitle());        
        CalendarDate d = note.getDate();

        filename += note.getId();//d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        /*DEBUG*/System.out.println("[DEBUG] Save note: "+ filename);

        try {
            OutputStreamWriter fw =
                new OutputStreamWriter(new FileOutputStream(filename), "UTF-8");
            AltHTMLWriter writer = new AltHTMLWriter(fw, (HTMLDocument) doc);
            writer.write();
            fw.flush();
            fw.close();
            //editorKit.write(new FileOutputStream(filename), doc, 0, doc.getLength());
            //editorKit.write(fw, doc, 0, doc.getLength());
        }
        catch (Exception ex) {
            new ExceptionDialog(
                ex,
                "Failed to write a document to " + filename,
                "");
        }
        /*String filename = JN_DOCPATH + note.getProject().getID() + "/";
        doc.putProperty(javax.swing.text.Document.TitleProperty, note.getTitle());
        CalendarDate d = note.getDate();
        filename += d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        try {
            long t1 = new java.util.Date().getTime();
            FileOutputStream ostream = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(ostream);
        
            oos.writeObject((HTMLDocument)doc);
        
            oos.flush();
            oos.close();
            ostream.close();
            long t2 = new java.util.Date().getTime();
            System.out.println(filename+" save:"+ (t2-t1) );
        }
            catch (Exception ex) {
                ex.printStackTrace();
            }*/

    }
    /**
     * @see main.java.memoranda.util.IStorage#openNote(main.java.memoranda.INote)
     */
    public javax.swing.text.Document openNote(INote note) {

        HTMLDocument doc = (HTMLDocument) editorKit.createDefaultDocument();
        if (note == null)
            return doc;
        /*
                String filename = JN_DOCPATH + note.getProject().getID() + File.separator;
                CalendarDate d = note.getDate();
                filename += d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        */
        String filename = getNotePath(note);
        try {
            /*DEBUG*/

//            Util.debug("Open note: " + filename);
//        	Util.debug("Note Title: " + note.getTitle());
        	doc.setBase(new URL(getNoteURL(note)));
        	editorKit.read(
                new InputStreamReader(new FileInputStream(filename), "UTF-8"),
                doc,
                0);
        }
        catch (Exception ex) {
            System.out.println("Project already deleted");
            //ex.printStackTrace();
            // Do nothing - we've got a new empty document!
        }
        
        return doc;
        /*HTMLDocument doc = (HTMLDocument)editorKit.createDefaultDocument();
        if (note == null) return doc;
        String filename = JN_DOCPATH + note.getProject().getID() + "/";
        CalendarDate d = note.getDate();
        filename += d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
        try {
            long t1 = new java.util.Date().getTime();
            FileInputStream istream = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(istream);
            doc = (HTMLDocument)ois.readObject();
            ois.close();
            istream.close();
            long t2 = new java.util.Date().getTime();
            System.out.println(filename+" open:"+ (t2-t1) );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return doc;*/
    }

    public String getNoteURL(INote note) {        
        return "file:" + JN_DOCPATH + note.getProject().getID() + "/" + note.getId();
    }

   public String getNotePath(INote note) {
        String filename = JN_DOCPATH + note.getProject().getID() + File.separator;
//        CalendarDate d = note.getDate();
        filename += note.getId();//d.getDay() + "-" + d.getMonth() + "-" + d.getYear();
	return filename;
   }


    public void removeNote(INote note) {
        File f = new File(getNotePath(note));
        /*DEBUG*/
        System.out.println("[DEBUG] Remove note:" + getNotePath(note));
        f.delete();
    }

    /**
     * @see main.java.memoranda.util.IStorage#openProjectManager()
     */
    public void openProjectManager() {
        if (!new File(JN_DOCPATH + ".projects").exists()) {
            ProjectManager._doc = null;
            return;
        }
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Open project manager: " + JN_DOCPATH + ".projects");
        ProjectManager._doc = openDocument(JN_DOCPATH + ".projects");
    }
    /**
     * @see main.java.memoranda.util.IStorage#storeProjectManager(nu.xom.Document)
     */
    public void storeProjectManager() {
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Save project manager: " + JN_DOCPATH + ".projects");
        saveDocument(ProjectManager._doc, JN_DOCPATH + ".projects");
    }
    /**
     * @see main.java.memoranda.util.IStorage#removeProject(main.java.memoranda.IProject)
     */
    public void removeProjectStorage(IProject prj) {
        String id = prj.getID();
        File f = new File(JN_DOCPATH + id);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++)
            files[i].delete();
        f.delete();
    }

    public ITaskList openTaskList(IProject prj) {
        String fn = JN_DOCPATH + prj.getID() + File.separator + ".tasklist";

        if (documentExists(fn)) {
            /*DEBUG*/
            System.out.println(
                "[DEBUG] Open task list: "
                    + JN_DOCPATH
                    + prj.getID()
                    + File.separator
                    + ".tasklist");
            
            Document tasklistDoc = openDocument(fn);
            /*DocType tasklistDoctype = tasklistDoc.getDocType();
            String publicId = null;
            if (tasklistDoctype != null) {
                publicId = tasklistDoctype.getPublicID();
            }
            boolean upgradeOccurred = TaskListVersioning.upgradeTaskList(publicId);
            if (upgradeOccurred) {
                // reload from new file
                tasklistDoc = openDocument(fn);
            }*/
            return new TaskListImpl(tasklistDoc, prj);   
        }
        else {
            /*DEBUG*/
            System.out.println("[DEBUG] New task list created");
            return new TaskListImpl(prj);
        }
    }

    public void storeTaskList(ITaskList tasklist, IProject prj) {
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Save task list: "
                + JN_DOCPATH
                + prj.getID()
                + File.separator
                + ".tasklist");
        Document tasklistDoc = tasklist.getXMLContent();
        //tasklistDoc.setDocType(TaskListVersioning.getCurrentDocType());
        saveDocument(tasklistDoc,JN_DOCPATH + prj.getID() + File.separator + ".tasklist");
    }
    /**
     * @see main.java.memoranda.util.IStorage#createProjectStorage(main.java.memoranda.IProject)
     */
    public void createProjectStorage(IProject prj) {
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Create project dir: " + JN_DOCPATH + prj.getID());
        File dir = new File(JN_DOCPATH + prj.getID());
        dir.mkdirs();
    }
    /**
     * @see main.java.memoranda.util.IStorage#openNoteList(main.java.memoranda.IProject)
     */
    public INoteList openNoteList(IProject prj) {
        String fn = JN_DOCPATH + prj.getID() + File.separator + ".notes";
      //System.out.println(fn);
        if (documentExists(fn)) {
            /*DEBUG*/
            System.out.println(
                "[DEBUG] Open note list: "
                    + JN_DOCPATH
                    + prj.getID()
                    + File.separator
                    + ".notes");
            return new NoteListImpl(openDocument(fn), prj);
        }
        else {
            /*DEBUG*/
            System.out.println("[DEBUG] New note list created");
            return new NoteListImpl(prj);
        }
    }
    /**
     * @see main.java.memoranda.util.IStorage#storeNoteList(main.java.memoranda.INoteList, main.java.memoranda.IProject)
     */
    public void storeNoteList(INoteList nl, IProject prj) {
        /*DEBUG*/
        try {
            System.out.println(
                    "[DEBUG] Save note list: "
                        + JN_DOCPATH
                        + prj.getID()
                        + File.separator
                        + ".notes");
                saveDocument(
                    nl.getXMLContent(),
                    JN_DOCPATH + prj.getID() + File.separator + ".notes");
        }
        catch(Exception e) {
            
        }

    }
    /**
     * @see main.java.memoranda.util.IStorage#openEventsList()
     */
    public void openEventsManager() {
        if (!new File(JN_DOCPATH + ".events").exists()) {
            EventsManager._doc = null;
            return;
        }
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Open events manager: " + JN_DOCPATH + ".events");
        EventsManager._doc = openDocument(JN_DOCPATH + ".events");
    }
    /**
     * @see main.java.memoranda.util.IStorage#storeEventsList()
     */
    public void storeEventsManager() {
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Save events manager: " + JN_DOCPATH + ".events");
        saveDocument(EventsManager._doc, JN_DOCPATH + ".events");
    }
    /**
     * @see main.java.memoranda.util.IStorage#openMimeTypesList()
     */
    public void openMimeTypesList() {
        if (!new File(JN_DOCPATH + ".mimetypes").exists()) {
            try {
                MimeTypesList._doc =
                    openDocument(
                        FileStorage.class.getResourceAsStream(
                            "/util/default.mimetypes"));
            }
            catch (Exception e) {
                new ExceptionDialog(
                    e,
                    "Failed to read default mimetypes config from resources",
                    "");
            }
            return;
        }
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Open mimetypes list: " + JN_DOCPATH + ".mimetypes");
        MimeTypesList._doc = openDocument(JN_DOCPATH + ".mimetypes");
    }
    /**
     * @see main.java.memoranda.util.IStorage#storeMimeTypesList()
     */
    public void storeMimeTypesList() {
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Save mimetypes list: " + JN_DOCPATH + ".mimetypes");
        saveDocument(MimeTypesList._doc, JN_DOCPATH + ".mimetypes");
    }
    /**
     * @see main.java.memoranda.util.IStorage#openResourcesList(main.java.memoranda.IProject)
     */
    public IResourcesList openResourcesList(IProject prj) {
        String fn = JN_DOCPATH + prj.getID() + File.separator + ".resources";
        if (documentExists(fn)) {
            /*DEBUG*/
            System.out.println("[DEBUG] Open resources list: " + fn);
            return new ResourcesListImpl(openDocument(fn), prj);
        }
        else {
            /*DEBUG*/
            System.out.println("[DEBUG] New note list created");
            return new ResourcesListImpl(prj);
        }
    }
    /**
     * @see main.java.memoranda.util.IStorage#storeResourcesList(main.java.memoranda.IResourcesList, main.java.memoranda.IProject)
     */
    public void storeResourcesList(IResourcesList rl, IProject prj) {
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Save resources list: "
                + JN_DOCPATH
                + prj.getID()
                + File.separator
                + ".resources");
        saveDocument(
            rl.getXMLContent(),
            JN_DOCPATH + prj.getID() + File.separator + ".resources");
    }
    
    /**
     * @see main.java.memoranda.util.IStorage#openCommitList(main.java.memoranda.IProject)
     */
    public ICommitList openCommitList(IProject prj) {
        String fn = JN_DOCPATH + prj.getID() + File.separator + ".commits";
        if (documentExists(fn)) {
            /*DEBUG*/
            System.out.println("[DEBUG] Open commit list: " + fn);
            return new CommitListImpl(fn);
        } else {
            /*DEBUG*/
            System.out.println("[DEBUG] New commit list created");
            return new CommitListImpl(fn, true);
        }
    }
    /**
     * @see main.java.memoranda.util.IStorage#storeResourcesList(main.java.memoranda.IResourcesList, main.java.memoranda.IProject)
     */
    public void storeCommitList(ICommitList cl, IProject prj) {
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Save commit list: "
                + JN_DOCPATH
                + prj.getID()
                + File.separator
                + ".commits");
        saveDocument(
                cl.getXmlContent(),
            JN_DOCPATH + prj.getID() + File.separator + ".commits");
        Util.debug("saved cl: " + cl);
        Loading.destroy();
    }
    
    /**
     * @see main.java.memoranda.util.IStorage#openPullRequestListList(main.java.memoranda.IProject)
     */
    public IPullRequestList openPullRequestList(IProject prj) {
        String fn = JN_DOCPATH + prj.getID() + File.separator + ".pullrequests";
        if (documentExists(fn)) {
            /*DEBUG*/
            System.out.println("[DEBUG] Open pull request list: " + fn);
            return new PullRequestListImpl(fn);
        } else {
            /*DEBUG*/
            System.out.println("[DEBUG] New pull request list created");
            return new PullRequestListImpl(fn, true);
        }
    }
    
    /**
     * @see main.java.memoranda.util.IStorage#storePullRequestList
     */
    public void storePullRequestList(IPullRequestList prl, IProject prj) {
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Save pull request list: "
                + JN_DOCPATH
                + prj.getID()
                + File.separator
                + ".pullrequests");
        saveDocument(
                prl.getXmlContent(),
            JN_DOCPATH + prj.getID() + File.separator + ".pullrequests");
        Util.debug("saved prl: " + prl);
        Loading.destroy();
    }
    
    /**
     * @see main.java.memoranda.util.IStorage#openPullRequestListList(main.java.memoranda.IProject)
     */
    public IContributorList openContributorList(IProject prj) {
        String fn = JN_DOCPATH + prj.getID() + File.separator + ".contributors";
        if (documentExists(fn)) {
            /*DEBUG*/
            System.out.println("[DEBUG] Open pull request list: " + fn);
            return new ContributorListImpl(fn);
        } else {
            /*DEBUG*/
            System.out.println("[DEBUG] New pull request list created");
            return new ContributorListImpl(fn, true);
        }
    }
    
    /**
     * @see main.java.memoranda.util.IStorage#storePullRequestList
     */
    public void storeContributorList(IContributorList contriblist, IProject prj) {
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Save pull request list: "
                + JN_DOCPATH
                + prj.getID()
                + File.separator
                + ".contributors");
        saveDocument(
                contriblist.getXmlContent(),
            JN_DOCPATH + prj.getID() + File.separator + ".contributors");
        Util.debug("saved prl: " + contriblist);
        Loading.destroy();
    }
    
    /**
     * @see main.java.memoranda.util.IStorage#openPullRequestListList(main.java.memoranda.IProject)
     */
    public IBranchList openBranchList(IProject prj) {
        String fn = JN_DOCPATH + prj.getID() + File.separator + ".branches";
        if (documentExists(fn)) {
            /*DEBUG*/
            System.out.println("[DEBUG] Open pull request list: " + fn);
            return new BranchListImpl(fn);
        } else {
            /*DEBUG*/
            System.out.println("[DEBUG] New pull request list created");
          
            return new BranchListImpl(fn, true);
        }
    }
    
    /**
     * @see main.java.memoranda.util.IStorage#storePullRequestList
     */
    public void storeBranchList(IBranchList branchList, IProject prj) {
        /*DEBUG*/
        System.out.println(
            "[DEBUG] Save pull request list: "
                + JN_DOCPATH
                + prj.getID()
                + File.separator
                + ".branches");
        saveDocument(
                branchList.getXmlContent(),
            JN_DOCPATH + prj.getID() + File.separator + ".branches");
        Util.debug("saved prl: " + branchList);
    }
    
    /**
     * @see main.java.memoranda.util.IStorage#restoreContext()
     */
    public void restoreContext() {
        try {
            /*DEBUG*/
            System.out.println(
                "[DEBUG] Open context: " + JN_DOCPATH + ".context");
            Context.context.load(new FileInputStream(JN_DOCPATH + ".context"));
        }
        catch (Exception ex) {
            /*DEBUG*/
            System.out.println("Context created.");
        }
    }
    
    /**
     * @see main.java.memoranda.util.IStorage#storeContext()
     */
    public void storeContext() {
        try {
            /*DEBUG*/
            System.out.println(
                "[DEBUG] Save context: " + JN_DOCPATH + ".context");
            Context.context.save(new FileOutputStream(JN_DOCPATH + ".context"));
        }
        catch (Exception ex) {
            new ExceptionDialog(
                ex,
                "Failed to store context to " + JN_DOCPATH + ".context",
                "");
        }
    }

}
