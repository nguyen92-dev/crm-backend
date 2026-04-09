package vn.io.nguyen32.crm.customer.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import top.nguyennd.restsqlbackend.abstraction.annotation.IgnoreAuditFields;
import top.nguyennd.restsqlbackend.abstraction.annotation.MapAuditFields;
import vn.io.nguyen32.crm.customer.CustomerEntity;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CustomerMapper {

  @MapAuditFields
  CustomerResDTO toDto(CustomerEntity entity);

  @IgnoreAuditFields
  CustomerEntity toEntity(CustomerReqDTO dto);

  @IgnoreAuditFields
  void updateEntity(CustomerReqDTO dto, @MappingTarget CustomerEntity entity);
}
