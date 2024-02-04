package com.accolite.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "BLOB")
    private byte[] compressedData;
    @Column(unique = true)
    private String title;

    private String type;
    @ManyToMany(mappedBy = "questions")
    List<Candidate> candidates = new ArrayList<>();
}
