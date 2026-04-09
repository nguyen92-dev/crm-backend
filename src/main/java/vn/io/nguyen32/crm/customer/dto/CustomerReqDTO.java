package vn.io.nguyen32.crm.customer.dto;

public record CustomerReqDTO(
    String name,
    String phone,
    String email,
    String facebook,
    String address
) {
}
