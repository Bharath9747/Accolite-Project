package com.accolite.app.service;

import com.accolite.app.dto.ExplorerItem;
import com.accolite.app.dto.QuestionDTO;
import com.accolite.app.entity.Question;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConverterService {
    String BASE_PATH = "C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\";

    public QuestionDTO convertQuestionToDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        String folderPath = BASE_PATH +"ExtractedQuestions\\"+question.getType()+"\\"+question.getTitle()+"\\";
        questionDTO.setId(question.getId());
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

                        return new ExplorerItem(file.getName(), "file", file.getAbsolutePath());
                    }
                })

                .collect(Collectors.toList());

    }
}
