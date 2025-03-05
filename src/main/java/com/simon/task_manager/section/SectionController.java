package com.simon.task_manager.section;

import org.springframework.web.bind.annotation.*;

import com.simon.task_manager.solution.Solution;

import java.util.List;

@RestController
@RequestMapping("/api/section")
@CrossOrigin(origins = "http://localhost:4200") // Jeśli Angular będzie na porcie 4200
public class SectionController {


    private final SectionRepository sectionRepository;
    public SectionController(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @GetMapping
    List<Section> findAll() {
        return sectionRepository.findAll();
    }

    @GetMapping("/{subject}")
    List<Section> findAllSectionsBySubject(@PathVariable String subject) {
        return sectionRepository.findBySubject(subject);
    }
}
