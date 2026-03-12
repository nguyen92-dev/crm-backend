package vn.io.nguyen32.crm.common.dto;

import java.time.LocalDateTime;

public record AuditResDto (
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy
) {
}
