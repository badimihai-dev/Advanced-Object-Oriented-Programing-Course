package com;

import com.assets.Task;
import com.assets.TodoList;
import com.assets.derivedtasks.General;
import com.assets.derivedtasks.Objective;
import com.assets.derivedtasks.Planned;
import com.assets.store.ReducerToDelete;
import com.assets.store.StoreToDelete;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class App extends StoreToDelete {
    private ReducerToDelete reducerToDelete;

    public App() {
        reducerToDelete = ReducerToDelete.create();
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
                            reducerToDelete.update(((General) t));
                            break;
                        case "CI":
                            System.out.print("Importancy: ");
                            int imp = keyboard.nextInt();
                            keyboard.nextLine();
                            assert t instanceof General;
                            ((General) t).updateImportancy(imp);
                            reducerToDelete.update(((General) t));
                            break;
                        case "RN":
                            System.out.print("New title: ");
                            String title = keyboard.nextLine();
                            t.setTitle(title);
                            reducerToDelete.update(t);
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
                            reducerToDelete.update((General) t);
                            break;
                        case "AS":
                            System.out.print("Subtask title: ");
                            title = keyboard.nextLine();
                            assert t instanceof Objective;
                            ((Objective) t).addSubtask(title, false);
                            reducerToDelete.update((Objective) t);
                            break;
                        case "PS":
                            assert t instanceof Objective;
                            ((Objective) t).printSubtasks();
                            reducerToDelete.update((Objective) t);
                            break;
                        case "US":
                            System.out.print("Subtask index: ");
                            int idx = keyboard.nextInt();
                            keyboard.nextLine();
                            assert t instanceof Objective;
                            ((Objective) t).updateSubtask(idx);
                            reducerToDelete.update((Objective) t);
                            break;
                        case "DS":
                            System.out.print("Subtask index: ");
                            idx = keyboard.nextInt();
                            keyboard.nextLine();
                            assert t instanceof Objective;
                            ((Objective) t).removeSubtask(idx);
                            reducerToDelete.update((Objective) t);
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
                    l.addTasks(title, false, imp);
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
        if(allTasks.size() == 0){
            System.out.println("No notes");
        }
        for(int i=0 ;i<allTasks.size(); i++){
            System.out.print(i + ". ");
            System.out.println(allTasks.get(i).getTitle());
            if(allTasks.get(i) instanceof General){
                System.out.println("   -Type: General");
            }
            if(allTasks.get(i) instanceof Planned){
                if(allTasks.get(i) instanceof Objective){
                    System.out.println("   -Type: Objective");
                }
                else{
                    System.out.println("   -Type: Planned");
                }
            }
        }
    }

    public void printAllLists(){
        for(int i=0 ;i<allList.size(); i++){
            System.out.print(i + ". ");
            System.out.println(allList.get(i).getListName());
        }
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
                allTasks.add(t);
                reducerToDelete.write(t);
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
                    allTasks.add(p);
                    reducerToDelete.write(p);
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
                    allTasks.add(o);
                    reducerToDelete.write(o);
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
        allList.add(new TodoList(title));
        reducerToDelete.write(new TodoList(title));
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
                                selectTask(allTasks.get(idx));
                                break;
                            case "DN":
                                System.out.print("Note index: ");
                                idx = key.nextInt();
                                key.nextLine();
                                allTasks.remove(idx);
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
                        if(allList.size() != 0){
                            printAllLists();
                        }
                        else{
                            System.out.println("No lists");
                        }
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
                                selectList(allList.get(idx));
                                break;
                            case "DL":
                                System.out.print("List index: ");
                                idx = key.nextInt();
                                key.nextLine();
                                allList.remove(idx);
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
