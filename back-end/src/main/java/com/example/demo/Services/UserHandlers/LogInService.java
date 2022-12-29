package com.example.demo.Services.UserHandlers;
import com.example.demo.Services.DatabaseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

@Service
public class LogInService {
    String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\com\\example\\demo\\Database\\";

    private DatabaseHandler dbHandler = DatabaseHandler.getInstance();
    private JSONArray users;

    public int letUserIn(String user, HttpServletResponse response) throws IOException, ParseException {

        //convert user to json object
        JSONObject userJson = new JSONObject(user);

        //just dealing with json array users instead of reading each time from the database "Flyweight"
        users = dbHandler.getUsers();

        //loop through current users to check if the username was already taken
        for (int i = 0; i < users.length(); i++) {
            JSONObject elementInArray = users.getJSONObject(i);
            if (elementInArray.get("email").equals(userJson.get("email"))) {
                if (elementInArray.get("password").equals(userJson.get("password"))) {
                    String UUID = elementInArray.get("uuid").toString();
                    response.setHeader("Set-Cookie", "user_id=" + UUID + "; Path=/");
                    return 200;
                }
            }
        }
        return 404;
    }
}
