package vn.io.nguyen32.crm.customer.impl;

import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import top.nguyennd.restsqlbackend.abstraction.cache.ICacheService;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.AbstractPagedListService;
import vn.io.nguyen32.crm.customer.CustomerEntity;
import vn.io.nguyen32.crm.customer.CustomerRepository;
import vn.io.nguyen32.crm.customer.ICustomerService;
import vn.io.nguyen32.crm.customer.dto.CustomerMapper;
import vn.io.nguyen32.crm.customer.dto.CustomerReqDTO;
import vn.io.nguyen32.crm.customer.dto.CustomerResDTO;

import java.util.function.Function;

import static top.nguyennd.restsqlbackend.abstraction.cache.CommonKey.ENTITY_KEY;
import static top.nguyennd.restsqlbackend.abstraction.exception.BusinessException.notFound;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomerServiceImpl extends AbstractPagedListService<CustomerEntity, CustomerResDTO, CustomerReqDTO>
    implements ICustomerService {

  CustomerRepository repository;
  CustomerMapper mapper;
  ICacheService cacheService;

  public CustomerServiceImpl(CustomerRepository repository,
                             CustomerMapper mapper,
                             ICacheService cacheService) {
    super(repository);
    this.repository = repository;
    this.mapper = mapper;
    this.cacheService = cacheService;
  }

  @Override
  protected Class<CustomerEntity> getEntityClass() {
    return CustomerEntity.class;
  }

  @Override
  protected Class<CustomerResDTO> getResDtoClass() {
    return CustomerResDTO.class;
  }

  @Override
  protected Function<CustomerEntity, CustomerResDTO> getMapper() {
    return mapper::toDto;
  }

  @Override
  protected ICacheService getCacheService() {
    return cacheService;
  }

  @Override
  public CustomerResDTO createCustomer(CustomerReqDTO reqDto) {
    validateUniqueFields(reqDto, null);
    CustomerEntity entity = mapper.toEntity(reqDto);
    CustomerResDTO dto = mapper.toDto(repository.save(entity));
    cacheService.setCache(ENTITY_KEY.formatted(getEntityClass().getSimpleName(), dto.id()), dto);
    return dto;
  }

  @Override
  public CustomerResDTO updateCustomer(Long id, CustomerReqDTO reqDto) {
    validateUniqueFields(reqDto, id);
    CustomerEntity entity = repository.findById(id).orElseThrow(
        () -> notFound("Không tìm thấy khách hàng"));
    mapper.updateEntity(reqDto, entity);
    return mapper.toDto(repository.save(entity));
  }
}
