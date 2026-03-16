package vn.io.nguyen32.crm.appuser.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import vn.io.nguyen32.crm.common.AppRoles;
import vn.io.nguyen32.crm.common.dto.AuditResDto;

@Builder
public record AppUserBaseResDto(
    Long id,
    String username,
    String email,
    String fullName,
    AppRoles roleName,
    @JsonUnwrapped
    AuditResDto audit
) {
}
