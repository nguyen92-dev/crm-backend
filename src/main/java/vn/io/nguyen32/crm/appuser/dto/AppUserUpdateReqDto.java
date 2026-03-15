package vn.io.nguyen32.crm.appuser.dto;

import vn.io.nguyen32.crm.common.AppRoles;

public record AppUserUpdateReqDto(
    String email,
    String fullName,
    AppRoles roleName
) {
}
