package com.accolite.app.service;

import com.accolite.app.entity.Question;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ExtracterService {
    String BASE_PATH="C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\";

    public void extractZipToFolder(String title,String type,byte[] file) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file);
             ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)) {
            String destinationFolder = BASE_PATH+"ExtractedQuestions\\"+type+"\\"+title+"\\";
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

    public void extractZipForCandidate(Long id, Question question)throws IOException  {
        byte[] questionFolder = question.getCompressedData();
        String type = question.getType();
        String title = question.getTitle();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(questionFolder);
             ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)) {
            String destinationFolder = BASE_PATH+"Test\\Candidate"+id+"\\"+type+"\\"+title+"\\";
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
                    if ((entryFile.getName().equals("README.md") ||
                            entryFile.getName().equals("Solution.java") ||
                            entryFile.getName().equals("Solution.cpp") ||
                            entryFile.getName().equals("Solution.py") ||
                            entryFile.getName().equals("Solution.sql"))) {
                        try (FileOutputStream fileOutputStream = new FileOutputStream(entryFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, bytesRead);
                            }
                        }
                    }
                }

                zipInputStream.closeEntry();
            }
        }

    }
}
