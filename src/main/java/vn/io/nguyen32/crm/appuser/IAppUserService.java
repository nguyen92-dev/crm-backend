package vn.io.nguyen32.crm.appuser;

import jakarta.transaction.Transactional;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.IPagedListService;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserReqDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserUpdateReqDto;

import java.util.Optional;

public interface IAppUserService extends IPagedListService<AppUserBaseResDto, AppUserReqDto> {
  @Transactional
  AppUserBaseResDto createUser(AppUserReqDto reqDto);

  @Transactional
  AppUserBaseResDto updateUser(Long id, AppUserUpdateReqDto reqDto);

  AppUserBaseResDto findUserById(long id);
}
