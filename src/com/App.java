package com;

import com.assets.Task;
import com.assets.TodoList;
import com.assets.derivedtasks.General;
import com.assets.derivedtasks.Objective;
import com.assets.derivedtasks.Planned;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {

    public App(){
    }

    public void selectTask(Task t){
        Scanner keyboard = new Scanner(System.in);
        t.Print();
        while (true){
            System.out.println("COMMANDS");
            if(t instanceof General){
                System.out.println("CS - Change status");
                System.out.println("CI - Change importancy");
                System.out.println("RN - Rename task");
            }
            else if(t instanceof Planned){
                if(t instanceof Objective){
                    System.out.println("CD - Change date");
                    System.out.println("RN - Rename task");
                    System.out.println("PS - Print subtasks");
                    System.out.println("DS - Delete subtask");
                    System.out.println("AS - Add subtask");
                    System.out.println("US - Toggle subtask status");
                }
                else{
                    System.out.println("US - Change status");
                    System.out.println("CD - Change date (dd-MM-yyyy)");
                    System.out.println("RN - Rename task");
                }
            }
            else{
                System.out.println("Invalid selection");
                System.exit(0);
            }
            System.out.println("BK - Back");
            System.out.print("Enter command: ");
            String choice = keyboard.nextLine();
            if(choice.toUpperCase().equals("BK")){
                break;
            }
            else{
                try {
                    switch (choice.toUpperCase()){
                        case "CS":
                            assert t instanceof General;
                            ((General) t).toggleStatus();
                            //TODO update general task
                            break;
                        case "CI":
                            System.out.print("Importancy: ");
                            int imp = keyboard.nextInt();
                            keyboard.nextLine();
                            assert t instanceof General;
                            ((General) t).updateImportancy(imp);
                            //TODO update general task
                            break;
                        case "RN":
                            System.out.print("New title: ");
                            String title = keyboard.nextLine();
                            t.setTitle(title);
                            //TODO update any task's title
                            break;
                        case "CD":
                            System.out.print("New date: ");
                            String dates = keyboard.nextLine();
                            Date dt = new Date();
                            try{
                                dt = new SimpleDateFormat("dd-MM-yyyy").parse(dates);
                            }
                            catch (ParseException e){
                                System.out.println("Date could not be parsed");
                                System.exit(0);
                            }
                            assert t instanceof Planned;
                            ((Planned) t).setHappeningDate(dt);
                            //TODO update planned or objective
                            break;
                        case "AS":
                            System.out.print("Subtask title: ");
                            title = keyboard.nextLine();
                            assert t instanceof Objective;
                            //TODO update objective
                            break;
                        case "PS":
                            assert t instanceof Objective;
                            //TODO print objective subtasks
                            break;
                        case "US":
                            System.out.print("Subtask index: ");
                            int idx = keyboard.nextInt();
                            keyboard.nextLine();
                            assert t instanceof Objective;
                            ((Objective) t).updateSubtask(idx);
                            //TODO update objective subtask
                            break;
                        case "DS":
                            System.out.print("Subtask index: ");
                            idx = keyboard.nextInt();
                            keyboard.nextLine();
                            assert t instanceof Objective;
                            ((Objective) t).removeSubtask(idx);
                            //TODO delete objective subtask
                            break;
                        default:
                            System.out.println("Invalid command");
                            break;
                    }
                }
                catch (ClassCastException e){
                    System.out.println("Class could not be casted");
                    System.exit(0);
                }
            }
        }
    }

    public void selectList(TodoList l){
        Scanner key = new Scanner(System.in);
        l.Print();
        while(true){
            System.out.println("COMMANDS: ");
            System.out.println("AN - Add note to list");
            System.out.println("DN - Delete note from list");
            System.out.println("RN - Rename list");
            System.out.println("SN - Select note");
            System.out.println("GC - Get completed notes");
            System.out.println("GU - Get uncompleted notes");
            System.out.println("SI - Sort by importancy");
            System.out.println("BK - Back");
            System.out.print("Enter command: ");
            String choice = key.nextLine();
            if(choice.toUpperCase().equals("BK")){
                break;
            }
            switch (choice.toUpperCase()){
                case "AN":
                    System.out.print("Note title: ");
                    String title = key.nextLine();
                    System.out.print("Importancy: ");
                    int imp = key.nextInt();
                    key.nextLine();
                    System.out.println("NOTES");
                    l.printTasks();
                    break;
                case "DN":
                    System.out.print("Note index: ");
                    int idx = key.nextInt();
                    key.nextLine();
                    l.removeTasks(idx);
                    System.out.println("NOTES");
                    l.printTasks();
                    break;
                case "RN":
                    System.out.print("Note title: ");
                    title = key.nextLine();
                    l.setListName(title);
                    break;
                case "SN":
                    System.out.print("Note index: ");
                    idx = key.nextInt();
                    key.nextLine();
                    selectTask(l.getTask(idx));
                    break;
                case "GC":
                    System.out.println("COMPLETED NOTES");
                    l.printCompletedTasks();
                    break;
                case "GU":
                    System.out.println("UNCOMPLETED NOTES");
                    l.printUncompletedTasks();
                    break;
                case "SI":
                    System.out.println("ASC - Ascending order");
                    System.out.println("DESC - Descending order");
                    System.out.print("Enter command: ");
                    key.nextLine();
                    String c = key.nextLine();
                    c = c.toUpperCase();
                    if(c.equals("ASC") || c.equals("DESC")){
                        l.sortByImportancy(c);
                    }
                    else{
                        System.out.println("Invalid format");
                    }
                    System.out.println("NOTES");
                    l.printTasks();
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }

    public void printAllTasks(){
        //TODO print all tasks
    }

    public void printAllLists(){
        //TODO print all lists
    }

    public void addTask(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("AT - Add General Task");
        System.out.println("AP - Add Planned Task");
        System.out.println("AO - Add Objective");
        System.out.print("Enter command: ");
        String cmd = keyboard.nextLine();
        switch (cmd.toUpperCase()){
            case "AT":
                System.out.print("Title: ");
                String title = keyboard.nextLine();
                General t = new General(title, false, 0);
                System.out.print("Importancy(0-2): ");
                int imp = keyboard.nextInt();
                keyboard.nextLine();
                t.updateImportancy(imp);
                //TODO add general task
                break;

            case "AP":
                System.out.print("Title: ");
                title = keyboard.nextLine();
                System.out.print("Date (dd-MM-yyyy): ");
                String ds = keyboard.nextLine();
                Date dt = new Date();
                try{
                    dt = new SimpleDateFormat("dd-MM-yyyy").parse(ds);
                }
                catch (ParseException e){
                    System.out.println("Date could not be parsed");
                    System.exit(0);
                }
                Planned p = new Planned(title, false, dt);
                if(p.getCountdown() < 0){
                    System.out.println("Invalid date");
                }
                else{
                    //TODO add planned task
                }
                break;

            case "AO":
                System.out.print("Title: ");
                title = keyboard.nextLine();
                System.out.print("Date (dd-MM-yyyy): ");
                ds = keyboard.nextLine();
                dt = new Date();
                try{
                    dt = new SimpleDateFormat("dd-MM-yyyy").parse(ds);
                }
                catch (ParseException e){
                    System.out.println("Date could not be parsed");
                    System.exit(0);
                }
                Objective o = new Objective(title, dt);
                if(o.getCountdown() < 0){
                    System.out.println("Invalid date");
                }
                else{
                    //TODO add objective task
                }
                break;
            default:
                System.out.println("Invalid command");
                break;
        }
    }

    public void addList(){
        Scanner key = new Scanner(System.in);
        System.out.print("List name: ");
        String title = key.nextLine();
        //TODO add list
    }

    public void Run(){
        Scanner key = new Scanner(System.in);
        while(true){
            System.out.println("COMMANDS");
            System.out.println("GTT - Go to notes");
            System.out.println("GTL - Go to lists");
            System.out.print("Enter command: ");
            String choice = key.nextLine();
            switch (choice.toUpperCase()){
                case "GTT":
                    while(true){
                        System.out.println("NOTES");
                        printAllTasks();
                        System.out.println("COMMANDS");
                        System.out.println("AN - Add new note");
                        System.out.println("SN - Select a note (By index)");
                        System.out.println("DN - Delete a note (By index)");
                        System.out.println("BK - Back");
                        System.out.print("Enter command: ");

                        String c = key.nextLine();
                        if(c.toUpperCase().equals("BK")){
                            break;
                        }
                        switch (c.toUpperCase()){
                            case "AN":
                                addTask();
                                break;
                            case "SN":
                                System.out.print("Note index: ");
                                int idx = key.nextInt();
                                key.nextLine();
                                //TODO select task
                                break;
                            case "DN":
                                System.out.print("Note index: ");
                                idx = key.nextInt();
                                key.nextLine();
                                //TODO delete task
                                break;
                            default:
                                System.out.println("Invalid command");
                                break;
                        }
                    }
                    break;
                case "GTL":
                    while (true){
                        System.out.println("LISTS");
                        //TODO print all lists if none print "no lists"

                        System.out.println("COMMANDS");
                        System.out.println("AL - Add new list");
                        System.out.println("SL - Select a list (By index)");
                        System.out.println("DL - Delete a list (By index)");
                        System.out.println("BK - Back");
                        System.out.print("Enter command: ");
                        String c = key.nextLine();
                        if(c.toUpperCase().equals("BK")){
                            break;
                        }
                        switch (c.toUpperCase()){
                            case "AL":
                                addList();
                                break;
                            case "SL":
                                System.out.print("List index: ");
                                int idx = key.nextInt();
                                key.nextLine();
                                //TODO select list
                                break;
                            case "DL":
                                System.out.print("List index: ");
                                idx = key.nextInt();
                                key.nextLine();
                                //TODO delete list
                                break;
                            default:
                                System.out.println("Invalid command");
                                break;
                        }

                    }
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }
}
