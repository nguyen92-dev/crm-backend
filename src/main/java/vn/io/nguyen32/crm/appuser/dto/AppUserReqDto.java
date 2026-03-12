package vn.io.nguyen32.crm.appuser.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AppUserReqDto(
    @NotNull(message = "username la bat buoc")
    @NotEmpty(message = "username la bat buoc")
    String username,
    @NotNull(message = "password la bat buoc")
    @NotEmpty(message = "password la bat buoc")
    String password,
    String email,
    String fullName
) {
}
