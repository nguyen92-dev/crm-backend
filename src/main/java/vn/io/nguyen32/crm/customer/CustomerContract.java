package vn.io.nguyen32.crm.customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import top.nguyennd.restsqlbackend.abstraction.controller.IPagedListController;
import top.nguyennd.restsqlbackend.abstraction.dto.BaseResponse;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.FilterReqDto;
import vn.io.nguyen32.crm.customer.dto.CustomerReqDTO;
import vn.io.nguyen32.crm.customer.dto.CustomerResDTO;

@Tag(name = "Quan ly khach hang", description = "Quan ly khach hang")
@RequestMapping("api/v1/customer")
@Validated
public interface CustomerContract extends IPagedListController<CustomerResDTO> {
  @Operation(summary = "Danh sách khách hàng", description = "Danh sách khách hàng")
  @PostMapping("/paged-list")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  ResponseEntity<BaseResponse<PagedModel<CustomerResDTO>>> getPagedList(Pageable pageable,
                                                                           FilterReqDto filter);

  @Operation(summary = "Thêm khách", description = "Thêm khách")
  @PostMapping()
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  ResponseEntity<BaseResponse<CustomerResDTO>> createCustomer(@Valid @RequestBody CustomerReqDTO reqDto);

  @Operation(summary = "Chi tiet khách ", description = "Chi tiet khách ")
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  ResponseEntity<BaseResponse<CustomerResDTO>> getById(@PathVariable Long id);

  @Operation(summary = "Cap nhat khách ", description = "Cap nhat khách ")
  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  ResponseEntity<BaseResponse<CustomerResDTO>> updateCustomer(@PathVariable Long id,
                                                              @RequestBody @Valid CustomerReqDTO reqDto);

  @Operation(summary = "Xoa khách ", description = "Xoa khách ")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<BaseResponse<Void>> deleteCustomer(@PathVariable Long id);
}
