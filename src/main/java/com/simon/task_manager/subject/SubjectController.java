package com.simon.task_manager.subject;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject")
@CrossOrigin(origins = "http://localhost:4200") // Jeśli Angular będzie na porcie 4200
public class SubjectController {


    private final SubjectRepository subjectRepository;
    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @GetMapping
    List<Subject> findAll() {
        return subjectRepository.findAll();
    }
}
