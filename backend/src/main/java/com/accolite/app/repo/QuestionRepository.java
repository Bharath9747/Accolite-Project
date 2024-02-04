package com.accolite.app.repo;

import com.accolite.app.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    Question findByTitle(String title);
}
