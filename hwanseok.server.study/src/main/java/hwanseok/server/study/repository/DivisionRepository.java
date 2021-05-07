package hwanseok.server.study.repository;

import hwanseok.server.study.entity.DivisionLayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DivisionRepository extends JpaRepository<DivisionLayer,Long> {

    Optional<DivisionLayer> findByUuid(String uuid);
}
