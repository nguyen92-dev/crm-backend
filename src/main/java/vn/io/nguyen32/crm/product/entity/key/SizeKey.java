package vn.io.nguyen32.crm.product.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record SizeKey(
    @Column(name = "category_size")
    String categorySize,
    @Column(name = "category_id")
    Integer categoryId
) implements Serializable {
}
