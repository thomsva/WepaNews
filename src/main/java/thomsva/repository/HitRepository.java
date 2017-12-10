
package thomsva.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thomsva.domain.Hit;


public interface HitRepository extends JpaRepository<Hit, Long>{
    
}
