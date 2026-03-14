package vn.io.nguyen32.crm.auth.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.io.nguyen32.crm.appuser.entity.AppUser;
import vn.io.nguyen32.crm.appuser.AppUserRepository;
import vn.io.nguyen32.crm.auth.IAuthService;
import vn.io.nguyen32.crm.auth.IRefreshTokenServiceProxy;
import vn.io.nguyen32.crm.auth.dto.LogInReqDto;
import vn.io.nguyen32.crm.auth.dto.LogInResDto;
import vn.io.nguyen32.crm.auth.dto.RefreshTokenDto;
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
    var token = generateAccessToken(user, validDuration, signerKey, sessionId);
    var refreshToken = generateRefreshToken(user, refreshableDuration, signerKey, sessionId);
    saveRefreshToken(user, refreshableDuration, refreshToken, sessionId);

    var resBuilder = LogInResDto.builder()
        .username(user.getUsername())
        .fullName(user.getFullName())
        .role(user.getRole().getName())
        .accessToken(token);

    refreshTokenServiceProxy.addRefreshToken(response, refreshToken);
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
