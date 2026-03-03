package vn.io.nguyen32.crm.appuser;

import top.nguyennd.restsqlbackend.abstraction.pagedlist.IPagedListService;
import vn.io.nguyen32.crm.appuser.dto.UserReqDto;
import vn.io.nguyen32.crm.appuser.dto.UserResDto;

public interface IAppUserService extends IPagedListService<UserResDto, UserReqDto> {
  UserResDto addAppUser(UserReqDto user);
}
