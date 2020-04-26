package com.assets.store;

import java.io.*;
import java.util.Scanner;

public class Actions {
    private static String directory = System.getProperty("user.dir");
    private static String storePath = ".src.com.assets.store.storage.".replace(".",File.separator);

    public static void write(String fileName, String fileContent){
        String absolutePath = directory + storePath + fileName;
        File f = new File(absolutePath);

        try{
            PrintWriter out = null;
            if ( f.exists() && !f.isDirectory() ) {
                out = new PrintWriter(new FileOutputStream(new File(absolutePath), true));
            }
            else {
                out = new PrintWriter(absolutePath);
            }
            out.append(fileContent);
            out.close();


        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public static String read(String fileName){
        String absolutePath = directory + storePath + fileName;
        String output = "";

        try {
            File myObj = new File(absolutePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                output += data;
                if(myReader.hasNextLine()){
                    output += "\n";
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        return output;
    }

    public static void update(String fileName, String fileContent){
        String absolutePath = directory + storePath + fileName;
        File f = new File(absolutePath);

        try{
            PrintWriter out = null;
            if ( f.exists() && !f.isDirectory() ) {
                out = new PrintWriter(new FileOutputStream(new File(absolutePath), false));
            }
            else {
                out = new PrintWriter(absolutePath);
            }
            out.write(fileContent);
            out.close();


        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
