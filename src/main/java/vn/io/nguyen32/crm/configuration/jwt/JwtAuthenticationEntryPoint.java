package vn.io.nguyen32.crm.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(@NonNull HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    var errorCode = HttpServletResponse.SC_UNAUTHORIZED;
    response.setStatus(errorCode);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    var error = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, authException.getMessage());
    ObjectMapper mapper = new ObjectMapper();
    response.getWriter().write(mapper.writeValueAsString(error));
    response.flushBuffer();
  }
}
