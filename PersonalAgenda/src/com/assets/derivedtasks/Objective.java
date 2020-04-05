package com.assets.derivedtasks;

import com.assets.Task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Objective extends Planned {
    private float progress;
    private List<Task> subtasks;
    private int completedSubtasks = 0;

    public Objective() {
        super();
        this.progress = 0;
        this.subtasks = new ArrayList<Task>();
    };

    public Objective(String title, Date date){
        super(title, false, date);
        this.progress = 0;
        this.subtasks = new ArrayList<Task>();
    }

    private void updateProgress(){
        this.progress = (float)completedSubtasks/subtasks.size() *100;
        this.progress = (float)Math.floor(this.progress*100)/100;
        if(this.progress >= 100){
            this.setStatus(true);
        }
        else{
            this.setStatus(false);
        }
    }

    public void printSubtasks(){
        if(subtasks.size() != 1){
            for(Task t : subtasks) {
                t.Print();
            }
        }
        else{
            System.out.println("No subtasks");
        }
    }

    public void addSubtask(String title, boolean status){
        subtasks.add(new Task(title, status));
        updateProgress();
    }

    public void updateSubtask(int idx){
        if(idx < subtasks.size() && idx >=0){
            if(subtasks.get(idx).toggleStatus()){
                completedSubtasks++;
            }
            else{
                completedSubtasks--;
            }
        }
        else{
            System.out.println("Index invalid");
            return;
        }
        updateProgress();
    }

    public void removeSubtask(int idx){
        if(idx < subtasks.size() && idx >=0){
            if(subtasks.get(idx).getStatus()){
                completedSubtasks--;
            }
            subtasks.remove(idx);
        }
        else{
            System.out.println("Index invalid");
            return;
        }
        updateProgress();
    }

    @Override
    public void Print(){
        super.Print();
        System.out.println("|-Progress " + this.progress + "%");
    }
}
