package vn.io.nguyen32.crm.appuser;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.nguyennd.restsqlbackend.abstraction.dto.BaseResponse;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.FilterReqDto;
import vn.io.nguyen32.crm.appuser.dto.UserReqDto;
import vn.io.nguyen32.crm.appuser.dto.UserResDto;


@RequestMapping("/users")
public interface AppUserContract {
  @PostMapping("/page-list")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  ResponseEntity<BaseResponse<PagedModel<UserResDto>>> getPageList(@RequestBody FilterReqDto filter,
                                                                   Pageable pageable,
                                                                   @RequestParam(defaultValue = "true")
                                                                   Boolean isPaged);

  @PostMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  ResponseEntity<BaseResponse<UserResDto>> createUser(@Valid @RequestBody UserReqDto reqDto);
}
