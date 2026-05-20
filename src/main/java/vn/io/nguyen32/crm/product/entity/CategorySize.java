package vn.io.nguyen32.crm.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import top.nguyennd.restsqlbackend.abstraction.entity.AbstractEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "size")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class CategorySize extends AbstractEntity {

  @Column(name = "size_name")
  String sizeName;

  @Column(name = "category_id")
  Integer categoryId;

  @Column(name = "price")
  Double price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", referencedColumnName = "id",
  insertable = false, updatable = false)
  Category category;

}
