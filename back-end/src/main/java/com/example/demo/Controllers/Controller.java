package com.example.demo.Controllers;
import com.example.demo.Services.Emails.ComposeService;
import com.example.demo.Services.UserHandlers.LogInService;
import com.example.demo.Services.UserHandlers.RegisterService;
import com.example.demo.Services.Emails.MailService;

import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.util.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@CrossOrigin
public class Controller {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private MailService Mail;
    @Autowired
    private LogInService logInService;
    @Autowired
    private ComposeService composeService ;

    @PostMapping(value = "/register")
    public String createUser(@RequestBody String user, HttpServletResponse response) throws IOException, ParseException {
        return registerService.createUser(user, response);
    }

    @PostMapping(value = "/login")
    public Map<String, String> login(@RequestBody String user, HttpServletResponse response, HttpServletRequest request) throws IOException, ParseException{
        Map<String, String> respObj = new HashMap<>();
        if (WebUtils.getCookie(request, "user_id") == null){
            int login = logInService.letUserIn(user, response);
            if (login == 200) {
                respObj.put("status", "success");
            } else {
                respObj.put("status", "failed");
                respObj.put("reason", "User not found");
            }
        }
        else{
            respObj.put("status", "failed");
            respObj.put("reason", "User already logged in");
        }
        return respObj;
    }

    @RequestMapping(value="/getEmails", method=RequestMethod.GET)
    public ObjectNode getEmails(HttpServletRequest request) throws IOException, ParseException{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode respObj = mapper.createObjectNode();

        if (WebUtils.getCookie(request, "user_id") != null){
            String uuid = WebUtils.getCookie(request, "user_id").getValue();
            JSONArray emails = Mail.getEmails(uuid);
            respObj.put("results", "LIST OF EMAILS :)");

        } else {
            respObj.put("results", "User not logged in");
        }
        return respObj;
    }

    @PostMapping(value = "/compose")
    public String compose(@RequestBody String email) throws IOException, ParseException{
        return composeService.compose(email);
    }
}
