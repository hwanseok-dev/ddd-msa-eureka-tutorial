package hwanseok.server.product.model.dto.response;

import hwanseok.server.product.model.Product;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 상품
 */
@Data
@Accessors(chain = true)
@ApiModel
@Builder
public class ProductResponseDto {
    /**
     * 상품 번호
     */
    private Long id;
    /**
     * 상품 설명
     */
    private String description;

    public Product responseDto2Entity(){
        return Product.builder()
                .id(this.id)
                .description(this.description)
                .build();
    }
}
