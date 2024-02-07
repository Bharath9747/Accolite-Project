package com.accolite.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    @CollectionTable(name = "test_question", joinColumns = @JoinColumn(name = "test_id"))
    @Column(name = "question_id")
    private List<Long> questions;
    @ElementCollection
    @CollectionTable(name = "test_candidate", joinColumns = @JoinColumn(name = "test_id"))
    @Column(name = "candidate_id")
    private List<Long> candidates;

}
