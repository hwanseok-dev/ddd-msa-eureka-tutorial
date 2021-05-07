package hwanseok.server.study.repository;

import hwanseok.server.study.entity.OrganizationLayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationLayer,Long> {

    Optional<OrganizationLayer> findByUuid(String uuid);
}
