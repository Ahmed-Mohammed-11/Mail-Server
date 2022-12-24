package com.example.demo.Services;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.xml.validation.Schema;
import java.io.*;
import java.util.UUID;


@Service
public class RegisterService {
    //try to change to relative path instead of the relative path
    String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\com\\example\\demo\\Database\\";
//    private String filePath = "D:\\Web\\Mail-Server\\back-end\\src\\main\\java\\com\\example\\demo\\DataBase\\";
    // if the relative path didn't work uncomment the absolute path

    public String createUser(String user) throws IOException, ParseException {

        //convert the user from string to json object
        JSONObject userJson = new JSONObject(user);
        JSONParser parser  = new JSONParser();
        //try reading the users.json file
        FileReader file = new FileReader(filePath + "users.json");
        //parse the previous content of the users.json file and store it in object loaded
        Object loaded =  parser.parse(file);
        //convert the object to json array to be able to traverse and add the new object to it
        JSONArray a = new JSONArray(loaded.toString());
        //loop through current users to check if the username was already taken
        for(int i = 0 ; i < a.length() ; i ++){
            JSONObject elementInArray = a.getJSONObject(i);
            if (elementInArray.get("email").equals(userJson.get("email"))){
                //return the error if it's already taken
                return "this username is already taken";
            }
        }
        //generate random user uuid
        UUID uuid = UUID.randomUUID();
        //if you managed to pass the loop put the generated uuid as a new entry in the user object
        userJson.put("uuid", uuid);
        //add the object in the array
        a.put(userJson);
        //try to write your changes to users.json file
        try {
            FileWriter users = new FileWriter(filePath + "users.json");         // writing back to the file
            users.write(a.toString());
            users.flush();
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



        //and as an indicator return the created session id for him
        return uuid.toString();
    }
}
