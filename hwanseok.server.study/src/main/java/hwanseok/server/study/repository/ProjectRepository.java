package hwanseok.server.study.repository;

import hwanseok.server.study.entity.GroupLayer;
import hwanseok.server.study.entity.ProjectLayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectLayer,Long> {

    Optional<ProjectLayer> findByUuid(String uuid);
}
