package vn.io.nguyen32.crm.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstant {
  // Messages
  public static final String NOT_FOUND_MESSAGE_PREFIX = "Không tìm thấy %s ";
  public static final String CUSTOMER = "khách hàng";
  public static final String NAME = "tên ";

  // MIME Types
  public static final String SHEET_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  // File Names
  public static final String CUSTOMER_FILE_NAME = "customers.xlsx";

  // Excel Headers
  public static final String CUSTOMER_HEADER = "STT,Tên khách hàng,Email,Số điện thoại,Địa chỉ";

  // Regex
  public static final String PHONE_PATTERN = "^0\\d{9}$";

  // error messages
  public static final String LOGIN_FAIL_MSG = "Vui lòng nhập đúng tên đăng nhập và mật khẩu";
}
