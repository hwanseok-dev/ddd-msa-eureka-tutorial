package hwanseok.server.product.controller;


import lombok.RequiredArgsConstructor;
import hwanseok.server.product.model.dto.request.ProductRequestDto;
import hwanseok.server.product.model.dto.response.ProductResponseDto;
import hwanseok.server.product.model.dto.response.ProductResponseListDto;
import hwanseok.server.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/study", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable("id") Long id){
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto request){
        return productService.create(request);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponseDto> update(@RequestBody ProductRequestDto request){
        return productService.update(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        return productService.delete(id);
    }

    @GetMapping("")
    public ResponseEntity<ProductResponseListDto> readByIds(@RequestParam("ids") String productIds) {
        List<Long> productIdList = new ArrayList<>(Arrays.asList(productIds.split(",")))
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return productService.findByIds(productIdList);
    }
}
