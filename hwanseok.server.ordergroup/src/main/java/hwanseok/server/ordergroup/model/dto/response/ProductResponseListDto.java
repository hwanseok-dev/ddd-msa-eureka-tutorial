package hwanseok.server.ordergroup.model.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 상품 응답 리스트 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@ApiModel
public class ProductResponseListDto {
    /**
     * 상품 응답 리스트
     */
    private List<ProductResponseDto> productResponseDtoList;
}
