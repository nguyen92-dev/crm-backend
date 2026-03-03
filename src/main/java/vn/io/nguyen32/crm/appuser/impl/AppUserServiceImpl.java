package vn.io.nguyen32.crm.appuser.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.nguyennd.restsqlbackend.abstraction.pagedlist.AbstractPagedListService;
import vn.io.nguyen32.crm.appuser.AppRoleRepository;
import vn.io.nguyen32.crm.appuser.AppUserRepository;
import vn.io.nguyen32.crm.appuser.IAppUserService;
import vn.io.nguyen32.crm.appuser.dto.AppUserMapper;
import vn.io.nguyen32.crm.appuser.dto.UserReqDto;
import vn.io.nguyen32.crm.appuser.dto.UserResDto;
import vn.io.nguyen32.crm.appuser.entity.AppUser;

import java.util.function.Function;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppUserServiceImpl extends AbstractPagedListService<AppUser, UserResDto, UserReqDto>
    implements IAppUserService {

  AppUserRepository repository;
  AppUserMapper mapper;
  PasswordEncoder passwordEncoder;
  AppRoleRepository roleRepository;

  public AppUserServiceImpl(AppUserRepository repository,
                            AppUserMapper mapper,
                            PasswordEncoder passwordEncoder,
                            AppRoleRepository roleRepository) {
    super(repository);
    this.repository = repository;
    this.mapper = mapper;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
  }

  @Override
  protected Class<AppUser> getEntityClass() {
    return AppUser.class;
  }

  @Override
  protected Function<AppUser, UserResDto> getMapper() {
    return mapper::toDto;
  }

  @Override
  @Transactional
  public UserResDto addAppUser(UserReqDto reqDto) {
    validateUniqueField("username", reqDto.username(), null);
    var user = mapper.toEntity(reqDto);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    var role = roleRepository.findByName("USER").orElse(null);
    user.setRole(role);
    return mapper.toDto(repository.save(user));
  }
}