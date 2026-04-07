package vn.io.nguyen32.crm.customer;

import top.nguyennd.restsqlbackend.abstraction.pagedlist.IPagedListService;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserReqDto;

public interface ICustomerService extends IPagedListService<AppUserBaseResDto> {
}
