package vn.io.nguyen32.crm.customer;

import top.nguyennd.restsqlbackend.abstraction.pagedlist.IPagedListService;
import vn.io.nguyen32.crm.customer.dto.CustomerReqDTO;
import vn.io.nguyen32.crm.customer.dto.CustomerResDTO;

public interface ICustomerService extends IPagedListService<CustomerResDTO> {
  CustomerResDTO createCustomer(CustomerReqDTO reqDto);

  CustomerResDTO updateCustomer(Long id, CustomerReqDTO reqDto);
}
