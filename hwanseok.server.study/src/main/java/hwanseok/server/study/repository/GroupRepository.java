package hwanseok.server.study.repository;

import hwanseok.server.study.entity.DivisionLayer;
import hwanseok.server.study.entity.GroupLayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupLayer,Long> {

    Optional<GroupLayer> findByUuid(String uuid);
}
