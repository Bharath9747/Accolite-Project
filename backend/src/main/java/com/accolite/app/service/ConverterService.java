package com.accolite.app.service;

import com.accolite.app.dto.ExplorerItem;
import com.accolite.app.dto.QuestionDTO;
import com.accolite.app.entity.Question;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ConverterService {
    String BASE_PATH = "C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\ExtractedQuestions\\Question";

    public QuestionDTO convertQuestionToDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        String folderPath = BASE_PATH + question.getId();

        questionDTO.setTitle(question.getTitle());
        questionDTO.setType(question.getType());
        questionDTO.setPaths(fetchDataFromDirectory(new File(folderPath)));

        return questionDTO;
    }

    public String readFile(String filepath) {
        StringBuilder content = new StringBuilder();
        try (FileReader reader = new FileReader(filepath)) {
            int character;

            while ((character = reader.read()) != -1) {
                content.append((char) character);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new String(content);
    }

    public List<ExplorerItem> fetchDataFromDirectory(File directory) {
        File[] files = directory.listFiles();

        if (files == null) {
            return null;
        }

        return Arrays.stream(files)
                .map(file -> {
                    if (file.isDirectory()) {
                        List<ExplorerItem> subItems = fetchDataFromDirectory(file);
                        return new ExplorerItem(file.getName(), "folder", file.getAbsolutePath(), subItems);
                    } else {
                        if ((file.getName().equals("README.md") ||
                                file.getName().equals("Solution.java") ||
                                file.getName().equals("Solution.cpp") ||
                                file.getName().equals("Solution.py") ||
                                file.getName().equals("Solution.sql"))) {
                            return new ExplorerItem(file.getName(), "file", file.getAbsolutePath());
                        } else {
                            return null; // or return Collections.emptyList();
                        }
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }
}
