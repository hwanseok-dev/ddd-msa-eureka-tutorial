package hwanseok.server.ordergroup.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 상품
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Accessors(chain = true)
@ApiModel
@ToString(exclude = "orderGroup")
public class LineItem implements Serializable {
    /**
     * 개별 주문 번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 개별 주문 설명
     */
    private String description;
    /**
     * 상품 번호
     */
    private Long productId;
    /**
     * 주문 그룹
     */
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_group_id")
    private OrderGroup orderGroup;
}
