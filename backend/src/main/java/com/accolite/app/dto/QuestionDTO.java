package com.accolite.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private String description;
    private List<TemplateDTO> templates;
}
