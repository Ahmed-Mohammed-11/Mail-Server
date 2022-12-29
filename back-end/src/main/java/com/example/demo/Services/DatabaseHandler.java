package com.example.demo.Services;
import org.json.simple.parser.JSONParser;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.io.FileUtils;


public class DatabaseHandler {
    static String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\com\\example\\demo\\Database\\";
    // private String filePath = "D:\\Web\\Mail-Server\\back-end\\src\\main\\java\\com\\example\\demo\\DataBase\\";
    private static JSONArray users ;

    //private instance to apply singleton design pattern
    private static final DatabaseHandler dbHandler = new DatabaseHandler();

    private DatabaseHandler() {}

    public static DatabaseHandler getInstance(){
        //singleton instance to make sure that just one instance of db is there in the code
        return dbHandler ;
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

    public JSONArray getEmails(String uuid, String folderName) throws IOException, ParseException{

        File userDirectory = new File(filePath + uuid + "\\" + folderName);
        System.out.println(userDirectory);
        File[] directoryListing = userDirectory.listFiles();

        JSONParser parser  = new JSONParser();
        JSONArray  emails = new JSONArray();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                FileReader file = new FileReader(child);
                Object loaded = parser.parse(file);
                System.out.println(loaded);
                emails.put(loaded);
            }
        } else {
            return null;
        }

        System.out.println(emails);
        return emails;
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

    public boolean deleteFolder(String uuid, String folderName){
        try {
            FileUtils.deleteDirectory(new File(filePath + uuid + "\\" + folderName));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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
}
