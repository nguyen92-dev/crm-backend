package vn.io.nguyen32.crm.configuration.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import top.nguyennd.restsqlbackend.abstraction.common.ErrorStatus;
import top.nguyennd.restsqlbackend.abstraction.exception.BusinessException;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Date;

import static java.util.Objects.isNull;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JwtCustomDecoder implements JwtDecoder {
  @NonFinal
  @Value("${jwt.secret}")
  String secret;

  @NonFinal
  @Value("${jwt.valid-duration}")
  Long duration;

  @NonFinal
  private NimbusJwtDecoder nimbusJwtDecoder = null;

  @Override
  public Jwt decode(String token) throws JwtException {
    verifyToken(token);
    if (isNull(nimbusJwtDecoder)) {
      SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HS512");
      nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
          .macAlgorithm(MacAlgorithm.HS512)
          .build();
    }
    return nimbusJwtDecoder.decode(token);
  }

  private void verifyToken(String token) {
    try {
      JWSVerifier verifier = new MACVerifier(secret.getBytes());
      SignedJWT signedJWT = SignedJWT.parse(token);

      Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

      var verified = signedJWT.verify(verifier);

      if (!(verified && expiryTime.after(new Date()))) throw new BusinessException(ErrorStatus.UNAUTHORIZED,"Invalid token");

    } catch (JOSEException | ParseException _) {
      throw new BusinessException(ErrorStatus.UNAUTHORIZED,"Invalid token");
    }
  }
}
