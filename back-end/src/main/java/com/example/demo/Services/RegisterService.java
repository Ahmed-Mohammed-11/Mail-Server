package com.example.demo.Services;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


@Service
public class RegisterService {
    private String filePath = "D:\\Web\\Mail-Server\\back-end\\src\\main\\java\\com\\example\\demo\\DataBase\\users.json";

    public String createUser(String user) throws IOException, ParseException {

        JSONObject userJson = new JSONObject(user);
        JSONParser parser  = new JSONParser();
        FileReader file = new FileReader(filePath);
        Object load =  parser.parse(file);
        JSONArray a = new JSONArray(load.toString());
        a.put(userJson);
        try {
            FileWriter users = new FileWriter(filePath);         // writing back to the file
            users.write(a.toString());
            users.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return " hi ";
    }
}
