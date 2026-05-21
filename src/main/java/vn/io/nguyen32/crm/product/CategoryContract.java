package vn.io.nguyen32.crm.product;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import top.nguyennd.restsqlbackend.abstraction.controller.IPagedListController;
import vn.io.nguyen32.crm.product.dto.CategoryResDto;

@Tag(name = "Quan ly loai san pham", description = "Quan ly loai san pham")
@RequestMapping("api/v1/category")
@Validated
public interface CategoryContract extends IPagedListController<CategoryResDto> {
}
