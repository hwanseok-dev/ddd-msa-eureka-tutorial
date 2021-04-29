package hwanseok.server.ordergroup.advice;

import hwanseok.server.ordergroup.exception.OrderGroupNotFoundException;
import hwanseok.server.ordergroup.exception.ProductNotFoundException;
import hwanseok.server.ordergroup.model.dto.common.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(OrderGroupNotFoundException.class)
    public ResponseEntity<ErrorDto> orderGroupNotFoundException(OrderGroupNotFoundException e){
        return ResponseEntity.badRequest().body(ErrorDto.builder()
                .msg("OrderGroup Not Found")
                .build());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> productNotFoundException(ProductNotFoundException e){
        return ResponseEntity.badRequest().body(ErrorDto.builder()
                .msg("Product Not Found")
                .build());
    }
}