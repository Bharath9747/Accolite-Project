package com.accolite.app.controller;

// FileUploadController.java

import com.accolite.app.entity.Question;
import com.accolite.app.repo.QuestionRepository;
import com.accolite.app.service.ExtracterService;
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
    public ResponseEntity<Map<String, String>> handleFileUpload(
            @RequestParam("question") MultipartFile questionZip
    ) {
        try {
            Question question = new Question();
            question.setQuestionZip(questionZip.getBytes());
            question = questionRepository.save(question);
            Long id = question.getId();
            extracterService.extractZipToFolder(id, question.getQuestionZip());
            Map<String, String> map = new HashMap<>();
            map.put("result", "Files uploaded successfully!");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("result", e.getMessage());

            return ResponseEntity.status(500).body(map);
        }
    }
}
