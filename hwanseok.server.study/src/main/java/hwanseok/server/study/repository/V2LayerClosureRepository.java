package hwanseok.server.study.repository;

import hwanseok.server.study.entity.LayerClosure;
import hwanseok.server.study.entity.OrganizationLayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface V2LayerClosureRepository extends JpaRepository<LayerClosure,Long> {

    Optional<LayerClosure> findByParentAndChild(Long parent, Long child);
}
