package com.example.demo.Services.Emails;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class MailService {
    String DB_PATH = System.getProperty("user.dir") + "\\src\\main\\java\\com\\example\\demo\\Database\\";
    public JSONArray getEmails(String uuid) {
        JSONArray emails = new JSONArray();
        File folder = new File(DB_PATH + uuid);
        if (folder.exists() && folder.isDirectory()){
            File[] listOfFiles = folder.listFiles();  // List of Mail Boxes (Folders)
            for (File file: listOfFiles) {
                if (file.isFile()) {
                    JSONObject batch = new JSONObject();
                    JSONArray emailsInBatch = new JSONArray();

                    // To Do: Read emails from file and add them to emailsInBatch



                    batch.put("folder", file.getName());
                    batch.put("emails", emailsInBatch);
                    emails.put(batch);
                }
            }
        }
        return emails;
    }

    public void sendEmail(String uuid, String email) {
        // To Do: Save email to file
    }

    public void deleteEmail(String uuid, String email) {
        // To Do: Delete email from file
    }

    public void moveEmail(String uuid, String email) {
        // To Do: Move email to another folder
    }

    public void createFolder(String uuid, String folderName) {
        // To Do: Create folder
    }

    public void deleteFolder(String uuid, String folderName) {
        // To Do: Delete folder
    }

    public void renameFolder(String uuid, String folderName) {
        // To Do: Rename folder
    }

    public void markAsRead(String uuid, String email) {
        // To Do: Mark email as read
    }

    public void markAsUnread(String uuid, String email) {
        // To Do: Mark email as unread
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

    public void markAsNotSpam(String uuid, String email) {
        // To Do: Mark email as not spam
    }
}
