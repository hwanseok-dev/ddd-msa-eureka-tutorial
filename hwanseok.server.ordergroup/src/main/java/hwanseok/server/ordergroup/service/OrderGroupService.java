package hwanseok.server.ordergroup.service;

import hwanseok.server.ordergroup.exception.OrderGroupNotFoundException;
import hwanseok.server.ordergroup.model.dto.common.ResponseDto;
import hwanseok.server.ordergroup.model.dto.request.LineItemRequestDto;
import hwanseok.server.ordergroup.model.dto.request.OrderGroupRequestDto;
import hwanseok.server.ordergroup.model.dto.response.OrderGroupResponseDto;
import hwanseok.server.ordergroup.model.dto.response.ProductResponseListDto;
import hwanseok.server.ordergroup.model.entity.OrderGroup;
import hwanseok.server.ordergroup.repository.OrderGroupRepository;
import lombok.RequiredArgsConstructor;
import hwanseok.server.ordergroup.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderGroupService {
    private final OrderGroupRepository orderGroupRepository;

    @Value("${product.api.url}")
    private String productApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * OrderGroup 조회
     */
    public ResponseEntity<OrderGroupResponseDto> findById(Long id) {
        Optional<OrderGroup> option = orderGroupRepository.findById(id);
        return option.map(orderGroup -> ResponseEntity
                .ok()
                .body(orderGroup.entity2ResponseDto())
        ).orElseThrow(OrderGroupNotFoundException::new);
    }

    /**
     * OrderGroup 생성
     */
    public ResponseEntity<OrderGroupResponseDto> create(OrderGroupRequestDto request) {
        String productIds = request.getLineItemRequestDtos().stream()
                .map(LineItemRequestDto::getProductId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        System.out.println(productIds);
        ProductResponseListDto listDto = getProductByIds(productIds);
        if(listDto.getProductResponseDtoList().size() != request.getLineItemRequestDtos().size()){
            throw new ProductNotFoundException();
        }
        OrderGroup newOrderGroup = orderGroupRepository.save(request.requestDto2Entity());
        return ResponseEntity
                .ok()
                .body(newOrderGroup.entity2ResponseDto());
    }

    /**
     * OrderGroup 수정
     */
    public ResponseEntity<OrderGroupResponseDto> update(OrderGroupRequestDto request) {
        Optional<OrderGroup> optional = orderGroupRepository.findById(request.getId());
        if (optional.isPresent()) {
            OrderGroup updateOrderGroup = orderGroupRepository.save(request.requestDto2Entity());
            return ResponseEntity.ok().body(updateOrderGroup.entity2ResponseDto());
        } else {
            throw new OrderGroupNotFoundException();
        }
    }

    /**
     * OrderGroup 삭제
     */
    public ResponseEntity<ResponseDto> delete(Long id) {
        Optional<OrderGroup> option = orderGroupRepository.findById(id);
        if (option.isPresent()) {
            orderGroupRepository.deleteById(id);
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .msg("Successfully deleted orderGroup")
                    .build());
        } else {
            throw new OrderGroupNotFoundException();
        }
    }

    /**
     * @param productIds @PathVariable로 전달될 productId
     * @return
     */
    public ProductResponseListDto getProductByIds(String productIds) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString("http://simple-msa-product")
                .path("/api/product")
                .queryParam("ids", productIds);
        System.out.println(builder.build(productIds).toString());
        ResponseEntity<ProductResponseListDto> ret = restTemplate.getForEntity(
                builder.build(productIds).toString(),
                ProductResponseListDto.class
        );
        return ret.getBody();
    }

}
