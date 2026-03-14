package vn.io.nguyen32.crm.auth;

public interface IRefreshTokenService<T> {
  void addRefreshToken(T response, String refreshToken);
  void clearRefreshToken(T response);
}
