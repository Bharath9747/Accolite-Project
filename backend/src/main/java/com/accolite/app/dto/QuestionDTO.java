package com.accolite.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data

public class QuestionDTO {
    private Long id;
    private String title;
    private List<ExplorerItem> paths;
    private String type;
}
