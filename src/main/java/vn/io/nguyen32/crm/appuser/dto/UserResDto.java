package vn.io.nguyen32.crm.appuser.dto;

import java.time.LocalDateTime;

public record UserResDto(
    Long id,
    String username,
    String email,
    String fullName,

    RoleResDto role,

    String createdBy,
    LocalDateTime createdAt,
    String updatedBy,
    LocalDateTime updatedAt
) {
}
