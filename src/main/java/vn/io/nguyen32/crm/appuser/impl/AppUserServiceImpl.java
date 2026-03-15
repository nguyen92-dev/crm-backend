package vn.io.nguyen32.crm.appuser.impl;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.nguyennd.restsqlbackend.abstraction.exception.BusinessException;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.AbstractPagedListService;
import vn.io.nguyen32.crm.appuser.AppUserRepository;
import vn.io.nguyen32.crm.appuser.IAppUserService;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserReqDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserUpdateReqDto;
import vn.io.nguyen32.crm.appuser.dto.UserMapper;
import vn.io.nguyen32.crm.appuser.entity.AppUser;
import vn.io.nguyen32.crm.rediscache.IRedisCacheService;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static vn.io.nguyen32.crm.common.AppConstant.DEFAULT_ROLE;
import static vn.io.nguyen32.crm.rediscache.CommonKey.ENTITY_KEY;

@Slf4j
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AppUserServiceImpl extends AbstractPagedListService<AppUser, AppUserBaseResDto, AppUserReqDto> implements IAppUserService {

  private static final long DEFAULT_USER_TTL_IN_MINUTES = 5;

  AppUserRepository repository;
  UserMapper mapper;
  PasswordEncoder passwordEncoder;
  IRedisCacheService cacheService;

  protected AppUserServiceImpl(AppUserRepository repository,
                               UserMapper mapper,
                               IRedisCacheService cacheService,
                               PasswordEncoder passwordEncoder) {
    super(repository);
    this.repository = repository;
    this.mapper = mapper;
    this.passwordEncoder = passwordEncoder;
    this.cacheService = cacheService;
  }


  @Override
  protected Class<AppUser> getEntityClass() {
    return AppUser.class;
  }

  @Override
  protected Function<AppUser, AppUserBaseResDto> getMapper() {
    return mapper::toResDto;
  }

  @Override
  public AppUserBaseResDto createUser(AppUserReqDto reqDto) {
    validateUniqueFields(reqDto, null);
    AppUser entity = mapper.toEntity(reqDto);
    entity.setPassword(passwordEncoder.encode(reqDto.password()));
    entity.setRoleName(DEFAULT_ROLE);
    var dto = mapper.toResDto(repository.save(entity));
    cacheService.setCache(ENTITY_KEY.formatted(getEntityClass().getSimpleName(), dto.id()),
        dto, DEFAULT_USER_TTL_IN_MINUTES, TimeUnit.MINUTES);
    return dto;
  }

  @Override
  public AppUserBaseResDto updateUser(Long id, AppUserUpdateReqDto reqDto) {
    validateUniqueField("email", reqDto.email(), id);
    var user = repository.findById(id).orElseThrow(
        () -> BusinessException.notFound("Khong tim thay nguoi dung"));
    mapper.updateEntity(reqDto, user);
    var dto = mapper.toResDto(repository.save(user));
    cacheService.setCache(ENTITY_KEY.formatted(getEntityClass().getSimpleName(), dto.id()),
        dto, DEFAULT_USER_TTL_IN_MINUTES, TimeUnit.MINUTES);
    return dto;
  }

  @Override
  public Optional<AppUserBaseResDto> findUserById(long id) {
    if (cacheService.isExist(ENTITY_KEY.formatted(getEntityClass().getSimpleName(), id))) {
      log.info("Get user {} from cache", id);
      return cacheService.getCache(ENTITY_KEY.formatted(getEntityClass().getSimpleName(), id), AppUserBaseResDto.class);
    }
    log.info("Get user {} from database", id);
    var optionalDto = repository.findById(id).map(mapper::toResDto);
    cacheService.setCache(ENTITY_KEY.formatted(getEntityClass().getSimpleName(), id),
        optionalDto.orElse(null), DEFAULT_USER_TTL_IN_MINUTES, TimeUnit.MINUTES);
    return optionalDto;
  }
}
