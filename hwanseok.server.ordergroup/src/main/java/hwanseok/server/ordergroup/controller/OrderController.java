package hwanseok.server.ordergroup.controller;


import hwanseok.server.ordergroup.model.dto.common.ResponseDto;
import hwanseok.server.ordergroup.model.dto.request.OrderGroupRequestDto;
import hwanseok.server.ordergroup.model.dto.response.OrderGroupResponseDto;
import lombok.RequiredArgsConstructor;
import hwanseok.server.ordergroup.service.OrderGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private final OrderGroupService orderGroupService;


    /**
     * 주문 생성
     * TODO return type ResponseDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderGroupResponseDto> create(@RequestBody OrderGroupRequestDto request){
        return orderGroupService.create(request);
    }

    /**
     * 주문 조회
     * TODO return type ResponseDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderGroupResponseDto> findById(@PathVariable("id") Long id){
        return orderGroupService.findById(id);
    }

    /**
     * 주문 수정
     * TODO return type ResponseDto
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderGroupResponseDto> update(@RequestBody OrderGroupRequestDto request){
        System.out.println("update request"+request);
        return orderGroupService.update(request);
    }

    /**
     * 주문 삭제
     * TODO return type ResponseDto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("id") Long id){
        return orderGroupService.delete(id);
    }
}
