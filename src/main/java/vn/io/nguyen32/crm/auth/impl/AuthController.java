package vn.io.nguyen32.crm.auth.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import top.nguyennd.restsqlbackend.abstraction.dto.BaseResponse;
import vn.io.nguyen32.crm.auth.IAuthContract;
import vn.io.nguyen32.crm.auth.IAuthService;
import vn.io.nguyen32.crm.auth.dto.LogInReqDto;
import vn.io.nguyen32.crm.auth.dto.LogInResDto;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController implements IAuthContract {

  IAuthService authService;

  @Override
  public ResponseEntity<BaseResponse<LogInResDto>> login(LogInReqDto reqDto) {
    LogInResDto loginRes = authService.login(reqDto);
    return ResponseEntity.ok(BaseResponse.buildSuccess(loginRes));
  }
}
