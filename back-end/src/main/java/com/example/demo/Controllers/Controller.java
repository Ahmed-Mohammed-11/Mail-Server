package com.example.demo.Controllers;
import com.example.demo.Services.Emails.ComposeService;
import com.example.demo.Services.UserHandlers.LogInService;
import com.example.demo.Services.UserHandlers.RegisterService;
import com.example.demo.Services.Emails.MailService;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
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
    private ComposeService composeService;

    @PostMapping(value = "/register")
    public String createUser(@RequestBody String user, HttpServletResponse response) throws Exception {
        return registerService.createUser(user, response);
    }

    @PostMapping(value = "/login")
    public Map<String, String> login(@RequestBody String user, HttpServletResponse response, HttpServletRequest request) throws Exception{
        Map<String, String> respObj = new HashMap<>();
        String login = logInService.letUserIn(user, response);
        if (login.length() != 3) {
            respObj.put("status", "success");
            respObj.put("uuid", login);
        } else {
            respObj.put("status", "failed");
            respObj.put("reason", "User not found");
        }
        return respObj;
    }

    @RequestMapping(value="/getEmails/{uuid}/{folderName}", method=RequestMethod.GET)
    public String getEmails(HttpServletRequest request, @PathVariable String uuid, @PathVariable String folderName) throws Exception{
        if (uuid != null){
            JSONArray emails = Mail.getEmails(uuid, folderName);
            return emails.toString();
        } else {
            return "User is not logged in";
        }
    }

    @PostMapping(value = "/compose")
    public String compose(@RequestBody String email) throws Exception{
        return composeService.compose(email);
    }

    @PostMapping(value = "/createFolder/{uuid}/{folderName}")
    public String create(HttpServletRequest request, @PathVariable String uuid, @PathVariable String folderName){
        if(Mail.createFolder(uuid, folderName)){
            return "success";
        }else{
            return "error";
        }
    }

    @DeleteMapping(value = "/deleteFolder/{folderName}")
    public String delete(HttpServletRequest request, @PathVariable String folderName){
        String uuid = WebUtils.getCookie(request, "user_id").getValue();
        if(Mail.deleteFolder(uuid, folderName)){
            return "success";
        }else{
            return "error";
        }
    }

    @GetMapping(value = "/getFolders/{uuid}")
    public String[] getFolders(HttpServletRequest request, @PathVariable String uuid){
        if (uuid != null){
            String[] folders = Mail.getFolders(uuid);
            return folders;
        } else {
            return null;
        }
    }

    @PostMapping(value = "/moveEmail/{oldFolder}/{newFolder}")
    public String move(HttpServletRequest request, @PathVariable String oldFolder, @PathVariable String newFolder, @RequestBody String emailID){
        String uuid = WebUtils.getCookie(request, "user_id").getValue();
        JSONObject emailIDJson = new JSONObject(emailID);
        String eID = emailIDJson.getString("emailID");
        if(Mail.moveEmail(uuid, oldFolder, newFolder, eID)){
            return "success";
        }else{
            return "error";
        }
    }

    @DeleteMapping(value = "/deleteEmail/{folderName}")
    public String delete(HttpServletRequest request, @PathVariable String folderName, @RequestBody String emailID){
        String uuid = WebUtils.getCookie(request, "user_id").getValue();
        JSONObject emailIDJson = new JSONObject(emailID);
        String eID = emailIDJson.getString("emailID");
        if(Mail.deleteEmail(uuid, folderName, eID)){
            return "success";
        }else{
            return "error";
        }
    }
}
