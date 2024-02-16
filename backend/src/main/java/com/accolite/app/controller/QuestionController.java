package com.accolite.app.controller;

import com.accolite.app.dto.CandidateDTO;
import com.accolite.app.dto.CodeDTO;
import com.accolite.app.dto.QuestionDTO;
import com.accolite.app.entity.Candidate;
import com.accolite.app.entity.Question;
import com.accolite.app.entity.Test;
import com.accolite.app.repo.CandidateRepository;
import com.accolite.app.repo.QuestionRepository;
import com.accolite.app.repo.TestRepository;
import com.accolite.app.service.CompilerService;
import com.accolite.app.service.ConverterService;
import com.accolite.app.service.ExtracterService;
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
    private final ExtracterService extracterService;

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    TestRepository testRepository;

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
                                questionRepository.findByName(question.getTitle())
                        );
                    }
            );
            candidateDTO.getCandidates().forEach(
                    candidate -> {

                        Candidate candidate1 = new Candidate();
                        candidate1.setEmail(candidate);
//                        candidate1.setQuestions(questions);
                        candidate1 = candidateRepository.save(candidate1);
                        Long id = candidate1.getId();
                        questions.forEach(
                                question -> {
                                    try {
                                        extracterService.extractZipForCandidate(id, question);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );

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
        Question question = questionRepository.findById(dto.getId()).orElse(null);
        if (question == null)
            return null;
        String type = question.getType();
        String title = question.getName();

        String BASE_PATH = "C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\ExtractedQuestions\\" + type + "\\" + title;

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
    public List<QuestionDTO> startTest(@RequestParam String email) {

//        List<Question> question = candidateRepository.findByEmail(email).getQuestions();

        List<QuestionDTO> questionDTOS = new ArrayList<>();
//        question.forEach(
//                question1 -> questionDTOS.add(converterService.convertQuestionToDTO(question1))
//        );
        return questionDTOS;
    }

    @PostMapping("/create/test")
    @Transactional
    public ResponseEntity<Map<String, String>> createTest(
            @RequestBody List<QuestionDTO> questions
    ) {
        try {
            Test test = new Test();
            List<Question> list = new ArrayList<>();
            List<Long> ids = new ArrayList<>();
            questions.forEach(
                    questionDTO -> {
                        Question question = questionRepository.findByName(questionDTO.getTitle());

                        ids.add(question.getId());
                        list.add(question);
                    }
            );

            test.setQuestions(ids);
            test = testRepository.save(test);
            Test finalTest = test;
            list.forEach(
                    question -> {
                        if (question.getTests() == null) {
                            List<Long> temp = new ArrayList<>();
                            temp.add(finalTest.getId());
                            question.setTests(temp);
                            questionRepository.save(question);
                        } else {
                            List<Long> temp = question.getTests();
                            temp.add(finalTest.getId());
                            question.setTests(temp);
                            questionRepository.save(question);
                        }
                    }
            );
            Map<String, String> map = new HashMap<>();
            map.put("result", " Test Created successfully!");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("result", e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

}
