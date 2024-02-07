package com.accolite.app.controller;

// FileUploadController.java

import com.accolite.app.entity.Question;
import com.accolite.app.repo.QuestionRepository;
import com.accolite.app.service.ExtracterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:4200/")
@RequiredArgsConstructor
public class FileUploadController {
    private final ExtracterService extracterService;

    @Autowired
    QuestionRepository questionRepository;

    @PostMapping("/upload")
    @Transactional
    public ResponseEntity<Map<String, String>> handleFileUpload(
            @RequestParam("file") MultipartFile compressedData,
            @RequestParam("type") String type,
            @RequestParam("title") String title
    ) {
        try {
            Question question = new Question();
            question.setZip(compressedData.getBytes());
            question.setName(title);
            question.setType(type);
            question = questionRepository.save(question);
            extracterService.extractZipToFolder(title, type, question.getZip());
            Map<String, String> map = new HashMap<>();
            map.put("result", type + " Question uploaded successfully!");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("result", e.getMessage());

            return ResponseEntity.status(500).body(map);
        }
    }
}
