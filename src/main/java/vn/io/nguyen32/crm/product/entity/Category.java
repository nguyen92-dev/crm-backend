package vn.io.nguyen32.crm.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import top.nguyennd.restsqlbackend.abstraction.entity.AbstractEntity;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "category")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
public class Category extends AbstractEntity {

    @Column(name = "category_name", unique = true)
    String name;

    @Column(name = "description")
    String description;

    @OneToMany(mappedBy = "category")
    Set<CategorySize> sizes;
}
