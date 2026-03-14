package vn.io.nguyen32.crm.auth.impl;

import org.springframework.stereotype.Service;
import vn.io.nguyen32.crm.auth.IRefreshTokenService;
import vn.io.nguyen32.crm.auth.dto.LogInResDto;

@Service
public class RefreshTokenResponseService implements IRefreshTokenService<LogInResDto.LogInResDtoBuilder> {
  @Override
  public void addRefreshToken(LogInResDto.LogInResDtoBuilder response, String refreshToken) {
    response.refreshToken(refreshToken);
  }

  @Override
  public void clearRefreshToken(LogInResDto.LogInResDtoBuilder response) {
    // do nothing
  }
}
