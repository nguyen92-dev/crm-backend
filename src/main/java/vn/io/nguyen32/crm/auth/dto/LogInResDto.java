package vn.io.nguyen32.crm.auth.dto;

import lombok.Builder;

@Builder
public record LogInResDto(
    String accessToken,
    String refreshToken,
    String username,
    String fullName,
    String role
) {
}
