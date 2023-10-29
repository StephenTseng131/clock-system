package com.example.clocksystem.utils;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class BackupDatabase {
    public void backupDatabase() {
        try {
            String command = "mysqldump -u root -p 123456 clock_system > D:/clock_system_backup.sql";
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
