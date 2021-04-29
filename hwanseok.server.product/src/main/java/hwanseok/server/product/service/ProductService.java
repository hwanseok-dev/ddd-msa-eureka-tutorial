package hwanseok.server.product.service;

import hwanseok.server.product.model.Product;
import hwanseok.server.product.model.dto.request.ProductRequestDto;
import hwanseok.server.product.model.dto.response.ProductResponseDto;
import hwanseok.server.product.model.dto.response.ProductResponseListDto;
import hwanseok.server.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import hwanseok.server.product.exception.ProductConstraintViolationException;
import hwanseok.server.product.exception.ProductNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ResponseEntity<ProductResponseDto> findById(Long id) {
        Optional<Product> option = productRepository.findById(id);
        return option.map(product -> ResponseEntity
                .status(HttpStatus.OK)
                .body(ProductResponseDto.builder()
                        .id(product.getId())
                        .description(product.getDescription()).build()))
                .orElseThrow(ProductNotFoundException::new);
    }

    public ResponseEntity<ProductResponseListDto> findByIds(List<Long> ids) {
        List<Optional<Product>> options = productRepository.findByIdIn(ids);
        System.out.println(options);
        if (options.size() != ids.size()) {
            throw new ProductNotFoundException();
        }
        return ResponseEntity.ok().body(ProductResponseListDto.builder()
                .productResponseDtoList(options.stream().map(Optional::get).map(product ->
                        ProductResponseDto.builder()
                                .id(product.getId())
                                .description(product.getDescription())
                                .build()).collect(Collectors.toList()))
                .build());
    }

    public ResponseEntity<ProductResponseDto> create(ProductRequestDto request) {
        Product newProduct = productRepository.save(
                Product.builder()
                        .description(request.getDescription())
                        .build());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductResponseDto.builder()
                        .id(newProduct.getId())
                        .description(newProduct.getDescription())
                        .build()
                );
    }

    public ResponseEntity<ProductResponseDto> update(ProductRequestDto request) {
        Optional<Product> option = productRepository.findById(request.getId());
        return option.map(product -> {
            product.setDescription(request.getDescription());
            productRepository.save(product);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ProductResponseDto.builder()
                            .id(product.getId())
                            .description(product.getDescription())
                            .build());
        })
                .orElseThrow(ProductNotFoundException::new);
    }

    public ResponseEntity<String> delete(Long id) {
        Optional<Product> option = productRepository.findById(id);
        try {
            if (option.isPresent()) {
                productRepository.deleteById(option.get().getId());
                return ResponseEntity.ok().body("Successfully deleted");
            } else {
                throw new ProductNotFoundException();
            }
        } catch (DataIntegrityViolationException e) {
            throw new ProductConstraintViolationException();
        }

    }

}
