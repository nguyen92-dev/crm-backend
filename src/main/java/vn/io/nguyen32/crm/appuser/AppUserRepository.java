package vn.io.nguyen32.crm.appuser;

import jakarta.validation.constraints.NotNull;
import top.nguyennd.restsqlbackend.abstraction.repository.IBaseRepository;
import vn.io.nguyen32.crm.appuser.entity.AppUser;

import java.util.Optional;

public interface AppUserRepository extends IBaseRepository<AppUser> {
  Optional<AppUser> findByUsernameIgnoreCase(@NotNull(message = "username không được để trống") String username);
}
