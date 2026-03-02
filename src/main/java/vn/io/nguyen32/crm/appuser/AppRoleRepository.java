package vn.io.nguyen32.crm.appuser;

import top.nguyennd.restsqlbackend.abstraction.repository.IBaseRepository;
import vn.io.nguyen32.crm.appuser.entity.AppRole;

import java.util.Optional;

public interface AppRoleRepository extends IBaseRepository<AppRole> {
  Optional<AppRole> findByName(String user);
}
