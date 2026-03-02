package vn.io.nguyen32.crm.auth;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import top.nguyennd.restsqlbackend.abstraction.dto.BaseResponse;
import vn.io.nguyen32.crm.auth.dto.LogInReqDto;
import vn.io.nguyen32.crm.auth.dto.LogInResDto;

@RequestMapping("api/auth")
public interface IAuthContract {

  @PostMapping("/login")
  ResponseEntity<BaseResponse<LogInResDto>> login(@RequestBody @Valid LogInReqDto reqDto);
}
