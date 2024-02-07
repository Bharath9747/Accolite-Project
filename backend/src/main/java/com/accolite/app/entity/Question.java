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
    private byte[] zip;
    @Column(unique = true)
    private String name;
    private String type;
    @ElementCollection
    @CollectionTable(name = "question_test", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "test_id")
    private List<Long> tests;
}
