package vn.io.nguyen32.crm.appuser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import top.nguyennd.restsqlbackend.abstraction.entity.AbstractEntity;
import vn.io.nguyen32.crm.common.AppRoles;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "app_users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
public class AppUser extends AbstractEntity {
  @Column(name = "username", unique = true, length = 50)
  String username;

  @Column(name = "password")
  String password;

  @Column(name = "email", unique = true, length = 100)
  String email;

  @Column(name = "full_name", length = 100)
  String fullName;

  @Column(name = "deleted_at")
  LocalDateTime deletedAt;

  @Column(name = "role_name")
  @Enumerated(EnumType.STRING)
  AppRoles roleName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_name", referencedColumnName = "role_name",
      insertable = false, updatable = false)
  AppRole role;
}
