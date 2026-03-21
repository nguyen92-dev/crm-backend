package vn.io.nguyen32.crm.appuser.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import top.nguyennd.restsqlbackend.abstraction.common.ErrorStatus;
import top.nguyennd.restsqlbackend.abstraction.dto.BaseResponse;
import top.nguyennd.restsqlbackend.abstraction.exception.BusinessException;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.FilterReqDto;
import vn.io.nguyen32.crm.appuser.IAppUserContract;
import vn.io.nguyen32.crm.appuser.IAppUserService;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserReqDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserUpdateReqDto;

import java.net.URI;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AppUserController implements IAppUserContract {

  IAppUserService appUserService;

  @Override
  public ResponseEntity<BaseResponse<PagedModel<AppUserBaseResDto>>> getPagedList(Pageable pageable, FilterReqDto filter) {
    PagedModel<AppUserBaseResDto> pagedModel = new PagedModel<>(appUserService.getPagedList(filter, pageable, true));
    return ResponseEntity.ok(BaseResponse.buildSuccess(pagedModel));
  }

  @Override
  public ResponseEntity<BaseResponse<AppUserBaseResDto>> createUser(AppUserReqDto reqDto) {
    AppUserBaseResDto result = appUserService.createUser(reqDto);
    URI uri = URI.create("api/v1/user/%s".formatted(result.id()));
    return ResponseEntity.created(uri).body(BaseResponse.buildSuccess(result));
  }

  @Override
  public ResponseEntity<BaseResponse<AppUserBaseResDto>> getById(Integer id) {
    AppUserBaseResDto result = appUserService.findUserById(id.longValue());
    return ResponseEntity.ok(BaseResponse.buildSuccess(result));
  }

  @Override
  public ResponseEntity<BaseResponse<AppUserBaseResDto>> updateUser(Long id, AppUserUpdateReqDto reqDto) {
    AppUserBaseResDto result = appUserService.updateUser(id, reqDto);
    return ResponseEntity.ok(BaseResponse.buildSuccess(result));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> deleteUser(Long id) {
    appUserService.deleteById(id);
    return ResponseEntity.ok(BaseResponse.buildSuccess());
  }
}
