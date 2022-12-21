package com.example.demo.Controllers;


import com.example.demo.Services.RegisterService;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(value = "/register")
public class Controller {
    @Autowired
    private RegisterService registerService ;
    @PostMapping()
    public String createUser(@RequestBody String user) throws IOException, ParseException {
        return registerService.createUser(user);
    }
}
