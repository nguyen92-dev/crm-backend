package vn.io.nguyen32.crm.appuser;

import top.nguyennd.restsqlbackend.abstraction.pagedlist.IPagedListService;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserReqDto;

public interface IAppUserService extends IPagedListService<AppUserBaseResDto, AppUserReqDto> {
  AppUserBaseResDto createUser(AppUserReqDto reqDto);
}
