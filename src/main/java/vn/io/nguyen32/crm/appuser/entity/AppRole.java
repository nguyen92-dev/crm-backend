package vn.io.nguyen32.crm.appuser.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import top.nguyennd.restsqlbackend.abstraction.entity.AbstractEntity;
import vn.io.nguyen32.crm.common.AppRoles;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "app_roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
public class AppRole extends AbstractEntity {
  @Column(name = "role_name")
  @Enumerated(EnumType.STRING)
  AppRoles name;

  @Column(name = "description")
  String description;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
      mappedBy = "role")
  List<AppUser> users;
}