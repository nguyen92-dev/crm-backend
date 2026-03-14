package vn.io.nguyen32.crm.auth.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import vn.io.nguyen32.crm.auth.IRefreshTokenService;

import java.time.Duration;

@Service
@Primary
public class RefreshTokenCookieService implements IRefreshTokenService<HttpServletResponse> {

  @Value("${jwt.refresh-token.cookie.name}")
  String cookieName;

  @Value("${jwt.refresh-token.cookie.secure}")
  boolean secure;

  @Value("${jwt.refreshable-duration}")
  Long duration;

  @Override
  public void addRefreshToken(HttpServletResponse response, String refreshToken) {
    ResponseCookie cookie = ResponseCookie.from(cookieName, refreshToken)
        .httpOnly(true)
        .secure(secure)
        .path("/api/auth/refresh")
        .maxAge(Duration.ofDays(duration))
        .sameSite("None")
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  @Override
  public void clearRefreshToken(HttpServletResponse response) {
    ResponseCookie cookie = ResponseCookie.from(cookieName, "")
        .httpOnly(true)
        .secure(secure)
        .path("/api/auth/refresh")
        .maxAge(Duration.ZERO)
        .sameSite("None")
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }
}
