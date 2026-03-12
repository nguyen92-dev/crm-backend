package vn.io.nguyen32.crm.appuser.impl;

import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.AbstractPagedListService;
import vn.io.nguyen32.crm.appuser.AppUserRepository;
import vn.io.nguyen32.crm.appuser.IAppUserService;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserReqDto;
import vn.io.nguyen32.crm.appuser.dto.UserMapper;
import vn.io.nguyen32.crm.appuser.entity.AppUser;

import java.util.function.Function;

import static vn.io.nguyen32.crm.common.AppConstant.DEFAULT_ROLE;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AppUserServiceImpl extends AbstractPagedListService<AppUser, AppUserBaseResDto, AppUserReqDto> implements IAppUserService {

  AppUserRepository repository;
  UserMapper mapper;
  PasswordEncoder passwordEncoder;

  protected AppUserServiceImpl(AppUserRepository repository,
                               UserMapper mapper,
                               PasswordEncoder passwordEncoder) {
    super(repository);
    this.repository = repository;
    this.mapper = mapper;
    this.passwordEncoder = passwordEncoder;
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
    return mapper.toResDto(repository.save(entity));
  }
}
