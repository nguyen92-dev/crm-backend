package vn.io.nguyen32.crm.rediscache;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonKey {
  public static final String REFRESH_TOKEN = "refreshToken:%s:%s";
  public static final String BLACKLIST_TOKEN = "blacklistToken:%s";
}
