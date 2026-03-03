package vn.io.nguyen32.crm.appuser.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import top.nguyennd.restsqlbackend.abstraction.annotation.IgnoreAuditFields;
import vn.io.nguyen32.crm.appuser.entity.AppUser;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AppUserMapper {

  UserResDto toDto(AppUser user);

  @IgnoreAuditFields
  AppUser toEntity(UserReqDto reqDto);
}
