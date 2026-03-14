package vn.io.nguyen32.crm.auth.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import top.nguyennd.restsqlbackend.abstraction.utils.Validator;
import vn.io.nguyen32.crm.auth.IRefreshTokenService;
import vn.io.nguyen32.crm.auth.IRefreshTokenServiceProxy;
import vn.io.nguyen32.crm.auth.dto.LogInResDto;

import java.util.HashMap;
import java.util.Map;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RefreshTokenServiceProxyImpl implements IRefreshTokenServiceProxy {

  IRefreshTokenService<HttpServletResponse> refreshTokenCookieService;
  IRefreshTokenService<LogInResDto.LogInResDtoBuilder> refreshTokenResponseService;

  @Override
  public void addRefreshToken(Object response, String refreshToken) {
    switch (response) {
      case HttpServletResponse servletResponse -> refreshTokenCookieService.addRefreshToken(servletResponse, refreshToken);
      case LogInResDto.LogInResDtoBuilder dtoBuilder -> refreshTokenResponseService.addRefreshToken(dtoBuilder, refreshToken);
      default -> throw new IllegalArgumentException("Invalid response type");
    }
  }

  @Override
  public void clearRefreshToken(Object response) {
    switch (response) {
      case HttpServletResponse servletResponse -> refreshTokenCookieService.clearRefreshToken(servletResponse);
      case LogInResDto.LogInResDtoBuilder dtoBuilder -> refreshTokenResponseService.clearRefreshToken(dtoBuilder);
      default -> throw new IllegalArgumentException("Invalid response type");
    }
  }
}
