package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.java.memoranda.ICommitList;
import main.java.memoranda.CurrentProject;
import main.java.memoranda.History;
import main.java.memoranda.INoteList;
import main.java.memoranda.IProject;
import main.java.memoranda.IProjectListener;
import main.java.memoranda.IPullRequestList;
import main.java.memoranda.IResourcesList;
import main.java.memoranda.ITask;
import main.java.memoranda.ITaskList;
import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.date.CurrentDate;
import main.java.memoranda.date.IDateListener;
import main.java.memoranda.util.Context;
import main.java.memoranda.util.CurrentStorage;
import main.java.memoranda.util.Local;

/*$Id: TaskPanel.java,v 1.27 2007/01/17 20:49:12 killerjoe Exp $*/
public class TaskPanel extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JButton historyBackB = new JButton();
    JToolBar tasksToolBar = new JToolBar();
    JButton historyForwardB = new JButton();
    JButton newTransB = new JButton(); // Nick Okerberg US43 Task 145
    JButton editTransB = new JButton(); // Nick Okerberg US43 Task 145
    JButton removeTransB = new JButton(); // Nick Okerberg US43 Task 145
    JButton completeTransB = new JButton(); // Nick Okerberg US43 Task 145
    JButton newTaskB = new JButton();
    // JButton subTaskB = new JButton(); Ovadia Shalom US32
    JButton editTaskB = new JButton();
    JButton removeTaskB = new JButton();
    JButton completeTaskB = new JButton();

    JCheckBoxMenuItem ppShowActiveOnlyChB = new JCheckBoxMenuItem();

    JScrollPane scrollPane = new JScrollPane();
    TaskTable taskTable = new TaskTable();
    JMenuItem ppEditTask = new JMenuItem();
    JPopupMenu taskPPMenu = new JPopupMenu();
    JMenuItem ppRemoveTask = new JMenuItem();
    JMenuItem ppNewTask = new JMenuItem();
    JMenuItem ppCompleteTask = new JMenuItem();
    // JMenuItem ppSubTasks = new JMenuItem();
    // JMenuItem ppParentTask = new JMenuItem();
    // JMenuItem ppAddSubTask = new JMenuItem();
    JMenuItem ppCalcTask = new JMenuItem();
    DailyItemsPanel parentPanel = null;

    public TaskPanel(DailyItemsPanel _parentPanel) {
        try {
            parentPanel = _parentPanel;
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        tasksToolBar.setFloatable(false);

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

        newTaskB.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                .getResource("/ui/icons/plus.png")));
        newTaskB.setEnabled(true);
        newTaskB.setMaximumSize(new Dimension(24, 24));
        newTaskB.setMinimumSize(new Dimension(24, 24));
        newTaskB.setToolTipText(Local.getString("Create new sprint"));
        newTaskB.setRequestFocusEnabled(false);
        newTaskB.setPreferredSize(new Dimension(24, 24));
        newTaskB.setFocusable(false);
        newTaskB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTaskB_actionPerformed(e);
            }
        });
        newTaskB.setBorderPainted(false);

        /*
         * US43 task 145 - Create a new Transition button on Sprint page.
         */
        newTransB.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                .getResource("/ui/icons/plus.png")));
        newTransB.setEnabled(true);
        newTransB.setMaximumSize(new Dimension(24, 24));
        newTransB.setMinimumSize(new Dimension(24, 24));
        newTransB.setToolTipText(Local.getString("Create new transition"));
        newTransB.setRequestFocusEnabled(false);
        newTransB.setPreferredSize(new Dimension(24, 24));
        newTransB.setFocusable(false);
        newTransB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(
                        "[DEBUG] Clicked on \"New Transition\" from Sprint page");
                // TODO
                // newTransB_actionPerformed(e);
            }
        });
        newTransB.setBorderPainted(false);

        // subTaskB.setIcon( Ovadia Shalom US32
        // new
        // ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/todo_new_sub.png")));
        // subTaskB.setEnabled(false);
        // subTaskB.setMaximumSize(new Dimension(24, 24));
        // subTaskB.setMinimumSize(new Dimension(24, 24));
        // subTaskB.setToolTipText(Local.getString("Add commit"));
        // subTaskB.setRequestFocusEnabled(false);
        // subTaskB.setPreferredSize(new Dimension(24, 24));
        // subTaskB.setFocusable(false);
        // subTaskB.addActionListener(new java.awt.event.ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // addSubTask_actionPerformed(e);
        // }
        // });
        // subTaskB.setBorderPainted(false);

        editTaskB.setBorderPainted(false);
        editTaskB.setFocusable(false);
        editTaskB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editTaskB_actionPerformed(e);
            }
        });
        editTaskB.setPreferredSize(new Dimension(24, 24));
        editTaskB.setRequestFocusEnabled(false);
        editTaskB.setToolTipText(Local.getString("Edit sprint"));
        editTaskB.setMinimumSize(new Dimension(24, 24));
        editTaskB.setMaximumSize(new Dimension(24, 24));
        // editTaskB.setEnabled(true);
        editTaskB.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                .getResource("/ui/icons/edit.png")));

        /*
         * US43 - Button to Edit a Transition, on the Sprint page.
         */
        editTransB.setBorderPainted(false);
        editTransB.setFocusable(false);
        editTransB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO:
                // editTransB_actionPerformed(e);
            }
        });
        editTransB.setPreferredSize(new Dimension(24, 24));
        editTransB.setRequestFocusEnabled(false);
        editTransB.setToolTipText(Local.getString("Edit transition"));
        editTransB.setMinimumSize(new Dimension(24, 24));
        editTransB.setMaximumSize(new Dimension(24, 24));
        // editTransB.setEnabled(true);
        editTransB.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                .getResource("/ui/icons/edit.png")));

        removeTaskB.setBorderPainted(false);
        removeTaskB.setFocusable(false);
        removeTaskB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeTaskB_actionPerformed(e);
            }
        });
        removeTaskB.setPreferredSize(new Dimension(24, 24));
        removeTaskB.setRequestFocusEnabled(false);
        removeTaskB.setToolTipText(Local.getString("Remove sprint"));
        removeTaskB.setMinimumSize(new Dimension(24, 24));
        removeTaskB.setMaximumSize(new Dimension(24, 24));
        removeTaskB.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                .getResource("/ui/icons/minus.png")));

        /*
         * US43 - Button to remove transition.
         */
        removeTransB.setBorderPainted(false);
        removeTransB.setFocusable(false);
        removeTransB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO:
                // removeTransB_actionPerformed(e);
            }
        });
        removeTransB.setPreferredSize(new Dimension(24, 24));
        removeTransB.setRequestFocusEnabled(false);
        removeTransB.setToolTipText(Local.getString("Remove transition"));
        removeTransB.setMinimumSize(new Dimension(24, 24));
        removeTransB.setMaximumSize(new Dimension(24, 24));
        removeTransB.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                .getResource("/ui/icons/minus.png")));

        completeTaskB.setBorderPainted(false);
        completeTaskB.setFocusable(false);
        completeTaskB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppCompleteTask_actionPerformed(e);
            }
        });
        completeTaskB.setPreferredSize(new Dimension(24, 24));
        completeTaskB.setRequestFocusEnabled(false);
        completeTaskB.setToolTipText(Local.getString("Complete sprint"));
        completeTaskB.setMinimumSize(new Dimension(24, 24));
        completeTaskB.setMaximumSize(new Dimension(24, 24));
        completeTaskB
                .setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                        .getResource("/ui/icons/check.png")));

        completeTaskB.setBorderPainted(false);
        completeTaskB.setFocusable(false);
        completeTaskB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppCompleteTask_actionPerformed(e);
            }
        });

        /*
         * US43 - Button to Complete the Transition.
         */
        completeTransB.setPreferredSize(new Dimension(24, 24));
        completeTransB.setRequestFocusEnabled(false);
        completeTransB.setToolTipText(Local.getString("Complete transition"));
        completeTransB.setMinimumSize(new Dimension(24, 24));
        completeTransB.setMaximumSize(new Dimension(24, 24));
        completeTransB
                .setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                        .getResource("/ui/icons/check.png")));
        completeTransB.setBorderPainted(false);
        completeTransB.setFocusable(false);
        completeTransB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO:
                // ppCompleteTrans_actionPerformed(e);
            }
        });

        // added by rawsushi
        // showActiveOnly.setBorderPainted(false);
        // showActiveOnly.setFocusable(false);
        // showActiveOnly.addActionListener(new java.awt.event.ActionListener()
        // {
        // public void actionPerformed(ActionEvent e) {
        // toggleShowActiveOnly_actionPerformed(e);
        // }
        // });
        // showActiveOnly.setPreferredSize(new Dimension(24, 24));
        // showActiveOnly.setRequestFocusEnabled(false);
        // if (taskTable.isShowActiveOnly()) {
        // showActiveOnly.setToolTipText(Local.getString("Show All"));
        // }
        // else {
        // showActiveOnly.setToolTipText(Local.getString("Show Active Only"));
        // }
        // showActiveOnly.setMinimumSize(new Dimension(24, 24));
        // showActiveOnly.setMaximumSize(new Dimension(24, 24));
        // showActiveOnly.setIcon(
        // new
        // ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("/ui/icons/todo_remove.png")));
        // added by rawsushi

        ppShowActiveOnlyChB.setFont(new java.awt.Font("Dialog", 1, 11));
        ppShowActiveOnlyChB.setText(Local.getString("Show Active only"));
        ppShowActiveOnlyChB
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        toggleShowActiveOnly_actionPerformed(e);
                    }
                });
        boolean isShao = (Context.get("SHOW_ACTIVE_TASKS_ONLY") != null)
                && (Context.get("SHOW_ACTIVE_TASKS_ONLY").equals("true"));
        ppShowActiveOnlyChB.setSelected(isShao);
        toggleShowActiveOnly_actionPerformed(null);

        /*
         * showActiveOnly.setPreferredSize(new Dimension(24, 24));
         * showActiveOnly.setRequestFocusEnabled(false); if
         * (taskTable.isShowActiveOnly()) {
         * showActiveOnly.setToolTipText(Local.getString("Show All")); } else {
         * showActiveOnly.setToolTipText(Local.getString("Show Active Only")); }
         * showActiveOnly.setMinimumSize(new Dimension(24, 24));
         * showActiveOnly.setMaximumSize(new Dimension(24, 24));
         * showActiveOnly.setIcon( new
         * ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource(
         * "/ui/icons/todo_active.png")));
         */
        // added by rawsushi

        this.setLayout(borderLayout1);
        scrollPane.getViewport().setBackground(Color.white);
        /*
         * taskTable.setMaximumSize(new Dimension(32767, 32767));
         * taskTable.setRowHeight(24);
         */
        ppEditTask.setFont(new java.awt.Font("Dialog", 1, 11));
        ppEditTask.setText(Local.getString("Edit sprint") + "...");
        ppEditTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppEditTask_actionPerformed(e);
            }
        });
        ppEditTask.setEnabled(false);
        ppEditTask.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                .getResource("/ui/icons/edit.png")));
        taskPPMenu.setFont(new java.awt.Font("Dialog", 1, 10));
        ppRemoveTask.setFont(new java.awt.Font("Dialog", 1, 11));
        ppRemoveTask.setText(Local.getString("Remove sprint"));
        ppRemoveTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppRemoveTask_actionPerformed(e);
            }
        });
        ppRemoveTask.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                .getResource("/ui/icons/minus.png")));
        ppRemoveTask.setEnabled(false);
        ppNewTask.setFont(new java.awt.Font("Dialog", 1, 11));
        ppNewTask.setText(Local.getString("New task") + "...");
        ppNewTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppNewTask_actionPerformed(e);
            }
        });
        ppNewTask.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                .getResource("/ui/icons/plus.png")));

        // ppAddSubTask.setFont(new java.awt.Font("Dialog", 1, 11)); Ovadia
        // Shalom US32
        // ppAddSubTask.setText(Local.getString("Add subtask"));
        // ppAddSubTask.addActionListener(new java.awt.event.ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // ppAddSubTask_actionPerformed(e);
        // }
        // });
        // ppAddSubTask.setIcon(new
        // ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/todo_new_sub.png")));

        /*
         * ppSubTasks.setFont(new java.awt.Font("Dialog", 1, 11));
         * ppSubTasks.setText(Local.getString("List sub tasks"));
         * ppSubTasks.addActionListener(new java.awt.event.ActionListener() {
         * public void actionPerformed(ActionEvent e) {
         * ppListSubTasks_actionPerformed(e); } }); ppSubTasks.setIcon(new
         * ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource(
         * "/ui/icons/todo_new.png")));
         * 
         * ppParentTask.setFont(new java.awt.Font("Dialog", 1, 11));
         * ppParentTask.setText(Local.getString("Parent Task"));
         * ppParentTask.addActionListener(new java.awt.event.ActionListener() {
         * public void actionPerformed(ActionEvent e) {
         * ppParentTask_actionPerformed(e); } }); ppParentTask.setIcon(new
         * ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource(
         * "/ui/icons/todo_new.png")));
         */

        ppCompleteTask.setFont(new java.awt.Font("Dialog", 1, 11));
        ppCompleteTask.setText(Local.getString("Complete sprint"));
        ppCompleteTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppCompleteTask_actionPerformed(e);
            }
        });
        ppCompleteTask
                .setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                        .getResource("/ui/icons/check.png")));
        ppCompleteTask.setEnabled(false);

        ppCalcTask.setFont(new java.awt.Font("Dialog", 1, 11));
        ppCalcTask.setText(Local.getString("Calculate task data"));
        ppCalcTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppCalcTask_actionPerformed(e);
            }
        });
        ppCalcTask.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class
                .getResource("/ui/icons/check.png")));
        ppCalcTask.setEnabled(false);

        scrollPane.getViewport().add(taskTable, null);
        this.add(scrollPane, BorderLayout.CENTER);
        tasksToolBar.add(historyBackB, null);
        tasksToolBar.add(historyForwardB, null);
        tasksToolBar.addSeparator(new Dimension(8, 24));

        tasksToolBar.add(newTaskB, null);
        // tasksToolBar.add(subTaskB, null); Ovadia Shalom US32
        tasksToolBar.add(removeTaskB, null);
        tasksToolBar.addSeparator(new Dimension(8, 24));
        tasksToolBar.add(editTaskB, null);
        tasksToolBar.add(completeTaskB, null);

        // Add separation between the Sprint section and Transition section for
        // buttons.
        tasksToolBar.addSeparator(new Dimension(100, 24));

        // US43 - New transition button on Sprint page, add to toolbar.
        tasksToolBar.add(newTransB, null);
        tasksToolBar.add(removeTransB, null);
        tasksToolBar.addSeparator(new Dimension(8, 24));
        tasksToolBar.add(editTransB, null);
        tasksToolBar.add(completeTransB, null);

        // tasksToolBar.add(showActiveOnly, null);

        this.add(tasksToolBar, BorderLayout.NORTH);

        // PopupListener ppListener = new PopupListener(); Ovadia Shalom US32
        // scrollPane.addMouseListener(ppListener);
        // taskTable.addMouseListener(ppListener);

        CurrentDate.addDateListener(new IDateListener() {
            public void dateChange(CalendarDate d) {
                newTaskB.setEnabled(
                        d.inPeriod(CurrentProject.get().getStartDate(),
                                CurrentProject.get().getEndDate()));
            }
        });
        CurrentProject.addProjectListener(new IProjectListener() {
            public void projectChange(IProject p, INoteList nl, ITaskList tl,
                    IResourcesList rl, ICommitList cl, IPullRequestList prl) {
                newTaskB.setEnabled(CurrentDate.get().inPeriod(p.getStartDate(),
                        p.getEndDate()));
            }

            public void projectWasChanged() {
                // taskTable.setCurrentRootTask(null); //XXX
            }
        });
        taskTable.getSelectionModel()
                .addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        boolean enbl = (taskTable.getRowCount() > 0)
                                && (taskTable.getSelectedRow() > -1);
                        editTaskB.setEnabled(enbl);
                        ppEditTask.setEnabled(enbl);
                        removeTaskB.setEnabled(enbl);
                        ppRemoveTask.setEnabled(enbl);

                        ppCompleteTask.setEnabled(enbl);
                        completeTaskB.setEnabled(enbl);
                        // ppAddSubTask.setEnabled(enbl); Ovadia Shalom US32
                        // ppSubTasks.setEnabled(enbl); // default value to be
                        // over-written later depending on whether it has sub
                        // tasks
                        ppCalcTask.setEnabled(enbl); // default value to be
                                                     // over-written later
                                                     // depending on whether it
                                                     // has sub tasks

                        /*
                         * if (taskTable.getCurrentRootTask() == null) {
                         * ppParentTask.setEnabled(false); } else {
                         * ppParentTask.setEnabled(true); }XXX
                         */

                        if (enbl) {
                            String thisTaskId = taskTable.getModel()
                                    .getValueAt(taskTable.getSelectedRow(),
                                            TaskTable.TASK_ID)
                                    .toString();

                            boolean hasSubTasks = CurrentProject.getTaskList()
                                    .hasSubTasks(thisTaskId);
                            // ppSubTasks.setEnabled(hasSubTasks);
                            ppCalcTask.setEnabled(hasSubTasks);
                            ITask t = CurrentProject.getTaskList()
                                    .getTask(thisTaskId);
                            parentPanel.calendar.jnCalendar.renderer.setTask(t);
                            parentPanel.calendar.jnCalendar.updateUI();
                        } else {
                            parentPanel.calendar.jnCalendar.renderer
                                    .setTask(null);
                            parentPanel.calendar.jnCalendar.updateUI();
                        }
                    }
                });
        editTaskB.setEnabled(false);
        removeTaskB.setEnabled(false);
        completeTaskB.setEnabled(false);
        // ppAddSubTask.setEnabled(false); Ovadia Shalom US32
        // ppSubTasks.setEnabled(false);
        // ppParentTask.setEnabled(false);
        taskPPMenu.add(ppEditTask);

        taskPPMenu.addSeparator();
        taskPPMenu.add(ppNewTask);
        // taskPPMenu.add(ppAddSubTask); Ovadia Shalom US32
        taskPPMenu.add(ppRemoveTask);

        taskPPMenu.addSeparator();
        taskPPMenu.add(ppCompleteTask);
        taskPPMenu.add(ppCalcTask);

        // taskPPMenu.addSeparator();

        // taskPPMenu.add(ppSubTasks);

        // taskPPMenu.addSeparator();
        // taskPPMenu.add(ppParentTask);

        taskPPMenu.addSeparator();
        taskPPMenu.add(ppShowActiveOnlyChB);

        // define key actions in TaskPanel:
        // - KEY:DELETE => delete tasks (recursivly).
        // - KEY:INTERT => insert new Subtask if another is selected.
        // - KEY:INSERT => insert new Task if nothing is selected.
        // - KEY:SPACE => finish Task.
        taskTable.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (taskTable.getSelectedRows().length > 0
                        && e.getKeyCode() == KeyEvent.VK_DELETE)
                    ppRemoveTask_actionPerformed(null);

                else if (e.getKeyCode() == KeyEvent.VK_INSERT) {
                    if (taskTable.getSelectedRows().length > 0) {
                        ppAddSubTask_actionPerformed(null);
                    } else {
                        ppNewTask_actionPerformed(null);
                    }
                }

                else if (e.getKeyCode() == KeyEvent.VK_SPACE
                        && taskTable.getSelectedRows().length > 0) {
                    ppCompleteTask_actionPerformed(null);
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });

    }

    void editTaskB_actionPerformed(ActionEvent e) {
        ITask t = CurrentProject.getTaskList().getTask(taskTable.getModel()
                .getValueAt(taskTable.getSelectedRow(), TaskTable.TASK_ID)
                .toString());
        TaskDialog dlg = new TaskDialog(App.getFrame(),
                Local.getString("Edit sprint"));
        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();
        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x,
                (frmSize.height - dlg.getSize().height) / 2 + loc.y);
        dlg.todoField.setText(t.getText());
        dlg.descriptionField.setText(t.getDescription());
        dlg.startDate.getModel().setValue(t.getStartDate().getDate());
        dlg.endDate.getModel().setValue(t.getEndDate().getDate());
        // Ovadia ShalomUS32
        // dlg.priorityCB.setSelectedIndex(t.getPriority());
        // dlg.effortField.setText(Util.getHoursFromMillis(t.getEffort()));

        /*
         * Original code before US33.
         * if((t.getStartDate().getDate()).after(t.getEndDate().getDate()))
         * dlg.chkEndDate.setSelected(false); else
         * dlg.chkEndDate.setSelected(true);
         */ // End of original code before US33.

        // Ovadia Shalom US32
        // dlg.progress.setValue(new Integer(t.getProgress()));
        dlg.chkEndDate_actionPerformed(null);
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
        CalendarDate sd = new CalendarDate(
                (Date) dlg.startDate.getModel().getValue());
        // CalendarDate ed = new CalendarDate((Date)
        // dlg.endDate.getModel().getValue());
        CalendarDate ed;

        /*
         * Original code before US33 commented out.
         * if(dlg.chkEndDate.isSelected()) ed = new CalendarDate((Date)
         * dlg.endDate.getModel().getValue()); else ed = null;
         */ // Original code before US33.
        ed = new CalendarDate((Date) dlg.endDate.getModel().getValue()); // New
                                                                         // code
                                                                         // for
                                                                         // US33.

        t.setStartDate(sd);
        t.setEndDate(ed);
        t.setText(dlg.todoField.getText());
        t.setDescription(dlg.descriptionField.getText());
        // Ovadia Shalom US32
        // t.setPriority(dlg.priorityCB.getSelectedIndex());
        // t.setEffort(Util.getMillisFromHours(dlg.effortField.getText()));
        // t.setProgress(((Integer)dlg.progress.getValue()).intValue());

        // CurrentProject.getTaskList().adjustParentTasks(t);

        CurrentStorage.get().storeTaskList(CurrentProject.getTaskList(),
                CurrentProject.get());
        taskTable.tableChanged();
        parentPanel.updateIndicators();
        //taskTable.updateUI();
        t.setGitMaster(dlg.gitMaster);
    }

    void newTaskB_actionPerformed(ActionEvent e) {
        TaskDialog dlg = new TaskDialog(App.getFrame(),
                Local.getString("New sprint"));

        // XXX String parentTaskId = taskTable.getCurrentRootTask();

        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();
        dlg.startDate.getModel().setValue(CurrentDate.get().getDate());
        dlg.endDate.getModel().setValue(CurrentDate.get().getDate());
        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x,
                (frmSize.height - dlg.getSize().height) / 2 + loc.y);
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
        CalendarDate sd = new CalendarDate(
                (Date) dlg.startDate.getModel().getValue());
        // CalendarDate ed = new CalendarDate((Date)
        // dlg.endDate.getModel().getValue());
        CalendarDate ed;

        /*
         * Original code before US33 commented out.
         * if(dlg.chkEndDate.isSelected()) ed = new CalendarDate((Date)
         * dlg.endDate.getModel().getValue()); else ed = null;
         */ // End of original code before US33.
        ed = new CalendarDate((Date) dlg.endDate.getModel().getValue()); // US33

        // long effort = Util.getMillisFromHours(dlg.effortField.getText());
        // XXX Task newTask = CurrentProject.getTaskList().createTask(sd, ed,
        // dlg.todoField.getText(), dlg.priorityCB.getSelectedIndex(),effort,
        // dlg.descriptionField.getText(),parentTaskId);
        // Task createTask(CalendarDate startDate, CalendarDate endDate, String
        // text, String description, String parentTaskId); Ovadia Shalom US32
        // CurrentProject.getTaskList().adjustParentTasks(newTask);
        // newTask.setProgress(new Integer(3)); ovadia
        
        
        // Automatically add a transition if the there is time between the
        // previous sprint and this one.
        // Find the task right before this one
        Collection allProjectTasks = CurrentProject.getTaskList().getTopLevelTasks();
        CalendarDate lastE = null;
        for (Iterator iter = allProjectTasks.iterator(); iter.hasNext();) {
            ITask t = (ITask) iter.next();
            if (t.getEndDate().before(sd)) {
                if (lastE == null || t.getEndDate().after(lastE) ) {
                    lastE = t.getEndDate();
                }
            }
        }
        if (lastE != null) {
            CalendarDate transitionStart = CalendarDate.addDays(lastE, 1);
            CalendarDate transitionEnd = CalendarDate.addDays(sd, -1);
            CurrentProject.getTaskList().createTask(transitionStart, transitionEnd,
                    "Transition", "Transition", null);
        }
        
        // Now handle if we're adding this sprint before another sprint
        CalendarDate lastS = null;
        for (Iterator iter = allProjectTasks.iterator(); iter.hasNext();) {
            ITask t = (ITask) iter.next();
            if (t.getStartDate().after(ed)) {
                if (lastS == null || t.getStartDate().before(lastS) ) {
                    lastS = t.getStartDate();
                }
            }
        }
        if (lastS != null) {
            CalendarDate transitionStart = CalendarDate.addDays(ed, 1);
            CalendarDate transitionEnd = CalendarDate.addDays(lastS, -1);
            CurrentProject.getTaskList().createTask(transitionStart, transitionEnd,
                    "Transition", "Transition", null);
        }
        
        
        ITask newTask = CurrentProject.getTaskList().createTask(sd, ed,
                dlg.todoField.getText(), dlg.descriptionField.getText(), null, dlg.gitMaster);
        
        CurrentStorage.get().storeTaskList(CurrentProject.getTaskList(),
                CurrentProject.get());


        taskTable.tableChanged();
        parentPanel.updateIndicators();
        // taskTable.updateUI();
    }

    void addSubTask_actionPerformed(ActionEvent e) {
        TaskDialog dlg = new TaskDialog(App.getFrame(),
                Local.getString("New Task"));
        String parentTaskId = taskTable.getModel()
                .getValueAt(taskTable.getSelectedRow(), TaskTable.TASK_ID)
                .toString();

        // Util.debug("Adding sub task under " + parentTaskId);

        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();
        ITask parent = CurrentProject.getTaskList().getTask(parentTaskId);
        CalendarDate todayD = CurrentDate.get();
        if (todayD.after(parent.getStartDate()))
            dlg.setStartDate(todayD);
        else
            dlg.setStartDate(parent.getStartDate());
        if (parent.getEndDate() != null)
            dlg.setEndDate(parent.getEndDate());
        else
            dlg.setEndDate(CurrentProject.get().getEndDate());
        dlg.setStartDateLimit(parent.getStartDate(), parent.getEndDate());
        dlg.setEndDateLimit(parent.getStartDate(), parent.getEndDate());
        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x,
                (frmSize.height - dlg.getSize().height) / 2 + loc.y);
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
<<<<<<< HEAD
        CalendarDate sd = new CalendarDate((Date) dlg.startDate.getModel().getValue());
