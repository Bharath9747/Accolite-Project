package com.accolite.app.controller;

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
        return converterService.convertQuestionToDTO(id);
    }

    @PostMapping("/run")
    public List<String> getResult(@RequestBody TemplateDTO dto) throws IOException, InterruptedException {
        if(dto.getLanguage().equals("Java"))
            return compilerService.compileAndrunJava(dto.getCode());
        if(dto.getLanguage().equals("Cpp"))
            return compilerService.compileAndrunCpp
                    (dto.getCode());
        if(dto.getLanguage().equals("Python"))
           return  compilerService.runPython(dto.getCode());
        return null;
    }

}
