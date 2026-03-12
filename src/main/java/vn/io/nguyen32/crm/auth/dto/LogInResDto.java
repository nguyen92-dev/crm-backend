package vn.io.nguyen32.crm.auth.dto;

import lombok.Builder;
import vn.io.nguyen32.crm.common.AppRoles;

@Builder
public record LogInResDto(
    String accessToken,
    String refreshToken,
    String username,
    String fullName,
    AppRoles role
) {
}
