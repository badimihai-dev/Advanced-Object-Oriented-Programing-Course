package com.assets.store;

import java.io.*;

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

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public static String read(String fileName){
        String absolutePath = directory + storePath + fileName;
        String output = "";

        try(FileInputStream fileInputStream = new FileInputStream(absolutePath)) {
            int ch = fileInputStream.read();
            while(ch != -1) {
                output += ch;
                ch = fileInputStream.read();
            }
        } catch (FileNotFoundException e) {
            // exception handling
        } catch (IOException e) {
            // exception handling
        }

        return output;
    }

    public static void update(String fileName, String id, String content){

    }

    public static void delete(String fileName, String id){

    }
}
