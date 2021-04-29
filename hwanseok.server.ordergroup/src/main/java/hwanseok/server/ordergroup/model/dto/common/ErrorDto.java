package hwanseok.server.ordergroup.model.dto.common;


import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 에러
 */
@Data
@Accessors(chain = true)
@ApiModel
@Builder
public class ErrorDto {
    /**
     * 에러 메시지
     */
    private String msg;
}
