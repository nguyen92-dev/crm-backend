package vn.io.nguyen32.crm.appuser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import top.nguyennd.restsqlbackend.abstraction.dto.BaseResponse;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.FilterReqDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserReqDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserUpdateReqDto;

@Tag(name = "Quan ly nguoi dung", description = "Quan ly nguoi dung")
@RequestMapping("api/v1/user")
@Validated
public interface IAppUserContract {
  @Operation(summary = "Danh sach nguoi dung", description = "Danh sach nguoi dung")
  @PostMapping("/paged-list")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<BaseResponse<PagedModel<AppUserBaseResDto>>> getPagedList(Pageable pageable,
                                                                           FilterReqDto filter);

  @Operation(summary = "Them nguoi dung", description = "Them nguoi dung")
  @PostMapping()
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<BaseResponse<AppUserBaseResDto>> createUser(@Valid @RequestBody AppUserReqDto reqDto);

  @Operation(summary = "Chi tiet nguoi dung", description = "Chi tiet nguoi dung")
  @GetMapping("/{id}")
  ResponseEntity<BaseResponse<AppUserBaseResDto>> getById(@PathVariable Integer id);

  @Operation(summary = "Cap nhat nguoi dung", description = "Cap nhat nguoi dung")
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<BaseResponse<AppUserBaseResDto>> updateUser(@PathVariable Long id,
                                                             @RequestBody @Valid AppUserUpdateReqDto reqDto);

  @Operation(summary = "Xoa nguoi dung", description = "Xoa nguoi dung")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<BaseResponse<Void>> deleteUser(@PathVariable Long id);
}
