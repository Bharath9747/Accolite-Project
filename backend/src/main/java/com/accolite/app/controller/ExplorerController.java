package com.accolite.app.controller;// ExplorerController.java

import com.accolite.app.dto.ExplorerItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ExplorerController {
    @GetMapping("/explorer")
    public ResponseEntity<List<ExplorerItem>> getExplorerData(@RequestParam Long id) {
        // Replace this with the path to your local directory
        String directoryPath = "C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\ExtractedQuestions\\Question" + id;

        List<ExplorerItem> explorerData = fetchDataFromDirectory(new File(directoryPath));

        return ResponseEntity.ok(explorerData);
    }

    @PostMapping("explorer/file-content")
    public ResponseEntity<Map<String, String>> getFileContent(@RequestBody Map<String, String> requestData) {
        String absolutePath = requestData.get("absolutePath");
        try {
            File file = new File(absolutePath);
            String content = new String(Files.readAllBytes(file.toPath()));
            Map<String, String> response = new HashMap<>();
            response.put("content", content);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("content", "Error reading file content.");
            return ResponseEntity.status(500).body(response);
        }
    }

    private List<ExplorerItem> fetchDataFromDirectory(File directory) {
        File[] files = directory.listFiles();

        if (files == null) {
            return null;
        }

        return Arrays.stream(files)
                .map(file -> {
                    if (file.isDirectory()) {
                        List<ExplorerItem> subItems = fetchDataFromDirectory(file);
                        return new ExplorerItem(file.getName(), "folder", file.getAbsolutePath(), subItems);
                    } else {
                        if ((file.getName().equals("Readme.md")||file.getName().equals("Solution.java") || file.getName().equals("Solution.cpp")|| file.getName().equals("Solution.py"))) {
                            return new ExplorerItem(file.getName(), "file", file.getAbsolutePath());
                        } else {
                            return null; // or return Collections.emptyList();
                        }
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }
}
