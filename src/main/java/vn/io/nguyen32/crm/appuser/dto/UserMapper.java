package vn.io.nguyen32.crm.appuser.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import top.nguyennd.restsqlbackend.abstraction.annotation.IgnoreAuditFields;
import top.nguyennd.restsqlbackend.abstraction.annotation.MapAuditFields;
import vn.io.nguyen32.crm.appuser.entity.AppUser;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

  @MapAuditFields
  AppUserBaseResDto toResDto(AppUser entity);

  @IgnoreAuditFields
  AppUser toEntity(AppUserReqDto reqDto);

  @IgnoreAuditFields
  void updateEntity(AppUserUpdateReqDto reqDto, @MappingTarget AppUser entity);
}
