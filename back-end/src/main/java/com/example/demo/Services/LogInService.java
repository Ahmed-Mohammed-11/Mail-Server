package com.example.demo.Services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Service
public class LogInService {
    String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\com\\example\\demo\\Database\\";

    public String letUserIn(String user) throws IOException, ParseException {

        //convert user to json object
        JSONObject userJson = new JSONObject(user);
        JSONParser parser  = new JSONParser();
        FileReader file = new FileReader(filePath + "users.json");
        //parse the previous content of the users.json file and store it in object loaded
        Object loaded =  parser.parse(file);
        //convert the object to json array to be able to traverse and add the new object to it
        JSONArray a = new JSONArray(loaded.toString());
        //loop through current users to check if the username was already taken
        for(int i = 0 ; i < a.length() ; i ++){
            JSONObject elementInArray = a.getJSONObject(i);
            if (elementInArray.get("email").equals(userJson.get("email"))){
                if (elementInArray.get("password").equals(userJson.get("password"))){
                    return  elementInArray.get("uuid").toString();
                }
            }
        }
        return "email and/or password incorrect";
    }
}
