package com.assets.store;

import com.assets.TodoList;
import com.assets.derivedtasks.General;
import com.assets.derivedtasks.Objective;
import com.assets.derivedtasks.Planned;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Reducer extends Store{
    private static Reducer reducer_instance = null;

    //Initialize virtual store
    private Reducer() {
        for(String type : storeTypes){
            String fileContent = this.read(type);
            if(fileContent != ""){
                String[] parsedContent = fileContent.split("\n");
                for(String task: parsedContent) {
                    String[] elements = task.split(",");
                    switch (type) {
                        case "General":
                            int importancy = Integer.parseInt(elements[0]);
                            String title = elements[1].replace("&comma;", ",");
                            boolean status = Boolean.parseBoolean(elements[2]);
                            General t = new General(title, status, importancy);
                            allTasks.add(t);
                            break;
                        case "Objective":
                            title = elements[4].replace("&comma;", ",");
                            Date date = new Date();
                            try{
                                date=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(elements[3]);
                            }catch (ParseException e){
                                System.out.println("Date parse Failed");
                                System.exit(0);
                            }

                            status = Boolean.parseBoolean(elements[5]);
                            Objective o = new Objective(title, date);
                            if(status){
                                o.toggleStatus();
                            }
                            if(!elements[1].equals("")){
                                String[] subTasks = elements[1].split("\\|");
                                for(String subTask : subTasks){
                                    String[] st = subTask.split("\\$");
                                    o.addSubtask(st[0], Boolean.parseBoolean(st[1]));
                                }
                            }
                            allTasks.add(o);
                            break;
                        case "Planned":
                            title = elements[1].replace("&comma;", ",");
                            date = new Date();
                            try{
                                date=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(elements[0]);
                            }catch (ParseException e){
                                System.out.println("Date parse Failed");
                                System.exit(0);
                            }

                            status = Boolean.parseBoolean(elements[2]);
                            Planned p = new Planned(title, status, date);
                            allTasks.add(p);
                            break;
                        case "TodoList":
                            title = elements[0].replace("&comma;", ",");
                            TodoList tl = new TodoList(title);
                            if(elements.length == 2 && !elements[1].equals("")){
                                String[] subTasks = elements[1].split("\\|");
                                for(String subTask : subTasks){
                                    String[] st = subTask.split("\\$");
                                    tl.addTasks(st[1], Boolean.parseBoolean(st[2]), Integer.parseInt(st[0]));
                                }
                            }
                            allList.add(tl);
                            break;
                    }
                }
            }
        }
    }

    public static Reducer create(){
        if(reducer_instance == null){
            reducer_instance = new Reducer();
        }
        return reducer_instance;
    }

    private static List<?> unpack(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
    }

    private ArrayList<Field> GetAllFields(Object obj){
        ArrayList<Field> fields = new ArrayList<Field>();

        Class c = obj.getClass();
        while(c != null) {
            for(Field field : c.getDeclaredFields()){
                fields.add(field);
            }
            c = c.getSuperclass();
        }

        return fields;
    }

    private String ToCsv(Object test){
        String output = "";

        //GET ALL FIELDS
        ArrayList<Field> declaredFields = GetAllFields(test);

        try{
            for (int f = 0; f< declaredFields.size(); f++) {
                Field field = declaredFields.get(f);

                field.setAccessible(true);
                Object value = field.get(test);
                if(List.class.isAssignableFrom(field.getType())){
                    String arrayString = "";

                    List valueArray = unpack(value);

                    if(!valueArray.isEmpty()){
                        ArrayList<Field> declaredSubFields = GetAllFields(valueArray.get(0));

                        for(int i=0; i< valueArray.size(); i++){
                            for(int j=0; j< declaredSubFields.size(); j++){
                                Field subField = declaredSubFields.get(j);
                                Object item = valueArray.get(i);

                                subField.setAccessible(true);
                                Object subValue = subField.get(item);
                                arrayString += subValue.toString();
                                if(j != declaredSubFields.size()-1){
                                    arrayString += "$";
                                }
                            }
                            if(i != valueArray.size()-1) {
                                arrayString += "|";
                            }
                        }
                    }

                    output += arrayString.replace(",", "&comma;");
                }
                else{
                    output += value.toString().replace(",", "&comma;");
                }
                if(f != declaredFields.size()-1){
                    output += ",";
                }
                else{
                    output += "\n";
                }
            }
        }catch(IllegalAccessException e){
            System.out.println("Illegal Access Eception thrown");
            System.exit(0);
        }

        return output;
    }

    private String read(String fileName){
        String filePath = fileName + ".csv";
        return Actions.read(filePath);
    }

    public void write(Object obj){
        String filePath = obj.getClass().getSimpleName() + ".csv";
        Actions.write( filePath, ToCsv(obj));
    }

    public void update(Object obj){
        String filePath = obj.getClass().getSimpleName() + ".csv";

        String fileContent = "";

        String className = obj.getClass().getSimpleName();

        if(obj.getClass().getSimpleName() != "TodoList"){
            for(Object task : allTasks){
                String taskClass = task.getClass().getSimpleName();
                if(taskClass == className){
                    fileContent += ToCsv(task);
                }
            }
        }
        else{
            for(Object list : allList){
                String listClass = list.getClass().getSimpleName();
                if(listClass == className){
                    fileContent += ToCsv(list);
                }
            }
        }


        Actions.update( filePath, fileContent);
    }

}