//        CalendarDate ed = new CalendarDate((Date) dlg.endDate.getModel().getValue());
          CalendarDate ed;
          
          /* Original code before US33 commented out.  
 		if(dlg.chkEndDate.isSelected())
 			ed = new CalendarDate((Date) dlg.endDate.getModel().getValue());
 		else
 			ed = null;
 		  */  // End of original code before US33. 
          ed = new CalendarDate((Date) dlg.endDate.getModel().getValue());;   // Addition for US33. 
     
        //long effort = Util.getMillisFromHours(dlg.effortField.getText());
		ITask newTask = CurrentProject.getTaskList().createTask(sd, ed, dlg.todoField.getText(), dlg.descriptionField.getText(),parentTaskId, dlg.gitMaster);
//        newTask.setProgress(new Integer(3)); ovadia
//		CurrentProject.getTaskList().adjustParentTasks(newTask);

		CurrentStorage.get().storeTaskList(CurrentProject.getTaskList(), CurrentProject.get());
=======
        CalendarDate sd = new CalendarDate(
                (Date) dlg.startDate.getModel().getValue());
        // CalendarDate ed = new CalendarDate((Date)
        // dlg.endDate.getModel().getValue());
        CalendarDate ed;

        /*
         * Original code before US33 commented out.
         * if(dlg.chkEndDate.isSelected()) ed = new CalendarDate((Date)
         * dlg.endDate.getModel().getValue()); else ed = null;
         */ // End of original code before US33.
        ed = new CalendarDate((Date) dlg.endDate.getModel().getValue());
        ; // Addition for US33.

        // long effort = Util.getMillisFromHours(dlg.effortField.getText());
        ITask newTask = CurrentProject.getTaskList().createTask(sd, ed,
                dlg.todoField.getText(), dlg.descriptionField.getText(),
                parentTaskId, dlg.gitMaster);
        // newTask.setProgress(new Integer(3)); ovadia
        // CurrentProject.getTaskList().adjustParentTasks(newTask);

        CurrentStorage.get().storeTaskList(CurrentProject.getTaskList(),
                CurrentProject.get());
