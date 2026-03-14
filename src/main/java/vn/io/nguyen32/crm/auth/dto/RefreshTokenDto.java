package vn.io.nguyen32.crm.auth.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record RefreshTokenDto(
    UUID id,
    String username,
    UUID sessionId,
    String refreshToken,
    LocalDateTime expiredAt,
    LocalDateTime revokedAt,
    String replacerBy
) {
}
