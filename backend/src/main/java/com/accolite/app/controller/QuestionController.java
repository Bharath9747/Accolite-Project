package com.accolite.app.controller;

import com.accolite.app.dto.CandidateDTO;
import com.accolite.app.dto.CodeDTO;
import com.accolite.app.dto.QuestionDTO;
import com.accolite.app.entity.Candidate;
import com.accolite.app.entity.Question;
import com.accolite.app.repo.CandidateRepository;
import com.accolite.app.repo.QuestionRepository;
import com.accolite.app.service.CompilerService;
import com.accolite.app.service.ConverterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:4200/")
@RequiredArgsConstructor
public class QuestionController {
    private final ConverterService converterService;
    private final CompilerService compilerService;

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    CandidateRepository candidateRepository;

    @GetMapping("/question")
    public QuestionDTO getQuestion(@RequestParam Long id) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null)
            return null;
        return converterService.convertQuestionToDTO(question);
    }

    @GetMapping("/question/all")
    public List<QuestionDTO> getAllQuestion() {
        List<Question> question = questionRepository.findAll();

        List<QuestionDTO> questionDTOS = new ArrayList<>();
        question.forEach(
                question1 -> questionDTOS.add(converterService.convertQuestionToDTO(question1))
        );
        return questionDTOS;
    }

    @PostMapping("/candidate/upload")
    public List<String> uploadData(@RequestParam("file") MultipartFile file) {
        List<String> names = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();
            for (int i = 0; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    names.add(row.getCell(0).getStringCellValue());
                }
            }

            return names;
        } catch (IOException e) {
            throw new RuntimeException("File Not Found");
        }

    }

    @PostMapping("/question/assign")
    @Transactional
    public ResponseEntity<Map<String, String>> assignQuestions(
            @RequestBody CandidateDTO candidateDTO
    ) {
        try {
            List<Question> questions = new ArrayList<>();
            candidateDTO.getQuestions().forEach(
                    question -> {
                        questions.add(
                                questionRepository.findByTitle(question.getTitle())
                        );
                    }
            );
            candidateDTO.getCandidates().forEach(
                    candidate->{
                        Candidate candidate1 = new Candidate();
                        candidate1.setEmail(candidate);
                        candidate1.setQuestions(questions);
                        candidateRepository.save(candidate1);
                    }
            );
            Map<String, String> map = new HashMap<>();
            map.put("result", " Question Assigned successfully!");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("result", e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    @PostMapping("/run")
    public List<String> getResult(@RequestBody CodeDTO dto) throws IOException, InterruptedException {
        String BASE_PATH = "C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\ExtractedQuestions\\Question" + dto.getId() + "\\";

        if (dto.getLanguage().equals("Java"))
            return compilerService.compileAndrunJava(BASE_PATH, dto.getCode());
        if (dto.getLanguage().equals("Cpp"))
            return compilerService.compileAndrunCpp
                    (BASE_PATH, dto.getCode());
        if (dto.getLanguage().equals("Python"))
            return compilerService.runPython(BASE_PATH, dto.getCode());
        return null;
    }
    @PostMapping("/test")
    public List<QuestionDTO> startTest(@RequestParam String email){

        List<Question> question = candidateRepository.findByEmail(email).getQuestions();

        List<QuestionDTO> questionDTOS = new ArrayList<>();
        question.forEach(
                question1 -> questionDTOS.add(converterService.convertQuestionToDTO(question1))
        );
        return questionDTOS;
    }

}
