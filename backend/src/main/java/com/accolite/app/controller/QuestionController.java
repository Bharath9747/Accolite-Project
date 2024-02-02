package com.accolite.app.controller;

import com.accolite.app.dto.QuestionDTO;
import com.accolite.app.entity.Question;
import com.accolite.app.repo.QuestionRepository;
import com.accolite.app.service.ConverterService;
import com.accolite.app.service.ExtracterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:4200/")
@RequiredArgsConstructor
public class QuestionController {
    private final ConverterService converterService;

    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("/question")
    public QuestionDTO getQuestion(@RequestParam Long id){

        Question question = questionRepository.findById(id).orElse(null);
        if(question==null)
            return null;

        return converterService.convertQuestionToDTO(id);
    }

}
