package vn.io.nguyen32.crm.appuser.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import top.nguyennd.restsqlbackend.abstraction.common.dto.AuditResDto;
import vn.io.nguyen32.crm.common.AppRoles;

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
