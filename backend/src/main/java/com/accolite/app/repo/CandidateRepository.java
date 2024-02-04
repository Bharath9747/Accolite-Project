package com.accolite.app.repo;

import com.accolite.app.entity.Candidate;
import com.accolite.app.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate,Long> {
    Candidate findByEmail(String email);
}
