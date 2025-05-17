package com.simon.task_manager.task;

import com.simon.task_manager.solution.SolutionRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Test
    void shouldReturnTaskById_whenExists() {

        TaskRepository taskRepository = mock(TaskRepository.class);
        SolutionRepository solutionRepository = mock(SolutionRepository.class);
        TaskService taskService = new TaskService(solutionRepository, taskRepository);

        Task task = new Task(1,
                "MATH",
                "ADDITION",
                "What is 2+2?",
                "Igor Simon",
                LocalDateTime.now(),
                "");
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.findById(1);

        assertTrue(result.isPresent());
        assertEquals("MATH", result.get().subject());
    }

    @Test
    void shouldCallDelete_whenDeletingTask() {

        TaskRepository taskRepository = mock(TaskRepository.class);
        SolutionRepository solutionRepository = mock(SolutionRepository.class);
        TaskService taskService = new TaskService(solutionRepository, taskRepository);

        Task task = new Task(1,
                "MATH",
                "ADDITION",
                "What is 2+2?",
                "Igor Simon",
                LocalDateTime.now(),
                "");

        when(taskRepository.findImageUrls(17)).thenReturn(List.of());
        when(solutionRepository.findAllForTask(17)).thenReturn(List.of());
        when(taskRepository.deleteTaskById(17)).thenReturn(1);

        taskService.deleteTask(17);

        //verify(solutionRepository).deleteAllForTask(1);
        verify(taskRepository).deleteImagesByTaskId(17);
        verify(taskRepository).deleteTaskById(17);
    }

    @Test
    void shouldCreateTaskAndReturnId() {

        TaskRepository taskRepository = mock(TaskRepository.class);
        SolutionRepository solutionRepository = mock(SolutionRepository.class);
        TaskService taskService = new TaskService(solutionRepository, taskRepository);

        Task task = new Task(1,
                "MATH",
                "ADDITION",
                "What is 2+2?",
                "Igor Simon",
                LocalDateTime.now(),
                "");

        when(taskRepository.create(any())).thenReturn(1);

        int resultId = taskService.create(task);

        assertEquals(1, resultId);
    }
}
