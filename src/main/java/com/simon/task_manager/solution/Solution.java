package com.simon.task_manager.solution;

import java.time.LocalDateTime;

public record Solution(
    Integer id,
    Integer task_id,
    String solution,
    String creator_username,
    LocalDateTime date_added
) {
}
