package com.simon.task_manager.task;

import java.time.LocalDateTime;
public record Task(
    Integer id,
    String subject,
    String section,
    String description,
    String creator_username,
    LocalDateTime dateAdded,
    String images
) {

}
