package waes.assesment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import waes.assesment.resources.entities.DiffDataEntity;

import java.util.UUID;

@Repository
public interface DiffDataRepository extends JpaRepository<DiffDataEntity, UUID> {
}
