package fraporti.teste.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepo extends JpaRepository<MapModel, Long>{
  
}