>>>>>>> f15ce9312c3fea764ca13c69698f4fde5e22ea9e
        taskTable.tableChanged();
        parentPanel.updateIndicators();
        // taskTable.updateUI();
    }

    void calcTask_actionPerformed(ActionEvent e) {
        TaskCalcDialog dlg = new TaskCalcDialog(App.getFrame());
        dlg.pack();
        ITask t = CurrentProject.getTaskList().getTask(taskTable.getModel()
                .getValueAt(taskTable.getSelectedRow(), TaskTable.TASK_ID)
                .toString());

        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();

        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x,
                (frmSize.height - dlg.getSize().height) / 2 + loc.y);
        dlg.setVisible(true);
        if (dlg.CANCELLED) {
            return;
        }

        ITaskList tl = CurrentProject.getTaskList();
        if (dlg.calcEffortChB.isSelected()) {
            t.setEffort(tl.calculateTotalEffortFromSubTasks(t));
        }

        if (dlg.compactDatesChB.isSelected()) {
            t.setStartDate(tl.getEarliestStartDateFromSubTasks(t));
            t.setEndDate(tl.getLatestEndDateFromSubTasks(t));
        }

        if (dlg.calcCompletionChB.isSelected()) {
            long[] res = tl.calculateCompletionFromSubTasks(t);
            int thisProgress = (int) Math
                    .round((((double) res[0] / (double) res[1]) * 100));
            t.setProgress(thisProgress);
        }

        // CalendarDate sd = new CalendarDate((Date)
        // dlg.startDate.getModel().getValue());
        //// CalendarDate ed = new CalendarDate((Date)
        // dlg.endDate.getModel().getValue());
        // CalendarDate ed;
        // if(dlg.chkEndDate.isSelected())
        // ed = new CalendarDate((Date) dlg.endDate.getModel().getValue());
        // else
        // ed = new CalendarDate(0,0,0);
        // long effort = Util.getMillisFromHours(dlg.effortField.getText());
        // Task newTask = CurrentProject.getTaskList().createTask(sd, ed,
        // dlg.todoField.getText(), dlg.priorityCB.getSelectedIndex(),effort,
        // dlg.descriptionField.getText(),parentTaskId);
        //

        CurrentStorage.get().storeTaskList(CurrentProject.getTaskList(),
                CurrentProject.get());
        taskTable.tableChanged();
        // parentPanel.updateIndicators();
        // taskTable.updateUI();
    }

    void listSubTasks_actionPerformed(ActionEvent e) {
        String parentTaskId = taskTable.getModel()
                .getValueAt(taskTable.getSelectedRow(), TaskTable.TASK_ID)
                .toString();

        // XXX taskTable.setCurrentRootTask(parentTaskId);
        taskTable.tableChanged();

        // parentPanel.updateIndicators();
        // //taskTable.updateUI();
    }

    void parentTask_actionPerformed(ActionEvent e) {
        // String taskId =
        // taskTable.getModel().getValueAt(taskTable.getSelectedRow(),
        // TaskTable.TASK_ID).toString();
        //
        // Task t = CurrentProject.getTaskList().getTask(taskId);
        /*
         * XXX Task t2 =
         * CurrentProject.getTaskList().getTask(taskTable.getCurrentRootTask());
         * 
         * String parentTaskId = t2.getParent(); if((parentTaskId == null) ||
         * (parentTaskId.equals(""))) { parentTaskId = null; }
         * taskTable.setCurrentRootTask(parentTaskId); taskTable.tableChanged();
         */

        // parentPanel.updateIndicators();
        // //taskTable.updateUI();
    }

    void removeTaskB_actionPerformed(ActionEvent e) {
        String msg;
        String thisTaskId = taskTable.getModel()
                .getValueAt(taskTable.getSelectedRow(), TaskTable.TASK_ID)
                .toString();

        if (taskTable.getSelectedRows().length > 1)
            msg = Local.getString("Remove") + " "
                    + taskTable.getSelectedRows().length + " "
                    + Local.getString("tasks") + "?" + "\n"
                    + Local.getString("Are you sure?");
        else {
            ITask t = CurrentProject.getTaskList().getTask(thisTaskId);
            // check if there are subtasks
            if (CurrentProject.getTaskList().hasSubTasks(thisTaskId)) {
                msg = Local.getString("Remove sprint") + "\n'" + t.getText()
                        + Local.getString("' and all subtasks") + "\n"
                        + Local.getString("Are you sure?");
            } else {
                msg = Local.getString("Remove sprint") + "\n'" + t.getText()
                        + "'\n" + Local.getString("Are you sure?");
            }
        }
        int n = JOptionPane.showConfirmDialog(App.getFrame(), msg,
                Local.getString("Remove sprint"), JOptionPane.YES_NO_OPTION);
        if (n != JOptionPane.YES_OPTION)
            return;
        Vector toremove = new Vector();
        for (int i = 0; i < taskTable.getSelectedRows().length; i++) {
            ITask t = CurrentProject.getTaskList()
                    .getTask(taskTable.getModel()
                            .getValueAt(taskTable.getSelectedRows()[i],
                                    TaskTable.TASK_ID)
                            .toString());
            if (t != null)
                toremove.add(t);
        }
        for (int i = 0; i < toremove.size(); i++) {
            CurrentProject.getTaskList().removeTask((ITask) toremove.get(i));
        }
        taskTable.tableChanged();
        CurrentStorage.get().storeTaskList(CurrentProject.getTaskList(),
                CurrentProject.get());
        parentPanel.updateIndicators();
        // taskTable.updateUI();

    }

    void ppCompleteTask_actionPerformed(ActionEvent e) {
        String msg;
        Vector tocomplete = new Vector();
        for (int i = 0; i < taskTable.getSelectedRows().length; i++) {
            ITask t = CurrentProject.getTaskList()
                    .getTask(taskTable.getModel()
                            .getValueAt(taskTable.getSelectedRows()[i],
                                    TaskTable.TASK_ID)
                            .toString());
            if (t != null)
                tocomplete.add(t);
        }
        for (int i = 0; i < tocomplete.size(); i++) {
            ITask t = (ITask) tocomplete.get(i);
            t.setProgress(100);
        }
        taskTable.tableChanged();
        CurrentStorage.get().storeTaskList(CurrentProject.getTaskList(),
                CurrentProject.get());
        parentPanel.updateIndicators();
        // taskTable.updateUI();
    }

    // toggle "show active only"
    void toggleShowActiveOnly_actionPerformed(ActionEvent e) {
        Context.put("SHOW_ACTIVE_TASKS_ONLY",
                new Boolean(ppShowActiveOnlyChB.isSelected()));
        taskTable.tableChanged();
    }

    class PopupListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if ((e.getClickCount() == 2) && (taskTable.getSelectedRow() > -1)) {
                // ignore "tree" column
                // if(taskTable.getSelectedColumn() == 1) return;

                editTaskB_actionPerformed(null);
            }
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                taskPPMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

    }

    void ppEditTask_actionPerformed(ActionEvent e) {
        editTaskB_actionPerformed(e);
    }

    void ppRemoveTask_actionPerformed(ActionEvent e) {
        removeTaskB_actionPerformed(e);
    }

    void ppNewTask_actionPerformed(ActionEvent e) {
        newTaskB_actionPerformed(e);
    }

    void ppAddSubTask_actionPerformed(ActionEvent e) {
        addSubTask_actionPerformed(e);
    }

    void ppListSubTasks_actionPerformed(ActionEvent e) {
        listSubTasks_actionPerformed(e);
    }

    void ppParentTask_actionPerformed(ActionEvent e) {
        parentTask_actionPerformed(e);
    }

    void ppCalcTask_actionPerformed(ActionEvent e) {
        calcTask_actionPerformed(e);
    }

}