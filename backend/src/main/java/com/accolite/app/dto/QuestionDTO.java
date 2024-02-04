package com.accolite.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private String title;
    private List<ExplorerItem> paths;
    private String type;
}
