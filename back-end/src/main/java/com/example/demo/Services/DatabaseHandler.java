package com.example.demo.Services;
import org.apache.juli.logging.Log;
import org.json.simple.parser.JSONParser;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FilenameFilter;

import org.apache.commons.io.FileUtils;


public class DatabaseHandler {
    private static String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\com\\example\\demo\\Database\\";
    // private String filePath = "D:\\Web\\Mail-Server\\back-end\\src\\main\\java\\com\\example\\demo\\DataBase\\";
    private static JSONArray users ;

    private static Path RegisterSchema = Path.of(filePath + "RegisterSchema.json");
    private static Path LogInSchema = Path.of(filePath + "LogInSchema.json");
    private static Path EmailSchema = Path.of(filePath + "EmailSchema.json");


    //private instance to apply singleton design pattern
    private static final DatabaseHandler dbHandler = new DatabaseHandler();

    private DatabaseHandler() {}



    public static DatabaseHandler getInstance(){
        //singleton instance to make sure that just one instance of db is there in the code
        return dbHandler ;
    }


    public String getRegisterSchema() throws IOException {
        String schema = Files.readString(RegisterSchema);
        return schema;
    }



    public String getLogInSchema() throws IOException {
        String schema = Files.readString(LogInSchema);
        return schema;
    }


    public String getEmailSchema() throws IOException {
        String schema = Files.readString(EmailSchema);
        return schema;
    }




    public JSONArray getUsers() throws IOException, ParseException{
        if(users == null){
            JSONParser parser  = new JSONParser();
            FileReader file = new FileReader(filePath + "users.json");
            //parse the previous content of the users.json file and store it in object loaded
            Object loaded =  parser.parse(file);
            //convert the object to json array to be able to traverse and add the new object to it
            users = new JSONArray(loaded.toString());
        }
        //just dealing with json array users instead of reading each time from the database "Flyweight"
        return users;
    }



    public void saveUsers(JSONArray users, String uuid){

        try {
            // writing back to the users.json file
            FileWriter usersFile = new FileWriter(filePath + "users.json");
            usersFile.write(users.toString());
            usersFile.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //create some directories for that user
        File newUserFolder = new File(filePath + uuid);
        File inbox = new File(filePath + uuid + "\\inbox");
        File sent = new File(filePath + uuid + "\\sent");
        File drafts = new File(filePath + uuid + "\\drafts");
        File trash = new File(filePath + uuid + "\\trash");
        newUserFolder.mkdir();
        inbox.mkdir();
        sent.mkdir();
        drafts.mkdir();
        trash.mkdir();
    }



    public JSONArray getEmails(String uuid, String folderName) throws IOException, ParseException{

        File userDirectory = new File(filePath + uuid + "\\" + folderName);

        //File array to loop through all files in a given directory
        File[] directoryListing = userDirectory.listFiles();

        JSONParser parser  = new JSONParser();
        JSONArray  emails = new JSONArray();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                //take the file name and open the file to get its content
                FileReader file = new FileReader(child);
                Object loaded = parser.parse(file);
                emails.put(loaded);
            }
        } else {
            return null;
        }

        return emails;
    }



    public void saveEmail(JSONObject email, String uuid, String emailID, String emailStatus){
        FileWriter userFile = null ;
        try {
            switch (emailStatus){
                //save the email in sender and receiver files
                case "sent" : userFile = new FileWriter(filePath + uuid + "\\sent\\" + emailID + ".json");break;
                case "inbox" : userFile = new FileWriter(filePath + uuid + "\\inbox\\" + emailID + ".json");break;
                default: break;
            }
            userFile.write(email.toString());
            userFile.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String[] getFolders(String uuid){

        try {
            File file = new File(filePath + uuid);
            String[] directories = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isDirectory();
                }
            });

            String[] directories_filtered = new String[directories.length - 4];
            int j = 0;
            for (int i = 0; i < directories.length; i++) {
                if(!directories[i].equals("inbox") && !directories[i].equals("sent") && !directories[i].equals("drafts") && !directories[i].equals("trash")){
                    directories_filtered[j] = directories[i];
                    j += 1;
                }
            }

            return directories_filtered;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    public boolean createFolder(String uuid, String folderName){
        try {
            File customFolder = new File(filePath + uuid + "\\" + folderName);

            customFolder.mkdir();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean renameFolder(String uuid, String oldFolderName, String newFolderName) {
        File old = new File(filePath + uuid + "\\" +  oldFolderName);
        File newOne = new File(filePath + uuid + "\\" + newFolderName);

        if(old.renameTo(newOne)){
            return true;
        }else{
            return false;
        }
    }


    public boolean deleteFolder(String uuid, String folderName){
        try {
            FileUtils.deleteDirectory(new File(filePath + uuid + "\\" + folderName));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    public boolean moveEmail(String uuid, String oldFolder, String newFolder, String emailID){
        try {
            File openOld = new File(filePath + uuid + "\\" + oldFolder + "\\" + emailID + ".json");
            openOld.renameTo(new File(filePath + uuid + "\\" + newFolder + "\\" + emailID + ".json" ));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteEmail(String uuid, String folderName, String emailID){
        File email = new File(filePath + uuid + "\\" + folderName + "\\" + emailID + ".json");
        if (email.delete()) {return true ;}
        else {return false;}
    }


}
