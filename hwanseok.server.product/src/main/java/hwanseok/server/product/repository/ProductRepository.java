package hwanseok.server.product.repository;

import hwanseok.server.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Optional<Product>> findByIdIn(@Param("ids") List<Long> productIds);
}
