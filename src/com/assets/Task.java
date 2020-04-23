package com.assets;

public class Task {
    private String title;
    private boolean status;

    public Task() {
        this.title = "Untitled Task";
        this.status = false;
    };

    public Task(String title, boolean status){
        this.title = title;
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean getStatus(){
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean toggleStatus(){
        this.status = !this.status;
        return this.status;
    }

    public void Print(){
        System.out.println(this.title);
        System.out.println("|-Status " + ((this.status) ? "Completed" : "Uncompleted"));
    }
}
