package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.Date;
import java.util.function.Consumer;

import static java.lang.Math.abs;

@SpringBootApplication

public class MailServerApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(MailServerApplication.class, args);
    }
    @Component
    public class RunAfterStartup {

        public static void deleteFiles(File dir) {
            if (dir.isDirectory()) {
                for (File file : dir.listFiles()) {
                    deleteFiles(file);
                    if(file.getAbsolutePath().contains("trash")){
                        long diff = new Date().getTime() - file.lastModified();
                        if (diff > (30 * 24 * 60 * 60 * 1000L)) {file.delete();}
                    }
                }
            }
        }


        @EventListener(ApplicationReadyEvent.class)
        public void runAfterStartup() {
            System.out.println();
            deleteFiles(new File((System.getProperty("user.dir") + "\\src\\main\\java\\com\\example\\demo\\Database\\")));
        }
    }
}
