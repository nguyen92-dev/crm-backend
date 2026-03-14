package vn.io.nguyen32.crm.auth;

import jakarta.servlet.http.HttpServletResponse;
import vn.io.nguyen32.crm.auth.dto.LogInReqDto;
import vn.io.nguyen32.crm.auth.dto.LogInResDto;

public interface IAuthService {
  LogInResDto login(HttpServletResponse response, LogInReqDto reqDto);

  LogInResDto refresh(HttpServletResponse response, String refreshToken);
}
