package vn.io.nguyen32.crm.customer.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import top.nguyennd.restsqlbackend.abstraction.dto.BaseResponse;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.FilterReqDto;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.IPagedListService;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.customer.CustomerContract;
import vn.io.nguyen32.crm.customer.ICustomerService;
import vn.io.nguyen32.crm.customer.dto.CustomerReqDTO;
import vn.io.nguyen32.crm.customer.dto.CustomerResDTO;

import java.net.URI;

@RestController
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomerController implements CustomerContract {

  ICustomerService customerService;

  @Override
  public ResponseEntity<BaseResponse<PagedModel<CustomerResDTO>>> getPagedList(Pageable pageable, FilterReqDto filter) {
    return doGetPagedList(pageable, filter);
  }

  @Override
  public ResponseEntity<BaseResponse<CustomerResDTO>> createCustomer(CustomerReqDTO reqDto) {
    CustomerResDTO result = customerService.createCustomer(reqDto);
    URI uri = URI.create("api/v1/user/%s".formatted(result.id()));
    return ResponseEntity.created(uri).body(BaseResponse.buildSuccess(result));
  }

  @Override
  public ResponseEntity<BaseResponse<CustomerResDTO>> getById(Long id) {
    return doGetById(id);
  }

  @Override
  public ResponseEntity<BaseResponse<CustomerResDTO>> updateCustomer(Long id, CustomerReqDTO reqDto) {
    CustomerResDTO result = customerService.updateCustomer(id, reqDto);
    return ResponseEntity.ok(BaseResponse.buildSuccess(result));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> deleteCustomer(Long id) {
    return doDeleteUser(id);
  }

  @Override
  public IPagedListService<CustomerResDTO> getService() {
    return customerService;
  }
}
