package hwanseok.server.ordergroup.model.dto.request;


import hwanseok.server.ordergroup.model.entity.LineItem;
import hwanseok.server.ordergroup.model.entity.OrderGroup;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 그룹
 */
@Data
@Accessors(chain = true)
@ApiModel
public class OrderGroupRequestDto {
    /**
     * 주문 번호
     */
    private Long id;
    /**
     * 주문 설명
     */
    private String description;
    /**
     * 개별 주문 리스트
     */
    private List<LineItemRequestDto> lineItemRequestDtos;

    public OrderGroup requestDto2Entity(){
        // create
        // set id, description
        OrderGroup orderGroup = OrderGroup.builder()
                .id(this.id)
                .description(this.description)
                .build();
        // set lineItemLists
        List<LineItem> lineItemList = this.lineItemRequestDtos.stream()
                .map(lineItemDto -> LineItem.builder()
                        .id(lineItemDto.getId())
                        .description(lineItemDto.getDescription())
                        .productId(lineItemDto.getProductId())
                        .orderGroup(orderGroup)
                        .build()).collect(Collectors.toList());
        orderGroup.setLineItems(lineItemList);
        return orderGroup;
    }

}
