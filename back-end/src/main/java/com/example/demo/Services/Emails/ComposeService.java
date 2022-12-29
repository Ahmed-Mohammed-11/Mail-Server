package com.example.demo.Services.Emails;
import com.example.demo.Services.DatabaseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class ComposeService {

    private DatabaseHandler dbHandler = DatabaseHandler.getInstance();
    private JSONArray users ;
    public String compose(String email) throws IOException, ParseException {

        boolean fromExists = false ;
        boolean toExists = false ;
        String senderUUID = "" ;
        String receiverUUID  = "" ;

        users = dbHandler.getUsers();

        JSONObject emailJson = new JSONObject(email);

        //check if the sender and receiver emails exist

        for( int i = 0 ; i < users.length() ; i ++){
            JSONObject user = users.getJSONObject(i);
            if(user.get("email").equals(emailJson.get("from"))){
                if(user.get("uuid").equals(emailJson.get("uuid"))){
                    fromExists = true ;
                    senderUUID = user.get("uuid").toString();
                }
            }
        }

        for( int i = 0 ; i < users.length() ; i ++){
            JSONObject user = users.getJSONObject(i);
            if(user.get("email").equals(emailJson.get("to"))){
                toExists = true ;
                receiverUUID = user.get("uuid").toString();
            }
        }

        if(toExists && fromExists){
            dbHandler.saveEmail(emailJson, senderUUID, emailJson.get("emailID").toString(), "sent");
            dbHandler.saveEmail(emailJson, receiverUUID , emailJson.get("emailID").toString(), "inbox");
            return "success";
        }

        return "error";
    }
}