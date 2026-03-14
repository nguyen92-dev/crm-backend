package vn.io.nguyen32.crm.auth.impl;

import jakarta.servlet.http.HttpServletResponse;
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
  public ResponseEntity<BaseResponse<LogInResDto>> login(HttpServletResponse response,
                                                         LogInReqDto reqDto) {
    LogInResDto loginRes = authService.login(response, reqDto);
    return ResponseEntity.ok(BaseResponse.buildSuccess(loginRes));
  }

  @Override
  public ResponseEntity<BaseResponse<LogInResDto>> refresh(String refreshToken, HttpServletResponse response) {
    LogInResDto loginRes = authService.refresh(response, refreshToken);
    return ResponseEntity.ok(BaseResponse.buildSuccess(loginRes));
  }

  @Override
  public ResponseEntity<BaseResponse<Void>> logOut(String refreshToken, HttpServletResponse response) {
    authService.logout(response, refreshToken);
    return ResponseEntity.ok(BaseResponse.buildSuccess());
  }
}
