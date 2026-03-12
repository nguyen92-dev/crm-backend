package vn.io.nguyen32.crm.auth.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.nguyennd.restsqlbackend.abstraction.common.ErrorStatus;
import top.nguyennd.restsqlbackend.abstraction.exception.BusinessException;
import vn.io.nguyen32.crm.appuser.entity.AppRole;
import vn.io.nguyen32.crm.appuser.entity.AppUser;
import vn.io.nguyen32.crm.appuser.AppUserRepository;
import vn.io.nguyen32.crm.auth.IAuthService;
import vn.io.nguyen32.crm.auth.dto.LogInReqDto;
import vn.io.nguyen32.crm.auth.dto.LogInResDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static top.nguyennd.restsqlbackend.abstraction.exception.BusinessException.badRequest;
import static vn.io.nguyen32.crm.common.AppConstant.LOGIN_FAIL_MSG;

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

    var token = generateToken(user);

    return LogInResDto.builder()
        .username(user.getUsername())
        .fullName(user.getFullName())
        .role(user.getRole().getName())
        .accessToken(token)
        .build();
  }

  private String generateToken(AppUser user) {
    JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
        .subject(user.getUsername())
        .issuer("nguyennd.top")
        .issueTime(new Date())
        .expirationTime(new Date(
            Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()))
        .jwtID(UUID.randomUUID().toString())
        .claim("roles", buildRoles(user.getRole()))
        .claim("scope", buildScope(user))
        .build();

    Payload payload = new Payload(jwtClaimsSet.toJSONObject());

    JWSObject jwsObject = new JWSObject(jwsHeader, payload);

    try {
      jwsObject.sign(new MACSigner(signerKey.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      throw new BusinessException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  private String buildScope(AppUser user) {
    return "ROLE_%s".formatted(user.getRole().getName());
  }

  private List<String> buildRoles(AppRole role) {
    if (role == null) {
      return List.of();
    }
    String rolePrefix = "ROLE_%s";
    return List.of(rolePrefix.formatted(role.getName()));
  }
}
