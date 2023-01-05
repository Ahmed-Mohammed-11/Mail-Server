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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import org.springframework.http.MediaType;
import java.io.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public Map<String, String> login(@RequestBody String user, HttpServletResponse response) throws Exception{
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
    public String getEmails( @PathVariable String uuid, @PathVariable String folderName) throws Exception{
        if (uuid != null){
            JSONArray emails = Mail.getEmails(uuid, folderName);
            if (emails != null) {
                return emails.toString();
            } else {
                return "null";
            }
        } else {
            return "User is not logged in";
        }
    }

    @PostMapping(value = "/compose")
    public String compose(@RequestBody String email) throws Exception{
        return Mail.compose(email);
    }


    @PostMapping(value = "/createFolder/{uuid}/{folderName}")
    public String create(HttpServletRequest request, @PathVariable String uuid, @PathVariable String folderName){
        if(Mail.createFolder(uuid, folderName)){
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


    @PostMapping(value = "/renameFolder/{uuid}/{oldFolderName}/{newFolderName}")
    public String create( @PathVariable String uuid, @PathVariable String oldFolderName, @PathVariable String newFolderName){
        if(Mail.renameFolder(uuid, oldFolderName, newFolderName)){
            return "success";
        }else{
            return "error";
        }
    }


    @DeleteMapping(value = "/deleteFolder/{uuid}/{folderName}")
    public String delete(HttpServletRequest request, @PathVariable String uuid, @PathVariable String folderName){
        if(Mail.deleteFolder(uuid, folderName)){
            return "success";
        }else{
            return "error";
        }
    }


    @PostMapping(value = "/moveEmail/{uuid}/{oldFolder}/{newFolder}/{eID}")
    public String move(HttpServletRequest request, @PathVariable String uuid, @PathVariable String eID, @PathVariable String oldFolder, @PathVariable String newFolder){
        if(Mail.moveEmail(uuid, oldFolder, newFolder, eID)){
            return "success";
        }else{
            return "error";
        }
    }

    @DeleteMapping(value = "/deleteEmail/{uuid}/{folderName}/{emailID}")
    public Map<String, String> delete(HttpServletRequest request, @PathVariable String uuid, @PathVariable String folderName, @PathVariable String emailID){
        Map<String, String> respObj = new HashMap<>();
        if(Mail.deleteEmail(uuid, folderName, emailID)){
            respObj.put("status", "success");
        }else{
            respObj.put("status", "error");
        }
        return respObj;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload( @RequestParam("file") MultipartFile file ) {
        String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\com\\example\\demo\\Database\\attachments\\";
        String fileName = file.getOriginalFilename();
        try {
          file.transferTo( new File(filePath + fileName) );
        } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } 
        return ResponseEntity.ok("File uploaded successfully.");
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename) throws IOException {
        
        String FOLDER_PATH = System.getProperty("user.dir") + "\\src\\main\\java\\com\\example\\demo\\Database\\attachments\\";
        File file = new File(FOLDER_PATH + File.separator + filename);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));


        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    
}