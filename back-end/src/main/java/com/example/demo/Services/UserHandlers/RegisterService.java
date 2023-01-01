package com.example.demo.Services.UserHandlers;
import com.example.demo.Services.DatabaseHandler;
import com.example.demo.Services.SchemaValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@Service
public class RegisterService {

    private DatabaseHandler dbHandler = DatabaseHandler.getInstance();
    private JSONArray users ;

//    private InputStream schemaStream = dbHandler.getRegisterSchema();;

    private SchemaValidator schemaValidator = SchemaValidator.getInstance() ;
    private String schema ;

    public String createUser(String user, HttpServletResponse response) throws Exception {

        schema = dbHandler.getRegisterSchema();
        if(schemaValidator.validate(schema , user)) {
            //convert the user from string to json object
            JSONObject userJson = new JSONObject(user);

            //just dealing with json array users instead of reading each time from the database "Flyweight"
            users = dbHandler.getUsers();

            for (int i = 0; i < users.length(); i++) {
                JSONObject elementInArray = users.getJSONObject(i);
                if (elementInArray.get("email").equals(userJson.get("email"))) {
                    //return the error if it's already taken
                    return "this email is already taken";
                }
            }

            //generate random user uuid
            UUID uuid = UUID.randomUUID();
            //if you managed to pass the loop put the generated uuid as a new entry in the user object
            userJson.put("uuid", uuid);
            //add the object in the array
            users.put(userJson);
            //save the json array of users and handle creation of directory system
            dbHandler.saveUsers(users, String.valueOf(uuid));
            //and as an indicator return the created session id for him

            response.setHeader("Set-Cookie", "user_id=" + uuid.toString() + "; Path=/");
            return uuid.toString();
        }return "validation error";
    }
}