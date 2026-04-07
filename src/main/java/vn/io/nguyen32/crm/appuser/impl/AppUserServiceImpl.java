package vn.io.nguyen32.crm.appuser.impl;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.nguyennd.restsqlbackend.abstraction.cache.ICacheService;
import top.nguyennd.restsqlbackend.abstraction.common.ErrorStatus;
import top.nguyennd.restsqlbackend.abstraction.exception.BusinessException;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.AbstractPagedListService;
import vn.io.nguyen32.crm.appuser.AppUserRepository;
import vn.io.nguyen32.crm.appuser.IAppUserService;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserReqDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserUpdateReqDto;
import vn.io.nguyen32.crm.appuser.dto.UserMapper;
import vn.io.nguyen32.crm.appuser.entity.AppUser;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static top.nguyennd.restsqlbackend.abstraction.cache.CommonKey.ENTITY_KEY;
import static vn.io.nguyen32.crm.common.AppConstant.DEFAULT_ROLE;

@Slf4j
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AppUserServiceImpl extends AbstractPagedListService<AppUser, AppUserBaseResDto, AppUserReqDto> implements IAppUserService {

  private static final long DEFAULT_USER_TTL_IN_MINUTES = 5;

  AppUserRepository repository;
  UserMapper mapper;
  PasswordEncoder passwordEncoder;
  ICacheService cacheService;

  protected AppUserServiceImpl(AppUserRepository repository,
                               UserMapper mapper,
                               ICacheService cacheService,
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
  protected Class<AppUserBaseResDto> getResDtoClass() {
    return AppUserBaseResDto.class;
  }

  @Override
  protected Function<AppUser, AppUserBaseResDto> getMapper() {
    return mapper::toResDto;
  }

  @Override
  protected ICacheService getCacheService() {
    return cacheService;
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
    var user = repository.findById(id).orElseThrow(
        () -> BusinessException.notFound("Khong tim thay nguoi dung"));
    validateUniqueField("email", reqDto.email(), id);
    mapper.updateEntity(reqDto, user);
    var dto = mapper.toResDto(repository.save(user));
    cacheService.setCache(ENTITY_KEY.formatted(getEntityClass().getSimpleName(), dto.id()),
        dto, DEFAULT_USER_TTL_IN_MINUTES, TimeUnit.MINUTES);
    return dto;
  }

  @Override
  public AppUserBaseResDto findUserById(long id) {
    return cacheService.getCache(ENTITY_KEY.formatted(getEntityClass().getSimpleName(), id), AppUserBaseResDto.class)
        .or(() -> {
          log.info("Get user {} from database", id);
          var dto = repository.findById(id).map(mapper::toResDto);
          cacheService.setCache(ENTITY_KEY.formatted(getEntityClass().getSimpleName(), id),
              dto.orElse(null), DEFAULT_USER_TTL_IN_MINUTES, TimeUnit.MINUTES);
          return dto;
        }).orElseThrow(
            () -> new BusinessException(ErrorStatus.NOT_FOUND, "Khong tim thay nguoi dung")
        );
  }
}
