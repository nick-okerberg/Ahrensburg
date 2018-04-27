package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.security.auth.Refreshable;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import main.java.memoranda.ICommitList;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.IEventNotificationListener;
import main.java.memoranda.EventsManager;
import main.java.memoranda.EventsScheduler;
import main.java.memoranda.History;
import main.java.memoranda.INoteList;
import main.java.memoranda.IProject;
import main.java.memoranda.IProjectListener;
import main.java.memoranda.ProjectManager;
import main.java.memoranda.IPullRequestList;
import main.java.memoranda.IResourcesList;
import main.java.memoranda.ITaskList;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.date.CurrentDate;
import main.java.memoranda.date.DateListener;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Local;
import main.java.memoranda.util.PullRequestGenerator;
import main.java.memoranda.util.Util;
import main.java.memoranda.util.PullRequestGenerator.PullRequestsClass;
import javax.swing.JOptionPane;
import nu.xom.Element;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;

/*$Id: AgendaPanel.java,v 1.11 2005/02/15 16:58:02 rawsushi Exp $*/
public class PullRequestPanel extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JButton historyBackB = new JButton();
    JToolBar toolBar = new JToolBar();
    JButton historyForwardB = new JButton();
    JButton export = new JButton();
    JEditorPane viewer = new JEditorPane("text/html", "");
    String[] priorities = {"Muy Alta","Alta","Media","Baja","Muy Baja"};
    JScrollPane scrollPane = new JScrollPane();

    DailyItemsPanel parentPanel = null;

    //  JPopupMenu agendaPPMenu = new JPopupMenu();
    //  JCheckBoxMenuItem ppShowActiveOnlyChB = new JCheckBoxMenuItem();

    Collection expandedTasks;
    String gotoTask = null;

    boolean isActive = true;
    //US173
    private static JComboBox<String> JboxButton;
    private final JButton goButton = new JButton("Go");
    private final JButton refreshButton = new JButton("Refresh");
    

    public PullRequestPanel(DailyItemsPanel _parentPanel) {
        
        JboxButton = new JComboBox();
        JboxButton.setSize(5, 5);
        JboxButton.setEditable(false);
        JboxButton.addItem("Sprint Not Selected");
      
        
        try {
            parentPanel = _parentPanel;
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
            ex.printStackTrace();
        }
        
    }
    public static void loadJcomoBox(PullRequestsClass prc) {
        JboxButton.addItem(prc.getTask().toString());
        refreshJcomboBox();
        
                //  "   Start Date: " +  prc.getCdStart() +
                //  "   End Date: " +  prc.getCdEnd() );
    }
    void jbInit() throws Exception {
        expandedTasks = new ArrayList();

        toolBar.setFloatable(false);
        viewer.setEditable(false);
        viewer.setOpaque(false);
        viewer.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    String d = e.getDescription();
                    Util.debug("Link clicked with description: "+ d);
                    if (d.equalsIgnoreCase("memoranda:events"))
                        parentPanel.alarmB_actionPerformed(null);
                    else if (d.startsWith("memoranda:tasks")) {
                        String id = d.split("#")[1];
                        CurrentProject.set(ProjectManager.getProject(id));
                        parentPanel.taskB_actionPerformed(null);
                    } else if (d.startsWith("memoranda:project")) {
                        String id = d.split("#")[1];
                        CurrentProject.set(ProjectManager.getProject(id));
                    } else if (d.startsWith("memoranda:removesticker")) {
                        String id = d.split("#")[1];
                        StickerConfirmation stc = new StickerConfirmation(App.getFrame());
                        Dimension frmSize = App.getFrame().getSize();
                        stc.setSize(new Dimension(300,180));
                        Point loc = App.getFrame().getLocation();
                        stc.setLocation(
                                (frmSize.width - stc.getSize().width) / 2 + loc.x,
                                (frmSize.height - stc.getSize().height) / 2
                                        + loc.y);
                        stc.setVisible(true);
                        if (!stc.CANCELLED) {
                        EventsManager.removeSticker(id);
                        CurrentStorage.get().storeEventsManager();}
                        refresh(CurrentDate.get());
                    } else if (d.startsWith("memoranda:addsticker")) {
                        StickerDialog dlg = new StickerDialog(App.getFrame());
                        Dimension frmSize = App.getFrame().getSize();
                        dlg.setSize(new Dimension(300,380));
                        Point loc = App.getFrame().getLocation();
                        dlg.setLocation(
                                (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                                (frmSize.height - dlg.getSize().height) / 2
                                + loc.y);
                        dlg.setVisible(true);
                        if (!dlg.CANCELLED) {
                            String txt = dlg.getStickerText();
                            int sP = dlg.getPriority();
                            txt = txt.replaceAll("\\n", "<br>");
                            txt = "<div style=\"background-color:"+dlg.getStickerColor()+";font-size:"+dlg.getStickerTextSize()+";color:"+dlg.getStickerTextColor()+"; \">"+txt+"</div>";
                            EventsManager.createSticker(txt, sP);
                            CurrentStorage.get().storeEventsManager();
                        }
                        refresh(CurrentDate.get());
                        System.out.println("agreguÃ© un sticker");
                    } else if (d.startsWith("memoranda:expandsubtasks")) {
                        String id = d.split("#")[1];
                        gotoTask = id;
                        expandedTasks.add(id);
                        refresh(CurrentDate.get());
                    } else if (d.startsWith("memoranda:closesubtasks")) {
                        String id = d.split("#")[1];
                        gotoTask = id;
                        expandedTasks.remove(id);
                        refresh(CurrentDate.get());
                    } else if (d.startsWith("memoranda:expandsticker")) {
                        String id = d.split("#")[1];
                        Element pre_sticker=(Element)((Map)EventsManager.getStickers()).get(id);
                        String sticker = pre_sticker.getValue();
                        int first=sticker.indexOf(">");
                        int last=sticker.lastIndexOf("<");
                        int backcolor=sticker.indexOf("#");
                        int fontcolor=sticker.indexOf("#", backcolor+1);
                        int sP=Integer.parseInt(pre_sticker.getAttributeValue("priority"));
                        String backGroundColor=sticker.substring(backcolor, sticker.indexOf(';',backcolor));
                        String foreGroundColor=sticker.substring(fontcolor, sticker.indexOf(';',fontcolor));
                        sticker="<html>"+sticker.substring(first+1, last)+"</html>";
                        StickerExpand dlg = new StickerExpand(App.getFrame(),sticker,backGroundColor,foreGroundColor,Local.getString("priority")+": "+Local.getString(priorities[sP]));
                        Dimension frmSize = App.getFrame().getSize();
                        dlg.setSize(new Dimension(300,200));
                        Point loc = App.getFrame().getLocation();
                        dlg.setLocation(
                                (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                                (frmSize.height - dlg.getSize().height) / 2
                                + loc.y);
                        dlg.stickerText.setText(sticker);
                        dlg.setVisible(true);
                    }else if (d.startsWith("memoranda:editsticker")) {
                        String id = d.split("#")[1];
                        Element pre_sticker=(Element)((Map)EventsManager.getStickers()).get(id);
                        String sticker = pre_sticker.getValue();
                        sticker=sticker.replaceAll("<br>","\n");
                        int first=sticker.indexOf(">");
                        int last=sticker.lastIndexOf("<");
                        int backcolor=sticker.indexOf("#");
                        int fontcolor=sticker.indexOf("#", backcolor+1);
                        int sizeposition=sticker.indexOf("font-size")+10;
                        int size=Integer.parseInt(sticker.substring(sizeposition,sizeposition+2));
                        System.out.println(size+" "+sizeposition);
                        int sP=Integer.parseInt(pre_sticker.getAttributeValue("priority"));
                        String backGroundColor=sticker.substring(backcolor, sticker.indexOf(';',backcolor));
                        String foreGroundColor=sticker.substring(fontcolor, sticker.indexOf(';',fontcolor));
                        StickerDialog dlg = new StickerDialog(App.getFrame(), sticker.substring(first+1, last), backGroundColor, foreGroundColor, sP, size);
                        Dimension frmSize = App.getFrame().getSize();
                        dlg.setSize(new Dimension(300,380));
                        Point loc = App.getFrame().getLocation();
                        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x,
                                    (frmSize.height - dlg.getSize().height) / 2 + loc.y);
                        dlg.setVisible(true);
                        if (!dlg.CANCELLED) {
                            String txt = dlg.getStickerText();
                            sP = dlg.getPriority();
                            txt = txt.replaceAll("\\n", "<br>");
                            txt = "<div style=\"background-color:"+dlg.getStickerColor()+";font-size:"+dlg.getStickerTextSize()+";color:"+dlg.getStickerTextColor()+";\">"+txt+"</div>";
                            EventsManager.removeSticker(id);
                            EventsManager.createSticker(txt, sP);
                            CurrentStorage.get().storeEventsManager();
                         }
                         refresh(CurrentDate.get());
                    }else if (d.startsWith("memoranda:exportstickerst")) {
                         /*  Falta agregar el exportar sticker mientras tanto..*/
                         final JFrame parent = new JFrame();
                         String name = JOptionPane.showInputDialog(parent,Local.getString("Enter filename to export as txt"),null);
                         new ExportSticker(name).export("txt");
                         //JOptionPane.showMessageDialog(null,name);
                    }else if (d.startsWith("memoranda:exportstickersh")) {
                         /*  Falta agregar el exportar sticker mientras tanto..*/
                         final JFrame parent = new JFrame();
                         String name = JOptionPane.showInputDialog(parent,Local.getString("Enter filename to export as html"),null);
                         new ExportSticker(name).export("html");
                         //JOptionPane.showMessageDialog(null,name);
                    }else if (d.startsWith("memoranda:importstickers")) {
                        final JFrame parent = new JFrame();
                        String name = JOptionPane.showInputDialog(parent,Local.getString("Enter filename to import"),null);
                        new ImportSticker(name).import_file();
                    // US 35 adding external link for github
                    } else if (d.startsWith("https://github.com")) {
                      if (Desktop.isDesktopSupported()) {
                        try {
                Desktop.getDesktop().browse(new URI(d));
              } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
              } catch (URISyntaxException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
              }
                      }
                    }
                   
                }
            }
        });
        historyBackB.setAction(History.historyBackAction);
        historyBackB.setFocusable(false);
        historyBackB.setBorderPainted(false);
        historyBackB.setToolTipText(Local.getString("History back"));
        historyBackB.setRequestFocusEnabled(false);
        historyBackB.setPreferredSize(new Dimension(24, 24));
        historyBackB.setMinimumSize(new Dimension(24, 24));
        historyBackB.setMaximumSize(new Dimension(24, 24));
        historyBackB.setText("");

        historyForwardB.setAction(History.historyForwardAction);
        historyForwardB.setBorderPainted(false);
        historyForwardB.setFocusable(false);
        historyForwardB.setPreferredSize(new Dimension(24, 24));
        historyForwardB.setRequestFocusEnabled(false);
        historyForwardB.setToolTipText(Local.getString("History forward"));
        historyForwardB.setMinimumSize(new Dimension(24, 24));
        historyForwardB.setMaximumSize(new Dimension(24, 24));
        historyForwardB.setText("");
        
        this.setLayout(borderLayout1);
        
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setViewportView(viewer);
        this.add(scrollPane, BorderLayout.CENTER);
        toolBar.add(historyBackB, null);
        toolBar.add(historyForwardB, null);
        toolBar.addSeparator(new Dimension(8, 24));

        this.add(toolBar, BorderLayout.NORTH);
       
        
        //US173
        toolBar.add(JboxButton);
        JboxButton.setSize(10,10);
        
        JboxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
  
            
           
            }
        });
        
        toolBar.add(refreshButton);
        refreshButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshJcomboBox();
                
                
            }
        });

        //Go button in Pull request pages
        toolBar.add(goButton);
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
                try {
                    CurrentProject.get().addRepoName(CurrentProject.get().getGitHubRepoName());
                } catch (RuntimeException e1) {
                   
                    e1.printStackTrace();
                }
                App.getFrame().refreshAgenda();
            }
        });
        
        
        
        //END of US173
      
      
        CurrentDate.addDateListener(new DateListener() {
            public void dateChange(CalendarDate d) {
                if (isActive)
                    refresh(d);
            }
        });
        CurrentProject.addProjectListener(new IProjectListener() {

            public void projectChange(
                    IProject prj,
                    INoteList nl,
                    ITaskList tl,
                    IResourcesList rl,
                    ICommitList cl, IPullRequestList prl) {
            }

            public void projectWasChanged() {
                if (isActive)
                    refresh(CurrentDate.get());
            }});
        EventsScheduler.addListener(new IEventNotificationListener() {
            public void eventIsOccured(main.java.memoranda.IEvent ev) {
                if (isActive)
                    refresh(CurrentDate.get());
            }

            public void eventsChanged() {
                if (isActive)
                    refresh(CurrentDate.get());
            }
        });
        refresh(CurrentDate.get());

               
    }

    public void refresh(CalendarDate date) {
      //Util.debug(AgendaGenerator.getAgenda(date,expandedTasks));
        viewer.setText(PullRequestGenerator.getAgenda()); 
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(gotoTask != null) {
                    viewer.scrollToReference(gotoTask);
                    scrollPane.setViewportView(viewer);
                    Util.debug("Set view port to " + gotoTask);
                }
            }
        });

        Util.debug("Summary updated.");
    }

    public void setActive(boolean isa) {
        isActive = isa;
    }
    public static void JcomboBox(){
        
    }
    public static void refreshJcomboBox() {
        JboxButton.removeAllItems();
        JboxButton.addItem("Sprint Not Selected");
        ITaskList tl = CurrentProject.getTaskList();
        Collection coll = tl.getAllSubTasks(null);
        Object[] obj = coll.toArray();
         for(int i = 0; i < coll.size(); i++) {
              JboxButton.addItem( obj[i].toString());
           }
    }

    
}

