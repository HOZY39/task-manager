package com.simon.task_manager.solution;

import java.time.LocalDateTime;

public record Solution(
    Integer id,
    Integer task_id,
    String solution,
    Integer creator_id,
    LocalDateTime date_added
) {
}
