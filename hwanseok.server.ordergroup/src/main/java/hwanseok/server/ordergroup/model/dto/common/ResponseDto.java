package hwanseok.server.ordergroup.model.dto.common;


import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 응답
 */
@Data
@Accessors(chain = true)
@ApiModel
@Builder
public class ResponseDto {
    /**
     * 응답 메시지
     */
    private String msg;
}
