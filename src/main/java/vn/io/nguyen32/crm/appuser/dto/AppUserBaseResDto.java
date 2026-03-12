package vn.io.nguyen32.crm.appuser.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import vn.io.nguyen32.crm.common.AppRoles;
import vn.io.nguyen32.crm.common.dto.AuditResDto;

public record AppUserBaseResDto(
    Integer id,
    String username,
    String email,
    String fullName,
    AppRoles roleName,
    @JsonUnwrapped
    AuditResDto audit
) {
}
