package com;

import com.assets.derivedtasks.General;
import com.assets.services.ServiceGeneral;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class GUI implements ActionListener {
    int count = 0;

    private JFrame frame;
    private JPanel panel;

    public GUI(){
        //Frame settings
        frame = new JFrame();
        frame.add(mainScreen(), BorderLayout.CENTER);
        frame.getContentPane().setBackground( Color.decode("#f1f1f1") );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Personal Agenda");
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel mainScreen(){
        JLabel title = new JLabel("PERSONAL AGENDA", SwingConstants.CENTER);
        title.setForeground(Color.decode("#333333"));
        Font font = new Font("SansSerif", Font.BOLD, 30);
        title.setFont(font);

        JButton notes = new JButton("Notes");
        notes.addActionListener(this);

        JButton plannifier = new JButton("Planifier");
        plannifier.addActionListener(this);

        JButton objectives = new JButton("Your objectives");
        objectives.addActionListener(this);

        JButton lists = new JButton("Your lists");
        lists.addActionListener(this);

        //Panel settings
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout("wrap 4"));
        mainPanel.add(title, "span 4");
        mainPanel.add(notes);
        mainPanel.add(plannifier);
        mainPanel.add(objectives);
        mainPanel.add(lists);

        this.panel = mainPanel;
        return mainPanel;
    }

    private JPanel notesWindow(){
        JLabel title = new JLabel("Notes", SwingConstants.CENTER);
        title.setForeground(Color.decode("#333333"));
        Font font = new Font("SansSerif", Font.BOLD, 30);
        title.setFont(font);

        JTextField textArea = new JTextField("Type note",50);

        JComboBox importancy = new JComboBox(new String[]{"Low", "Medium", "High"});
        importancy.setSelectedIndex(0);

        JButton addTask = new JButton("Add note");
        addTask.addActionListener(ae -> {
            String textFieldValue = textArea.getText();
            int imp = importancy.getSelectedIndex();
            General obj = new General(textFieldValue, false, imp);
            ServiceGeneral.add_task(obj);

            this.frame.remove(this.panel);
            this.frame.repaint();
            this.frame.add(notesWindow());
            this.frame.revalidate();
        });


        JButton back = new JButton("Back");
        back.addActionListener(this);

        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new MigLayout("wrap 4"));
        notesPanel.add(title, "span 4");
        notesPanel.add(back, "span 1 2");
        notesPanel.add(textArea, "span 2 2, center");
        notesPanel.add(addTask, "span 1, right");
        notesPanel.add(importancy, "span 1, right");

        ArrayList<General> arr = ServiceGeneral.get_all_tasks();
        for (General task : arr){
            JButton note = new JButton(task.getTitle());
            note.putClientProperty("id", task.getIndex());

            note.addActionListener(this);
            note.setActionCommand("note");
            notesPanel.add(note, "span 4, center");
        }

        this.panel = notesPanel;
        return notesPanel;
    }

    private JPanel selectedNote(int index){
        General task = ServiceGeneral.get_single(index);

        JLabel title = new JLabel(task.getTitle(), SwingConstants.LEFT);
        title.setForeground(Color.decode("#333333"));
        Font font = new Font("SansSerif", Font.BOLD, 30);
        title.setFont(font);

        JTextField textArea = new JTextField("Type note",50);

        JComboBox status = new JComboBox(new String[]{"Uncompleted", "Completed"});
        if(task.getStatus()){
            status.setSelectedIndex(1);
        }
        else{
            status.setSelectedIndex(0);
        }

        JComboBox importancy = new JComboBox(new String[]{"Low", "Medium", "High"});
        if(task.getImportancy() == 0){
            importancy.setSelectedIndex(0);
        }
        else if(task.getImportancy() == 1){
            importancy.setSelectedIndex(1);
        }
        else{
            importancy.setSelectedIndex(2);
        }

        JButton update = new JButton("Update note");
        update.addActionListener(ae -> {
            ServiceGeneral.update_task_title(index, textArea.getText());
            ServiceGeneral.update_task_status(index, status.getSelectedIndex() == 1);
            ServiceGeneral.update_task_importancy(index, importancy.getSelectedIndex());

            this.frame.remove(this.panel);
            this.frame.repaint();
            this.frame.add(selectedNote(index));
            this.frame.revalidate();
        });

        JButton delete = new JButton("Delete note");
        delete.addActionListener(ae -> {
            ServiceGeneral.delete_task(index);
            this.frame.remove(this.panel);
            this.frame.repaint();
            this.frame.add(notesWindow());
            this.frame.revalidate();
        });

        JButton back = new JButton("Back to notes");
        back.addActionListener(ae -> {
            this.frame.remove(this.panel);
            this.frame.repaint();
            this.frame.add(notesWindow());
            this.frame.revalidate();
        });


        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new MigLayout("wrap 3"));
        notesPanel.add(title, "span 3");
        notesPanel.add(textArea, "span 2 2");
        notesPanel.add(status, "span 1");
        notesPanel.add(importancy, "span 1");
        notesPanel.add(delete, "span 1");
        notesPanel.add(back, "span 1");
        notesPanel.add(update, "span 1");

        this.panel = notesPanel;
        return notesPanel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()){
            case "Back":
                this.frame.remove(this.panel);
                this.frame.repaint();
                this.frame.add(mainScreen());
                this.frame.revalidate();
                break;
            case "Back to notes":
            case "Notes":
                this.frame.remove(this.panel);
                this.frame.repaint();
                this.frame.add(notesWindow());
                this.frame.revalidate();
                break;
            case "note":
                this.frame.remove(this.panel);
                this.frame.repaint();
                this.frame.add(selectedNote((Integer)((JButton)actionEvent.getSource()).getClientProperty("id")));
                this.frame.revalidate();
                break;
            case "Planifier":
                break;
            case "Your lists":
                break;
            case "Your objectives":
                break;
            default:
                break;
        }
    }
}
