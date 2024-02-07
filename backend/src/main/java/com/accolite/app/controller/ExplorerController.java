package com.accolite.app.controller;// ExplorerController.java

import com.accolite.app.dto.ExplorerItem;
import com.accolite.app.entity.Candidate;
import com.accolite.app.entity.Question;
import com.accolite.app.repo.CandidateRepository;
import com.accolite.app.repo.QuestionRepository;
import com.accolite.app.service.ConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ExplorerController {
    private final ConverterService converterService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    CandidateRepository candidateRepository;

    @GetMapping("/explorer")
    public ResponseEntity<List<ExplorerItem>> getExplorerData(@RequestParam Long id) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null)
            return ResponseEntity.status(500).body(null);
        String type = question.getType();
        String title = question.getName();
        String directoryPath = "C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\ExtractedQuestions\\" + type + "\\" + title;
        List<ExplorerItem> explorerData = converterService.fetchDataFromDirectory(new File(directoryPath));

        return ResponseEntity.ok(explorerData);
    }

    @GetMapping("/explorer/test")
    public ResponseEntity<List<ExplorerItem>> getCandidate(@RequestParam Long id, @RequestParam String email) {
        Candidate candidate = candidateRepository.findByEmail(email);
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null)
            return ResponseEntity.status(500).body(null);
        String type = question.getType();
        String title = question.getName();
        String directoryPath = "C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\Test\\Candidate" + candidate.getId() + "\\" + type + "\\" + title;
        List<ExplorerItem> explorerData = converterService.fetchDataFromDirectory(new File(directoryPath));

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
            Map<String, String> response = new HashMap<>();
            response.put("content", "Error reading file content.");
            return ResponseEntity.status(500).body(response);
        }
    }


}
