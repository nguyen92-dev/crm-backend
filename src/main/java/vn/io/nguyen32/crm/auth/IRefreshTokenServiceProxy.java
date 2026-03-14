package vn.io.nguyen32.crm.auth;

public interface IRefreshTokenServiceProxy {
  void addRefreshToken(Object response, String refreshToken);
  void clearRefreshToken(Object response);
}
