package com.accolite.app.service;

import com.accolite.app.dto.QuestionDTO;
import com.accolite.app.dto.TemplateDTO;
import com.accolite.app.entity.Question;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConverterService {
    String BASE_PATH="C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\ExtractedQuestions\\";

    public QuestionDTO convertQuestionToDTO(Long id) {
        QuestionDTO questionDTO = new QuestionDTO();
        String folderPath = BASE_PATH+"\\Question"+id+"\\";
        questionDTO.setDescription(readFile(folderPath+"readme.md"));
        File folder = new File(folderPath);
        List<TemplateDTO> templates = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {
            File[] subfolders = folder.listFiles(File::isDirectory);

            if (subfolders != null) {
                for (File subfolder : subfolders) {
//                    if(subfolder.getName().equals("Python"))
//                        continue;
                    TemplateDTO dto = new TemplateDTO();
                    dto.setLanguage(subfolder.getName());

                    if(subfolder.getName().equals("Java"))
                        dto.setCode(readFile(folderPath+"\\Java\\Solution.java"));
                    if(subfolder.getName().equals("Cpp"))
                        dto.setCode(readFile(folderPath+"\\Cpp\\Solution.cpp"));
                    if(subfolder.getName().equals("Python"))
                        dto.setCode(readFile(folderPath+"\\Python\\Solution.py"));
                    templates.add(dto);
                }
            }
        }
        questionDTO.setTemplates(templates);
        return questionDTO;
    }
    private String readFile(String filepath){
        StringBuilder content=new StringBuilder();
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
}
