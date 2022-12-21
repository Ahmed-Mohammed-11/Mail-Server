package com.example.demo.Controllers;


import com.example.demo.Services.LogInService;
import com.example.demo.Services.RegisterService;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin

public class Controller {
    @Autowired
    private RegisterService registerService ;
    @Autowired
    private LogInService logInService ;
    @PostMapping(value = "/register")
    public String createUser(@RequestBody String user) throws IOException, ParseException {
        return registerService.createUser(user);
    }

    @PostMapping(value = "/login")
    public String login(@RequestBody String user) throws IOException, ParseException{
        return logInService.letUserIn(user);
    }
}
