package com.simon.task_manager.task;

import java.time.LocalDateTime;

public record Solution(
    Integer id,
    Integer task_id,
    String solution,
    LocalDateTime date_added
) {
}
