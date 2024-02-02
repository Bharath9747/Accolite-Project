package com.accolite.app.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ExtracterService {
    String BASE_PATH="C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\ExtractedQuestions\\";

    public void extractZipToFolder(Long id,byte[] file) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file);
             ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)) {
            String destinationFolder = BASE_PATH+"Question"+id+"\\";
            File folder = new File(destinationFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String entryName = zipEntry.getName();
                File entryFile = new File(destinationFolder + File.separator + entryName);

                if (zipEntry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    try (FileOutputStream fileOutputStream = new FileOutputStream(entryFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }

                zipInputStream.closeEntry();
            }
        }
    }
    public void createReadmeFile(Long id,byte[] readmeBlob) throws IOException {
        String readmeContent = new String(readmeBlob, StandardCharsets.UTF_8);
        String destinationFolderPath = BASE_PATH+"Question"+id+"\\";
        File destinationFolder = new File(destinationFolderPath);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        File readmeFile = new File(destinationFolder, "Readme.md");
        try (FileOutputStream fileOutputStream = new FileOutputStream(readmeFile)) {
            fileOutputStream.write(readmeContent.getBytes(StandardCharsets.UTF_8));
        }
    }
}
