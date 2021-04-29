package hwanseok.server.ordergroup.repository;

import hwanseok.server.ordergroup.model.entity.OrderGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderGroupRepository extends JpaRepository<OrderGroup,Long> {
}
