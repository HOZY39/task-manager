package com.simon.task_manager.task;

import java.time.LocalDateTime;
public record Task(
    Integer id,
    subject subject,
    String section,
    String description,
    Integer creator_id,
    LocalDateTime dateAdded
) {

}
