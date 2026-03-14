package vn.io.nguyen32.crm.auth.impl;

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
import vn.io.nguyen32.crm.auth.dto.LogInReqDto;
import vn.io.nguyen32.crm.auth.dto.LogInResDto;
import vn.io.nguyen32.crm.auth.dto.RefreshTokenDto;

import java.time.LocalDateTime;
import java.util.UUID;

import static top.nguyennd.restsqlbackend.abstraction.exception.BusinessException.badRequest;
import static vn.io.nguyen32.crm.common.AppConstant.LOGIN_FAIL_MSG;
import static vn.io.nguyen32.crm.utils.JwtUtils.generateToken;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

  PasswordEncoder passwordEncoder;
  AppUserRepository userRepository;

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
  public LogInResDto login(LogInReqDto loginReq) {
    var user = userRepository.findByUsernameIgnoreCase(loginReq.username()).orElseThrow(
        () -> badRequest(LOGIN_FAIL_MSG));

    if (!passwordEncoder.matches(loginReq.password(), user.getPassword())) {
      throw badRequest(LOGIN_FAIL_MSG);
    }

    var sessionId = UUID.randomUUID();
    var token = generateToken(user, validDuration, signerKey, sessionId);
    var refreshToken = generateRefreshToken(user, refreshableDuration, signerKey, sessionId);

    return LogInResDto.builder()
        .username(user.getUsername())
        .fullName(user.getFullName())
        .role(user.getRole().getName())
        .accessToken(token)
        .refreshToken(refreshToken)
        .build();
  }

  private String generateRefreshToken(AppUser user, long refreshableDuration, String signerKey, UUID sessionId) {
    String refreshToken = passwordEncoder.encode(UUID.randomUUID().toString());
    RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
        .id(UUID.randomUUID())
        .username(user.getUsername())
        .expiredAt(LocalDateTime.now().plusDays(refreshableDuration))
        .refreshToken(passwordEncoder.encode(refreshToken))
        .build();
    return refreshToken;
  }

}
