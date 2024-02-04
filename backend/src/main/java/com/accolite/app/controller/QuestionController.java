package com.accolite.app.controller;

import com.accolite.app.dto.CodeDTO;
import com.accolite.app.dto.QuestionDTO;
import com.accolite.app.dto.TemplateDTO;
import com.accolite.app.entity.Question;
import com.accolite.app.repo.QuestionRepository;
import com.accolite.app.service.CompilerService;
import com.accolite.app.service.ConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:4200/")
@RequiredArgsConstructor
public class QuestionController {
    private final ConverterService converterService;
    private final CompilerService compilerService;

    @Autowired
    QuestionRepository questionRepository;

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

    @PostMapping("/run")
    public List<String> getResult(@RequestBody CodeDTO dto) throws IOException, InterruptedException {
        String BASE_PATH="C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\ExtractedQuestions\\Question"+dto.getId()+"\\";

        if(dto.getLanguage().equals("Java"))
            return compilerService.compileAndrunJava(BASE_PATH,dto.getCode());
        if(dto.getLanguage().equals("Cpp"))
            return compilerService.compileAndrunCpp
                    (BASE_PATH,dto.getCode());
        if(dto.getLanguage().equals("Python"))
           return  compilerService.runPython(BASE_PATH,dto.getCode());
        return null;
    }

}
