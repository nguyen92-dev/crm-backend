package vn.io.nguyen32.crm.customer;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Quan ly khach hang", description = "Quan ly khach hang")
@RequestMapping("api/v1/customer")
@Validated
public interface CustomerContract {

}
