package vn.io.nguyen32.crm.appuser.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import vn.io.nguyen32.crm.appuser.entity.AppUser;
import vn.io.nguyen32.crm.common.annotation.MapAuditFields;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

  @MapAuditFields
  AppUserBaseResDto toResDto(AppUser entity);

  AppUser toEntity(AppUserReqDto reqDto);

  void updateEntity(AppUserReqDto reqDto, @MappingTarget AppUser entity);
}
