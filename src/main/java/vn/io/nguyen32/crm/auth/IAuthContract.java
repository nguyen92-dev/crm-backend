package vn.io.nguyen32.crm.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import top.nguyennd.restsqlbackend.abstraction.dto.BaseResponse;
import vn.io.nguyen32.crm.auth.dto.LogInReqDto;
import vn.io.nguyen32.crm.auth.dto.LogInResDto;

@RequestMapping("api/auth")
@Validated
@Tag(name = "Api dang nhap", description = "Api dang nhap")
public interface IAuthContract {

  @PostMapping("/login")
  @Operation(summary = "Dang nhap", description = "Dang nhap")
  ResponseEntity<BaseResponse<LogInResDto>> login(HttpServletResponse response,
                                                  @RequestBody @Valid LogInReqDto reqDto);

  @PostMapping("/refresh")
  @Operation(summary = "refresh token", description = "refresh token")
  ResponseEntity<BaseResponse<LogInResDto>> refresh(@CookieValue("refreshToken") String refreshToken,
                                                    HttpServletResponse response);

  @PostMapping("/logout")
  @Operation(summary = "logout", description = "logout")
  ResponseEntity<BaseResponse<Void>> logOut(@CookieValue("refreshToken") String refreshToken,
                                            HttpServletResponse response);
}
