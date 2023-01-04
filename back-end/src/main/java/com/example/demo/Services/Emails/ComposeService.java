package com.example.demo.Services.Emails;
import com.example.demo.Services.DatabaseHandler;
import com.example.demo.Services.SchemaValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class ComposeService {

    private DatabaseHandler dbHandler = DatabaseHandler.getInstance();
    private JSONArray users;
    private SchemaValidator schemaValidator = SchemaValidator.getInstance();
    private String schema;
    public String compose(String email) throws Exception {

        schema = dbHandler.getEmailSchema();
        if (schemaValidator.validate(schema, email)) {
            JSONObject emailJson = new JSONObject(email);
            //size of recipients array in email object
            int toSize = emailJson.getJSONArray("to").length();
            JSONArray recipients = emailJson.getJSONArray("to");
            boolean fromExists = false;
            boolean toExist[] = new boolean[toSize];
            String senderUUID = "";
            String receiverUUIDs[] = new String[toSize];

            users = dbHandler.getUsers();

            //check if the sender's and receivers' emails exist

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.get("email").equals(emailJson.get("from"))) {
                    fromExists = true;
                    senderUUID = user.get("uuid").toString();
//                    emailJson.put("from_fullname", user.get("firstname").toString() + " " + user.get("lastname").toString());
                }
            }


            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                for (int j = 0; j < toSize; j++) {
                    if (user.get("email").equals(recipients.get(j))) {
                        toExist[j] = true;
                        receiverUUIDs[j] = user.get("uuid").toString();
                    }
//                    emailJson.put("to_fullname", user.get("firstname").toString() + " " + user.get("lastname").toString());
                }
            }


            if (fromExists) {
                //save the original email version with multiple recipients at sender sent folder
                dbHandler.saveEmail(emailJson, senderUUID, emailJson.get("emailID").toString(), "sent");
                for (int i = 0; i < toSize; i++) {
                    if (toExist[i]) {
                        //for each recipient set the "to" email correctly and save the email at his inbox
                        emailJson.put("to", recipients.get(i));
                        //security reasons "receiver shouldn't know sender's uuid of course"
                        if(! (emailJson.get("from").equals(recipients.get(i)))){
                            emailJson.remove("uuid");
                        }
                        dbHandler.saveEmail(emailJson, receiverUUIDs[i], emailJson.get("emailID").toString(), "inbox");
                    }
                }
                return "success";
            }return "error";
        }return "Validation Error !";
    }
}
