package com.simon.task_manager.subject;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject")
@CrossOrigin(origins = "*")
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
