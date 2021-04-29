package hwanseok.server.ordergroup.model.dto.response;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 상품 응답 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@ApiModel
public class ProductResponseDto implements Serializable {
    /**
     * 상품 번호
     */
    private Long id;
    /**
     * 상품 설명
     */
    private String description;
}
