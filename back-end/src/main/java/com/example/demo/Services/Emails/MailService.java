package com.example.demo.Services.Emails;
import com.example.demo.Services.DatabaseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;

@Service
public class MailService {
    private DatabaseHandler dbHanlder = DatabaseHandler.getInstance();
    private JSONArray emails ;

    public JSONArray getEmails(String uuid, String folderName) throws IOException, ParseException {
        emails = dbHanlder.getEmails(uuid, folderName);
        return emails;
    }

    public void deleteEmail(String uuid, String email) {
        // To Do: Delete email from file
    }

    public void moveEmail(String uuid, String email) {
        // To Do: Move email to another folder
    }

    public boolean createFolder(String uuid, String folderName) {
        return dbHanlder.createFolder(uuid, folderName);
    }

    public boolean deleteFolder(String uuid, String folderName) {
        return dbHanlder.deleteFolder(uuid, folderName);
    }

    public void markAsRead(String uuid, String email) {
        // To Do: Mark email as read
    }

    public void markAsImportant(String uuid, String email) {
        // To Do: Mark email as important
    }

    public void markAsUnimportant(String uuid, String email) {
        // To Do: Mark email as unimportant
    }

    public void markAsSpam(String uuid, String email) {
        // To Do: Mark email as spam
    }

}
