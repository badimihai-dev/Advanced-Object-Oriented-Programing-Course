package com.assets.store;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Reducer {
    private static Reducer reducer_instance = null;
    private BufferedReader csvReader;

    private Reducer(){
    }

    public static Reducer create(){
        if(reducer_instance == null){
            reducer_instance = new Reducer();
        }
        return reducer_instance;
    }

    public static List<?> unpack(Object obj) {
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
                                if(j == declaredSubFields.size()-1){
                                    arrayString += subValue.toString();
                                }
                                else{
                                    arrayString += subValue.toString() + "$";
                                }
                            }
                            if(i != valueArray.size()-1) {
                                arrayString += "|";
                            }
                        }
                    }

                    output += arrayString;
                }
                else{
                    output += value.toString();
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

    public void write(Object obj){
        String filePath = obj.getClass().getSimpleName() + ".csv";
        Actions.write( filePath, ToCsv(obj));
    }

    public void read(String fileName){
        String filePath = fileName + ".csv";
        Actions.read(filePath);
    }

    public void update(Object obj){

    }

    public void delete(Object obj){

    }
}