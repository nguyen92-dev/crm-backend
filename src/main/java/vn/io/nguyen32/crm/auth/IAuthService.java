package vn.io.nguyen32.crm.auth;

import vn.io.nguyen32.crm.auth.dto.LogInReqDto;
import vn.io.nguyen32.crm.auth.dto.LogInResDto;

public interface IAuthService {
  LogInResDto login(LogInReqDto reqDto);
}
