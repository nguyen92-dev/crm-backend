package vn.io.nguyen32.crm.appuser.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import top.nguyennd.restsqlbackend.abstraction.dto.BaseResponse;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.FilterReqDto;
import vn.io.nguyen32.crm.appuser.AppUserContract;
import vn.io.nguyen32.crm.appuser.IAppUserService;
import vn.io.nguyen32.crm.appuser.dto.UserReqDto;
import vn.io.nguyen32.crm.appuser.dto.UserResDto;


@RestController
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AppUserController implements AppUserContract {

  IAppUserService appUserService;

  @Override
  public ResponseEntity<BaseResponse<PagedModel<UserResDto>>> getPageList(FilterReqDto filter,
                                                                          Pageable pageable,
                                                                          Boolean isPaged) {
    var result = new PagedModel<>(appUserService.getPagedList(filter, pageable, isPaged));
    return ResponseEntity.ok(BaseResponse.buildSuccess(result));
  }

  @Override
  public ResponseEntity<BaseResponse<UserResDto>> createUser(UserReqDto reqDto) {
    UserResDto result = appUserService.addAppUser(reqDto);
    var response = BaseResponse.buildSuccess(result);
    return ResponseEntity.ok(response);
  }
}
