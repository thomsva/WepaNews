
package thomsva.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thomsva.domain.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
