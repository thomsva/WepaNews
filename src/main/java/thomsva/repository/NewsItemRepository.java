
package thomsva.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thomsva.domain.NewsItem;


public interface NewsItemRepository extends JpaRepository<NewsItem, Long> {
    
}
