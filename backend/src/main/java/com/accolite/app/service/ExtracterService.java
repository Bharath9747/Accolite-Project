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
                if (entryName.contains("/")) {
                    entryName = entryName.substring(entryName.indexOf('/') + 1);
                }
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
}
