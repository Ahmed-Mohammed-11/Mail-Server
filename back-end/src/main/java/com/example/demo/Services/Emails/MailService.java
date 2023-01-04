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
    private ComposeService composeService = new ComposeService() ;

    public String compose(String email) throws Exception{return composeService.compose(email);}

    public JSONArray getEmails(String uuid, String folderName) throws IOException, ParseException {
        return dbHanlder.getEmails(uuid, folderName);
    }

    public boolean moveEmail(String uuid, String oldFolder, String newFolder , String emailID) {
        return dbHanlder.moveEmail(uuid, oldFolder, newFolder, emailID);
    }

    public boolean deleteEmail(String uuid, String folderName , String emailID) {
        return dbHanlder.deleteEmail(uuid, folderName, emailID);
    }

    public boolean createFolder(String uuid, String folderName) {
        return dbHanlder.createFolder(uuid, folderName);
    }

    public boolean renameFolder(String uuid, String oldFolderName, String newFolderName) {
        return dbHanlder.renameFolder(uuid, oldFolderName, newFolderName);
    }

    public boolean deleteFolder(String uuid, String folderName) {
        return dbHanlder.deleteFolder(uuid, folderName);
    }

    public String[] getFolders(String uuid) {
        return dbHanlder.getFolders(uuid);
    }


}
