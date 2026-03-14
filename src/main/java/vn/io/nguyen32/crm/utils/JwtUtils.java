package vn.io.nguyen32.crm.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.experimental.UtilityClass;
import top.nguyennd.restsqlbackend.abstraction.common.ErrorStatus;
import top.nguyennd.restsqlbackend.abstraction.exception.BusinessException;
import vn.io.nguyen32.crm.appuser.entity.AppRole;
import vn.io.nguyen32.crm.appuser.entity.AppUser;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@UtilityClass
public class JwtUtils {
  public static String generateToken(AppUser user, long validityInSec, String signerKey, UUID sessionId) {
    JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
        .subject(user.getUsername())
        .issuer("nguyennd.top")
        .issueTime(new Date())
        .expirationTime(new Date(
            Instant.now().plus(validityInSec, ChronoUnit.SECONDS).toEpochMilli()))
        .jwtID(UUID.randomUUID().toString())
        .claim("roles", buildRoles(user.getRole()))
        .claim("scope", buildScope(user))
        .claim("sId", sessionId.toString())
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
    if (isNull(user) || isNull(user.getRole())) {
      return "";
    }
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
