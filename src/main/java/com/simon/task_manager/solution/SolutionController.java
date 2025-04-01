package com.simon.task_manager.solution;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/solutions")
public class SolutionController {

    private final SolutionRepository solutionRepository;
    private static final Logger log = LoggerFactory.getLogger(SolutionController.class);

    public SolutionController(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
    }

    @GetMapping
    List<Solution> findAll() {
        return solutionRepository.findAll();
    }


    @GetMapping("/{id}")
    Solution findById(@PathVariable Integer id) {
        Optional<Solution> solution = solutionRepository.findById(id);
        if (solution.isPresent()){
            return solution.get();
        }else{
            throw new SolutionNotFoundException();
        }
    }
    @GetMapping("/{id}/images")
    List<String> findImagesSol(@PathVariable Integer id) {
        return solutionRepository.findImagesSol(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Integer create(@RequestBody Solution solution) {
        return solutionRepository.create(solution);
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

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable Integer id, @RequestParam("image") MultipartFile file) {
        try {
            log.info("Uploading image for solution with id: " + id);
            log.info("File name: " + file.getOriginalFilename());
            solutionRepository.addImage(file, id);
            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (IOException e) {
            log.error("Failed to upload image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }
}
