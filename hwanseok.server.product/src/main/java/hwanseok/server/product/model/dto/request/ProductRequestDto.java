package hwanseok.server.product.model.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 상품
 */
@Data
@Accessors(chain = true)
@ApiModel
public class ProductRequestDto{
    /**
     * 상품 번호
     */
    private Long id;
    /**
     * 상품 설명
     */
    private String description;
}
