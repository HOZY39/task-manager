package com.simon.task_manager.solution;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/solutions")
public class SolutionController {

    private final SolutionRepository solutionRepository;

    public SolutionController(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
    }

    @GetMapping
    List<Solution> findAll() {
        return solutionRepository.findAll();
    }


    @GetMapping("/{id}")
    Solution findById(@PathVariable Integer id) {
        Optional<Solution> task = solutionRepository.findById(id);
        if (task.isPresent()){
            return task.get();
        }else{
            throw new SolutionNotFoundException();
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@RequestBody Solution solution){
        solutionRepository.create(solution);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@RequestBody Solution solution, @PathVariable Integer id){
        solutionRepository.update(solution, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id){
        solutionRepository.delete(id);
    }
}
