package com.assets;

import com.assets.derivedtasks.General;
import com.assets.sorting.ImpTasksComparatorAsc;
import com.assets.sorting.ImpTasksComparatorDesc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoList {
    private int index;
    private String listName;
    private List<General> taskList;

    public TodoList(){
        this.listName = "Untitled List";
        this.taskList = new ArrayList<General>();
    }
    public TodoList(String listName){
        this.listName = listName;
        this.taskList = new ArrayList<General>();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void printTasks(){
        if(this.taskList.size() > 0){
            for(Task t : taskList){
                System.out.print("|*");
                t.Print();
            }
        }
        else{
            System.out.println("List empty");
        }
    }

    public void addTasks(String title, boolean status, int importancy, int index){
        General obj = new General(title, status, importancy);
        obj.setIndex(index);

        taskList.add(obj);
    }

    public void updateTasks(int idx){
        if(idx < taskList.size() && idx >=0){
            taskList.get(idx).toggleStatus();
        }
        else{
            System.out.println("Index invalid");
        }
    }

    public void removeTasks(int idx){
        if(idx < taskList.size() && idx >=0){
            taskList.remove(idx);
        }
        else{
            System.out.println("Index invalid");
        }
    }

    public void sortByImportancy(String order){
        switch (order){
            case "ASC":
                ImpTasksComparatorAsc impTasksComparatorAsc = new ImpTasksComparatorAsc();
                Collections.sort(taskList, impTasksComparatorAsc);
                break;
            case "DESC":
                ImpTasksComparatorDesc impTasksComparatorDesc = new ImpTasksComparatorDesc();
                Collections.sort(taskList, impTasksComparatorDesc);
                break;
        }
    }

    public void printUncompletedTasks(){
        for(General t : taskList){
            if(!t.getStatus()){
                t.Print();
            }
        }
    }
    public void printCompletedTasks(){
        for(General t : taskList){
            if(t.getStatus()){
                t.Print();
            }
        }
    }

    public void Print(){
        System.out.println(this.listName);
        this.printTasks();
    }

    public Task getTask(int idx){
        return taskList.get(idx);
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListName() {
        return listName;
    }
}
