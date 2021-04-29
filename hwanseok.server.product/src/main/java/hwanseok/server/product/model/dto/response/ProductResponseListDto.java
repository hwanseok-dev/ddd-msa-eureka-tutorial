package hwanseok.server.product.model.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 상품
 */
@Data
@Accessors(chain = true)
@ApiModel
@Builder
public class ProductResponseListDto {
    private List<ProductResponseDto> productResponseDtoList;
}
