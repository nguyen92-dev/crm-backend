package vn.io.nguyen32.crm.auth.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import top.nguyennd.restsqlbackend.abstraction.common.ErrorStatus;
import top.nguyennd.restsqlbackend.abstraction.exception.BusinessException;
import vn.io.nguyen32.crm.appuser.entity.AppUser;
import vn.io.nguyen32.crm.appuser.AppUserRepository;
import vn.io.nguyen32.crm.auth.IAuthService;
import vn.io.nguyen32.crm.auth.IRefreshTokenServiceProxy;
import vn.io.nguyen32.crm.auth.dto.LogInReqDto;
import vn.io.nguyen32.crm.auth.dto.LogInResDto;
import vn.io.nguyen32.crm.auth.dto.RefreshTokenDto;
import vn.io.nguyen32.crm.configuration.jwt.JwtCustomDecoder;
import vn.io.nguyen32.crm.rediscache.IRedisCacheService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static top.nguyennd.restsqlbackend.abstraction.exception.BusinessException.badRequest;
import static vn.io.nguyen32.crm.common.AppConstant.LOGIN_FAIL_MSG;
import static vn.io.nguyen32.crm.rediscache.CommonKey.REFRESH_TOKEN;
import static vn.io.nguyen32.crm.utils.JwtUtils.generateAccessToken;
import static vn.io.nguyen32.crm.utils.JwtUtils.generateRefreshToken;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

  PasswordEncoder passwordEncoder;
  AppUserRepository userRepository;
  IRedisCacheService cacheService;
  IRefreshTokenServiceProxy refreshTokenServiceProxy;
  JwtCustomDecoder decoder;

  @NonFinal
  @Value("${jwt.secret}")
  protected String signerKey;

  @NonFinal
  @Value("${jwt.valid-duration}")
  protected long validDuration;

  @NonFinal
  @Value("${jwt.refreshable-duration}")
  protected long refreshableDuration;

  @Override
  public LogInResDto login(HttpServletResponse response, LogInReqDto loginReq) {
    var user = userRepository.findByUsernameIgnoreCase(loginReq.username()).orElseThrow(
        () -> badRequest(LOGIN_FAIL_MSG));

    if (!passwordEncoder.matches(loginReq.password(), user.getPassword())) {
      throw badRequest(LOGIN_FAIL_MSG);
    }

    var sessionId = UUID.randomUUID();
    return buildLoginResponse(user, sessionId, response);
  }

  @Override
  public LogInResDto refresh(HttpServletResponse response, String refreshToken) {
    Jwt jwt = decoder.decode(refreshToken);
    var sessionId = UUID.fromString(jwt.getClaimAsString("sId"));
    String tokenKey = REFRESH_TOKEN.formatted(jwt.getSubject(), sessionId);
    if (!cacheService.isExist(tokenKey)) {
      throw new BusinessException(ErrorStatus.UNAUTHORIZED,"Invalid refresh token");
    }
    var user = userRepository.findByUsernameIgnoreCase(jwt.getSubject()).orElseThrow(
        () -> badRequest("Nguoi dung %s khong con trong he thong".formatted(jwt.getSubject())));
    cacheService.getCache(tokenKey, RefreshTokenDto.class).ifPresent(refreshTokenDto -> {
      if (!validateSha256(refreshToken, refreshTokenDto.refreshToken())) {
        cacheService.deleteAllByPattern(REFRESH_TOKEN.formatted(jwt.getSubject(), "*"));
        refreshTokenServiceProxy.clearRefreshToken(response);
        throw new BusinessException(ErrorStatus.UNAUTHORIZED,"Invalid refresh token");
      }
      cacheService.deleteCache(tokenKey);
    });
    return buildLoginResponse(user, sessionId, response);
  }

  @Override
  public void logout(HttpServletResponse response, String refreshToken) {
    Jwt jwt = decoder.decode(refreshToken);
    String tokenKey = REFRESH_TOKEN.formatted(jwt.getSubject(), jwt.getClaimAsString("sId"));
    cacheService.deleteCache(tokenKey);
    refreshTokenServiceProxy.clearRefreshToken(response);
  }

  private LogInResDto buildLoginResponse(AppUser user, UUID sessionId, HttpServletResponse response) {
    var newToken = generateAccessToken(user, validDuration, signerKey, sessionId);
    var newRefreshToken = generateRefreshToken(user, refreshableDuration, signerKey, sessionId);
    saveRefreshToken(user, refreshableDuration, newRefreshToken, sessionId);

    var resBuilder = LogInResDto.builder()
        .username(user.getUsername())
        .fullName(user.getFullName())
        .role(user.getRole().getName())
        .accessToken(newToken);

    refreshTokenServiceProxy.addRefreshToken(response, newRefreshToken);
    return resBuilder
        .build();
  }

  private void saveRefreshToken(AppUser user, long refreshableDuration, String refreshToken, UUID sessionId) {
    RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
        .id(UUID.randomUUID())
        .username(user.getUsername())
        .expiredAt(LocalDateTime.now().plusDays(refreshableDuration))
        .refreshToken(sha256Hex(refreshToken))
        .sessionId(sessionId)
        .build();
    cacheService.setCache(REFRESH_TOKEN.formatted(user.getUsername(), sessionId.toString()),
        refreshTokenDto, refreshableDuration, TimeUnit.DAYS);
  }

  private boolean validateSha256(String token, String hashedToken) {
    return hashedToken.equals(sha256Hex(token));
  }

  private String sha256Hex(String token) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hashed);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }
}
