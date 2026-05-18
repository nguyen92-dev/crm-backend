package vn.io.nguyen32.crm.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import vn.io.nguyen32.crm.product.entity.key.SizeKey;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Data
@Entity
@Table(name = "size")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
public class Size {

  @EmbeddedId
  SizeKey id;

  @Column(name = "category_size")
  String categorySize;

  @Column(name = "category_id")
  Integer categoryId;

  @Column(name = "price")
  Double price;

  @CreatedDate
  @Column(
      nullable = false,
      updatable = false
  )
  private LocalDateTime createdAt;
  @CreatedBy
  @Column(
      nullable = false,
      updatable = false
  )
  private String createdBy;
  @LastModifiedDate
  @Column(
      nullable = false
  )
  private LocalDateTime updatedAt;
  @Column(
      nullable = false
  )
  @LastModifiedBy
  private String updatedBy;
}
