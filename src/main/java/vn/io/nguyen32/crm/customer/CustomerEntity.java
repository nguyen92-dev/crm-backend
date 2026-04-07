package vn.io.nguyen32.crm.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import top.nguyennd.restsqlbackend.abstraction.entity.AbstractEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "customer")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
public class CustomerEntity extends AbstractEntity {
  @Column(name = "customer_name")
  String name;

  @Column(name = "phone", unique = true, nullable = false)
  String phone;

  @Column(name = "email")
  String email;

  @Column(name = "facebook")
  String facebook;

  @Column(name = "address")
  String address;
}
