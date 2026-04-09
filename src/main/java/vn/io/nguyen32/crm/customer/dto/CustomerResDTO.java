package vn.io.nguyen32.crm.customer.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import top.nguyennd.restsqlbackend.abstraction.common.dto.AuditResDto;

public record CustomerResDTO(
    Long id,
    String name,
    String phone,
    String email,
    String facebook,
    String address,
    @JsonUnwrapped
    AuditResDto audit
) {
}
